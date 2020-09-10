package fr.mnhn.diversity.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Repository used to deal with pages
 * @author JB Nizet
 */
@Repository
public class PageRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public PageRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Page> findById(Long id) {
        String sql =
            "select page.id, page.name, page.model_name, page.title, el.id as element_id, el.type, el.key, el.text, el.image_id, el.alt, el.href" +
                " from page" +
                " left outer join page_element el on page.id = el.page_id" +
                " where page.id = :id";
        return jdbcTemplate.query(sql, Map.of("id", id), this::extractPage);
    }

    public Optional<Page> findByNameAndModel(String name, String modelName) {
        String sql =
            "select page.id, page.name, page.model_name, page.title, el.id as element_id, el.type, el.key, el.text, el.image_id, el.alt, el.href" +
                " from page" +
                " left outer join page_element el on page.id = el.page_id" +
                " where page.name = :name and page.model_name = :modelName";
        return jdbcTemplate.query(sql, Map.of("name", name, "modelName", modelName), this::extractPage);
    }

    public List<Page> findByModel(String modelName) {
        String sql =
            "select page.id, page.name, page.model_name, page.title, el.id as element_id, el.type, el.key, el.text, el.image_id, el.alt, el.href" +
                " from page" +
                " left outer join page_element el on page.id = el.page_id" +
                " where page.model_name = :modelName";
        return jdbcTemplate.query(sql, Map.of("modelName", modelName), this::extractPages);
    }



    private List<Page> extractPages(ResultSet rs) throws SQLException {
        Map<Long, PageData> pageDataById = new HashMap<>();
        Map<Long, List<Element>> elementsById = new HashMap<>();
        while (rs.next()) {
            Long pageId = rs.getLong("id");
            String name = rs.getString("name");
            String modelName = rs.getString("model_name");
            String title = rs.getString("title");
            pageDataById.computeIfAbsent(pageId, id -> new PageData(id, name, modelName, title));
            List<Element> elements = elementsById.computeIfAbsent(pageId, id -> new ArrayList<>());

            long elementId = rs.getLong("element_id");
            if (!rs.wasNull()) { // in case a page has no element
                ElementType type = ElementType.valueOf(rs.getString("type"));
                String key = rs.getString("key");
                switch (type) {
                    case TEXT:
                        elements.add(Element.text(elementId, key, rs.getString("text")));
                        break;
                    case IMAGE:
                        elements.add(Element.image(elementId, key, rs.getLong("image_id"), rs.getString("alt")));
                        break;
                    case LINK:
                        elements.add(Element.link(elementId, key, rs.getString("text"), rs.getString("href")));
                        break;
                    default:
                        throw new IllegalStateException("Unknown element type: " + type);
                }
            }
        }

        return pageDataById.values()
                           .stream()
                           .sorted(Comparator.comparing(pageData -> pageData.id))
                           .map(pageData -> new Page(pageData.id,
                                                     pageData.name,
                                                     pageData.modelName,
                                                     pageData.title,
                                                     elementsById.get(pageData.id)))
                           .collect(Collectors.toList());
    }

    private Optional<Page> extractPage(ResultSet rs) throws SQLException {
        List<Page> pages = extractPages(rs);
        return pages.isEmpty() ? Optional.empty() : Optional.of(pages.get(0));
    }

    private static class PageData {
        private final Long id;
        private final String name;
        private final String modelName;
        private final String title;

        public PageData(Long id, String name, String modelName, String title) {
            this.id = id;
            this.name = name;
            this.modelName = modelName;
            this.title = title;
        }
    }
}
