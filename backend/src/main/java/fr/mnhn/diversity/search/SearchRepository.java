package fr.mnhn.diversity.search;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Repository used to execute a full-text search on pages
 * @author JB Nizet
 */
@Repository
public class SearchRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public SearchRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<PageSearchResult> search(String searchText) {
        // Explanation of this horrible query:
        // Let's start by the inner query.
        // it selects from the page and its elements
        // and finds the elements whose text matches with the search text: `search_text @@ query`
        // The two operands of this comparison are
        //  - the entered text is the text entered by the user, and transformed into a plain text query
        //    (see https://www.postgresql.org/docs/12/functions-textsearch.html)
        //  - the text of the element, transformed into a vector, with a weight of 'A' for titles or 'D' for regular texts
        //    (see https://www.postgresql.org/docs/12/functions-textsearch.html)
        // In addition to the values stored in the two tables, the query also computes, for each row,
        // its search rank (with `ts_rank_cd()`.
        //
        // so the intermediary result of the inner query for the search text "compteur diversité" would be rows such as
        //
        //     2, 'About', 'about', 'A propos', 'Les compteurs de diversité sont...', <query>, 0.33
        //
        // But since multiple pieces of text of a same page can be returned by this query, and we only want
        // one result per page, query partitions this intermediary result by page ID, sorts every row of a page by
        // search rank and ID, and assigns a position (1 for the best rank, 2 for the second best, etc.) to each row of
        // a page.
        //
        // So the actual result of the inner query for the search text "compteur biodiversité" would be rows such as
        //
        //     2, 'About', 'about', 'A propos', 0.66, 0.5, `les compteurs de diversité sont...`, 1
        //     2, 'About', 'about', 'A propos', 0.66, 0.16, `un compteur ...`                  , 2
        //     1, 'Home', 'home', 'Accueil', 0.5, 0.5, `Compteurs de diversité<`               , 1
        //
        // the outer query then only returns the rows with a position of 1, sorts the rows by descending rank sum,
        // and extracts the highlights (with `ts_headline()` from the selected (and thus most relevant) text of the page

        String query =
            //language=SQL
            "select id, name, model_name, title, ts_headline('french', text, query, 'ShortWord=0, MaxFragments=5') as highlight\n" +
                "from (\n" +
                "    select page.id,\n" +
                "        page.name,\n" +
                "        page.model_name,\n" +
                "        page.title,\n" +
                "        pe.text,\n" +
                "        query,\n" +
                "        rank() over (partition by page.id order by ts_rank_cd(search_text, query, 0) desc, pe.id asc) as position,\n" +
                "        sum(ts_rank_cd(search_text, query, 0)) over ( partition by page.id) as rank_sum\n" +
                "    from page \n" +
                "            inner join page_element pe on page.id = pe.page_id,\n" +
                "        plainto_tsquery('french', :searchText) query,\n" +
                "        setweight(to_tsvector('french', pe.text), (case when pe.title then 'A' else 'D' end)::\"char\") search_text\n" +
                "    where search_text @@ query\n" +
                ") as result\n" +
                "where position = 1\n" +
                "order by rank_sum desc";

        return jdbcTemplate.query(query, Map.of("searchText", searchText), (rs, rowNum) ->
            new PageSearchResult(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("model_name"),
                rs.getString("title"),
                rs.getString("highlight"),
                null
            )
        );
    }
}
