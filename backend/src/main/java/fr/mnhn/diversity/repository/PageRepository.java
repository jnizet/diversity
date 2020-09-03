package fr.mnhn.diversity.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    public Optional<Page> get(Long id) {
        String sql =
            "select page.name, page.model_name, el.id as element_id, el.type, el.key, el.text, el.image_id, el.alt, el.href" +
                " from page" +
                " left outer join page_element el on page.id = el.page_id" +
                " where page.id = :id" +
                " order by el.key";
        return jdbcTemplate.query(sql, Map.of("id", id), rs -> {
           PageData pageData = null;
           List<Element> elements = new ArrayList<>();
           while (rs.next()) {
               if (pageData == null) {
                   pageData = new PageData(rs.getString("name"), rs.getString("model_name"));
               }
               long elementId = rs.getLong("element_id");
               ElementType type = ElementType.valueOf(rs.getString("type"));
               String key = rs.getString("key");
               switch (type) {
                   case TEXT:
                       elements.add(Element.text(elementId, key, rs.getString("text")));
                       break;
                   case IMAGE:
                       elements.add(Element.image(elementId, key, rs.getString("image_id"), rs.getString("alt")));
                       break;
                   case LINK:
                       elements.add(Element.link(elementId, key, rs.getString("text"), rs.getString("href")));
                       break;
                   default:
                       throw new IllegalStateException("Unknown element type: " + type);
               }
           }

           return pageData == null ? Optional.empty() : Optional.of(new Page(id, pageData.name, pageData.modelName, elements));
        });
    }

    private static class PageData {
        private final String name;
        private final String modelName;

        public PageData(String name, String modelName) {
            this.name = name;
            this.modelName = modelName;
        }
    }
}
