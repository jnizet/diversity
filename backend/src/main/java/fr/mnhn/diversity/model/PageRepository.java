package fr.mnhn.diversity.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import fr.mnhn.diversity.indicator.IndicatorModel;
import fr.mnhn.diversity.territory.Territory;
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

    public Optional<BasicPage> findBasicByNameAndModel(String name, String modelName) {
        String sql =
            "select page.id, page.name, page.model_name, page.title" +
                " from page" +
                " where page.name = :name and page.model_name = :modelName";
        return jdbcTemplate.query(sql, Map.of("name", name, "modelName", modelName), this::extractBasicPage);
    }

    public List<BasicPage> findBasicByModel(String modelName) {
        String sql =
            "select page.id, page.name, page.model_name, page.title" +
                " from page" +
                " where page.model_name = :modelName" +
                " order by page.id";
        return jdbcTemplate.query(sql, Map.of("modelName", modelName), this::extractBasicPages);
    }

    public List<BasicPage> findAllBasic() {
        String sql =
            "select page.id, page.name, page.model_name, page.title" +
                " from page" +
                " order by page.id";
        return jdbcTemplate.query(sql, this::extractBasicPages);
    }

    /**
     * Finds the next page which has the given model, when ordering these pages by ID.
     * If no page has an ID greater than the given one, returns the first one.
     * If the given current page ID is unique, then the page with that ID will be returned.
     * If there is no page at all with the given model name, then empty is returned.
     */
    public Optional<Page> findNextOrFirstByModel(String modelName, Long currentPageId) {
        String sql =
            "with next_id as (\n"
                + "    select min(p.id) as id\n"
                + "    from page p\n"
                + "    where p.model_name = :modelName\n"
                + "      and p.id > :currentPageId\n"
                + "),\n"
                + "     first_id as (\n"
                + "         select min(id) as id\n"
                + "         from page p\n"
                + "         where p.model_name = :modelName\n"
                + "     ),\n"
                + "     searched_id as (\n"
                + "         select max(u.id) as id from (select id from next_id union select id from first_id) as u\n"
                + "     )\n"
                + "select page.id,\n"
                + "       page.name,\n"
                + "       page.model_name,\n"
                + "       page.title,\n"
                + "       el.id as element_id,\n"
                + "       el.type,\n"
                + "       el.key,\n"
                + "       el.text,\n"
                + "       el.image_id,\n"
                + "       el.alt,\n"
                + "       el.href\n"
                + "from page\n"
                + "         left outer join page_element el on page.id = el.page_id\n"
                + "where page.id = (select id from searched_id)";
        return jdbcTemplate.query(sql, Map.of("modelName", modelName, "currentPageId", currentPageId), this::extractPage);
    }

    /**
     * Finds up to N random indicator pages where an indicator value exists for the given territory
     */
    public List<Page> findRandomIndicatorPagesForTerritory(int max, Territory territory) {
        String sql =
            "with page_ids as\n"
                + "         (select page.id\n"
                + "          from page page\n"
                + "                   inner join indicator on indicator.slug = page.name\n"
                + "                   inner join indicator_value iv on indicator.id = iv.indicator_id\n"
                + "          where page.model_name = :indicatorModelName\n"
                + "            and iv.territory = :territory\n"
                + "          order by random()\n"
                + "          limit :max)\n"
                + "select page.id,\n"
                + "       page.name,\n"
                + "       page.model_name,\n"
                + "       page.title,\n"
                + "       el.id as element_id,\n"
                + "       el.type,\n"
                + "       el.key,\n"
                + "       el.text,\n"
                + "       el.image_id,\n"
                + "       el.alt,\n"
                + "       el.href\n"
                + "from page\n"
                + "         left outer join page_element el on page.id = el.page_id\n"
                + "         inner join page_ids on page.id = page_ids.id";

        return jdbcTemplate.query(
            sql,
            Map.of(
                "territory", territory.name(),
                "max", max,
                "indicatorModelName", IndicatorModel.INDICATOR_PAGE_MODEL.getName()
            ),
            this::extractPages);
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

    public void deleteByNameAndModel(String name, String modelName) {
        String deleteElementsSql = "delete from page_element pe where pe.page_id = ("+
            " select p.id from page p" +
            " where p.name = :name and p.model_name = :modelName"
            + ")";
        String deletePageSql = "delete from page p" +
            " where p.name = :name and p.model_name = :modelName";

        Map<String, Object> paramMap = Map.of(
            "name", name,
            "modelName", modelName
        );
        jdbcTemplate.update(deleteElementsSql, paramMap);
        jdbcTemplate.update(deletePageSql, paramMap);
    }

    public void updateName(String currentName, String modelName, String newName) {
        String sql = "update page set name = :newName" +
            " where name = :currentName and model_name = :modelName";

        Map<String, Object> paramMap = Map.of(
            "currentName", currentName,
            "modelName", modelName,
            "newName", newName
        );
        jdbcTemplate.update(sql, paramMap);
    }

    /**
     * Creates a page with its metadata and elements.
     */
    public Page create(Page page) {

        // update page metadata (title)
        Map<String, Object> paramMap = Map.of(
            "name", page.getName(),
            "model_name", page.getModelName(),
            "title", page.getTitle()
        );
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update("insert into page (id, name, model_name, title) values (nextval('page_seq'), :name, :model_name, :title)", new MapSqlParameterSource(paramMap), keyHolder);
        Long id = (Long) keyHolder.getKeys().get("id");

        // visit all elements and create them
        ElementCreatorVisitor elementCreatorVisitor = new ElementCreatorVisitor(id);
        List<Element> elements = page.getElements().values().stream()
                                     .map(element -> element.accept(elementCreatorVisitor))
                                     .filter(Objects::nonNull)
                                     .collect(Collectors.toList());

        return new Page(id, page.getName(), page.getModelName(), page.getTitle(), elements);
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
    private Text createText(Long pageId, Text text) {
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
     * Creates a new {@link Select}
     */
    private Select createSelect(Long pageId, Select select) {
        Map<String, Object> paramMap = Map.of(
            "page_id", pageId,
            "type", select.getType().name(),
            "key", select.getKey(),
            "value", select.getValue()
        );
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update("insert into page_element (id, page_id, type, key, text) values (nextval('page_element_seq'), :page_id, :type, :key, :value)", new MapSqlParameterSource(paramMap), keyHolder);
        Long id = (Long) keyHolder.getKeys().get("id");
        return new Select(id, select.getKey(), select.getValue());
    }

    /**
     * Creates a new {@link Select}
     */
    private Checkbox createCheckbox(Long pageId, Checkbox checkbox) {
        Map<String, Object> paramMap = Map.of(
            "page_id", pageId,
            "type", checkbox.getType().name(),
            "key", checkbox.getKey(),
            "value", checkbox.getValue()
        );
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update("insert into page_element (id, page_id, type, key, text) values (nextval('page_element_seq'), :page_id, :type, :key, :value)", new MapSqlParameterSource(paramMap), keyHolder);
        Long id = (Long) keyHolder.getKeys().get("id");
        return new Checkbox(id, checkbox.getKey(), checkbox.getValue());
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
        Map<Long, BasicPage> basicPagesById = new HashMap<>();
        Map<Long, List<Element>> elementsById = new HashMap<>();
        while (rs.next()) {
            Long pageId = rs.getLong("id");
            String name = rs.getString("name");
            String modelName = rs.getString("model_name");
            String title = rs.getString("title");
            basicPagesById.computeIfAbsent(pageId, id -> new BasicPage(id, name, modelName, title));
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
                    case SELECT:
                        elements.add(Element.select(elementId, key, rs.getString("text")));
                        break;
                    case CHECKBOX:
                        elements.add(Element.checkbox(elementId, key, rs.getBoolean("text")));
                        break;
                    default:
                        throw new IllegalStateException("Unknown element type: " + type);
                }
            }
        }

        return basicPagesById.values()
                           .stream()
                           .sorted(Comparator.comparing(BasicPage::getId))
                           .map(basicPage -> new Page(basicPage.getId(),
                                                      basicPage.getName(),
                                                      basicPage.getModelName(),
                                                      basicPage.getTitle(),
                                                      elementsById.get(basicPage.getId())))
                           .collect(Collectors.toList());
    }

    private Optional<Page> extractPage(ResultSet rs) throws SQLException {
        List<Page> pages = extractPages(rs);
        return pages.isEmpty() ? Optional.empty() : Optional.of(pages.get(0));
    }

    private List<BasicPage> extractBasicPages(ResultSet rs) throws SQLException {
        List<BasicPage> result = new ArrayList<>();
        while (rs.next()) {
            Long pageId = rs.getLong("id");
            String name = rs.getString("name");
            String modelName = rs.getString("model_name");
            String title = rs.getString("title");
            result.add(new BasicPage(pageId, name, modelName, title));
        }

        return result;
    }

    private Optional<BasicPage> extractBasicPage(ResultSet rs) throws SQLException {
        List<BasicPage> pages = extractBasicPages(rs);
        return pages.isEmpty() ? Optional.empty() : Optional.of(pages.get(0));
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

        @Override
        public Select visitSelect(Select select) {
            return createSelect(pageId, select);
        }

        @Override
        public Checkbox visitCheckbox(Checkbox checkbox) {
            return createCheckbox(pageId, checkbox);
        }
    }
}
