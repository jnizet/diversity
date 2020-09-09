package fr.mnhn.diversity.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Repository used to deal with pages
 * @author JB Nizet
 */
@Repository
public class PageRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private ResultSetExtractor<Optional<Page>> pageExtractor;

    public PageRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Page> findById(Long id) {
        String sql =
            "select page.id, page.name, page.model_name, el.id as element_id, el.type, el.key, el.text, el.image_id, el.alt, el.href" +
                " from page" +
                " left outer join page_element el on page.id = el.page_id" +
                " where page.id = :id";
        return jdbcTemplate.query(sql, Map.of("id", id), this::extractPage);
    }

    public Optional<Page> findByNameAndModel(String name, String modelName) {
        String sql =
            "select page.id, page.name, page.model_name, el.id as element_id, el.type, el.key, el.text, el.image_id, el.alt, el.href" +
                " from page" +
                " left outer join page_element el on page.id = el.page_id" +
                " where page.name = :name and page.model_name = :modelName";
        return jdbcTemplate.query(sql, Map.of("name", name, "modelName", modelName), this::extractPage);
    }

    private Optional<Page> extractPage(ResultSet rs) throws SQLException {
        PageData pageData = null;
        List<Element> elements = new ArrayList<>();
        while (rs.next()) {
            if (pageData == null) {
                pageData = new PageData(rs.getLong("id"), rs.getString("name"), rs.getString("model_name"));
            }
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

        return pageData == null ? Optional.empty() : Optional.of(new Page(pageData.id,
                                                                          pageData.name,
                                                                          pageData.modelName,
                                                                          elements));
    }

    private static class PageData {
        private final Long id;
        private final String name;
        private final String modelName;

        public PageData(Long id, String name, String modelName) {
            this.id = id;
            this.name = name;
            this.modelName = modelName;
        }
    }
}
