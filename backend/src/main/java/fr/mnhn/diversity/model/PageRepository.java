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

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
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

    /**
     * Updates a page, its metadata and elements, by removing all the elements and re-creating them.
     */
    public boolean update(Page page) {
        // remove previous elements
        removeElementsFromPage(page.getId());

        // visit all elements and create them
        ElementCreatorVisitor elementCreatorVisitor = new ElementCreatorVisitor(page.getId());
        page.getElements().forEach((elementKey, element) -> element.accept(elementCreatorVisitor));

        // update page metadata (title)
        Map<String, Object> paramMap = Map.of(
            "id", page.getId(),
            "title", page.getTitle()
        );
        int updatedRows = jdbcTemplate.update("update page set title = :title where id = :id", paramMap);
        return updatedRows > 0;
    }

    private void removeElementsFromPage(Long pageId) {
        Map<String, Object> paramMap = Map.of(
            "id", pageId
        );
        jdbcTemplate.update("delete from page_element where page_id = :id", paramMap);
    }

    /**
     * Creates a new {@link Text}
     */
    public Text createText(Long pageId, Text text) {
        Map<String, Object> paramMap = Map.of(
            "page_id", pageId,
            "type", text.getType().name(),
            "key", text.getKey(),
            "text", text.getText()
        );
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update("insert into page_element (id, page_id, type, key, text) values (nextval('page_element_seq'), :page_id, :type, :key, :text)", new MapSqlParameterSource(paramMap), keyHolder);
        Long id = (Long) keyHolder.getKeys().get("id");
        return new Text(id, text.getKey(), text.getText());
    }

    /**
     * Creates a new {@link Link}
     */
    private Link createLink(Long pageId, Link link) {
        Map<String, Object> paramMap = Map.of(
            "page_id", pageId,
            "type", link.getType().name(),
            "key", link.getKey(),
            "text", link.getText(),
            "href", link.getHref()
        );
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update("insert into page_element (id, page_id, type, key, text, href) values (nextval('page_element_seq'), :page_id, :type, :key, :text, :href)", new MapSqlParameterSource(paramMap), keyHolder);
        Long id = (Long) keyHolder.getKeys().get("id");
        return new Link(id, link.getKey(), link.getText(), link.getHref());
    }

    /**
     * Creates a new {@link Image}
     */
    private Image createImage(Long pageId, Image image) {
        Map<String, Object> paramMap = Map.of(
            "page_id", pageId,
            "type", image.getType().name(),
            "key", image.getKey(),
            "image_id", image.getImageId(),
            "alt", image.getAlt()
        );
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update("insert into page_element (id, page_id, type, key, image_id, alt) values (nextval('page_element_seq'), :page_id, :type, :key, :image_id, :alt)", new MapSqlParameterSource(paramMap), keyHolder);
        Long id = (Long) keyHolder.getKeys().get("id");
        return new Image(id, image.getKey(), image.getImageId(), image.getAlt(), image.isMultiSize());
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
                        elements.add(Element.image(elementId, key, rs.getLong("image_id"), rs.getString("alt"), false));
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

    /**
     * Visitor in charge of creating page elements
     */
    private class ElementCreatorVisitor implements ElementVisitor<Element> {

        private final Long pageId;

        public ElementCreatorVisitor(Long pageId) {
            this.pageId = pageId;
        }

        @Override
        public Text visitText(Text text) {
            return createText(pageId, text);
        }

        @Override
        public Image visitImage(Image image) {
            return createImage(pageId, image);
        }

        @Override
        public Link visitLink(Link link) {
            return createLink(pageId, link);
        }
    }
}
