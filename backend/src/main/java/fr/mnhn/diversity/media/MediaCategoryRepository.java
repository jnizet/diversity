package fr.mnhn.diversity.media;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

/**
 * The repository used to handle the categories of the indicators
 */
@Repository
public class MediaCategoryRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public MediaCategoryRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<MediaCategory> list() {
        return jdbcTemplate.query("select id, name from media_category order by name", (rs, rowNum) -> new MediaCategory(rs.getLong("id"), rs.getString("name")));
    }

    public List<MediaCategory> listUsedCategory() {
        return jdbcTemplate.query("select id, name from media_category where exists(select 1 from media_category_relation where category_id = id) order by name", (rs, rowNum) -> new MediaCategory(rs.getLong("id"), rs.getString("name")));
    }

    /**
     * Creates a new {@link MediaCategory}
     */
    public MediaCategory create(
        MediaCategory mediaCategory) {
        Map<String, Object> paramMap = Map.of(
            "name", mediaCategory.getName()
        );
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update("insert into media_category (id, name) values (nextval('category_seq'), :name)", new MapSqlParameterSource(paramMap), keyHolder);
        Long id = (Long) keyHolder.getKeys().get("id");
        return new MediaCategory(id, mediaCategory.getName());
    }

    /**
     * Updates the {@link MediaCategory}
     * @return true if the mediaCategory was updated, false if not, because the mediaCategory didn't exist yet.
     */
    public boolean update(MediaCategory mediaCategory) {
        Map<String, Object> paramMap = Map.of(
            "id", mediaCategory.getId(),
            "name", mediaCategory.getName()
        );
        int updatedRows = jdbcTemplate.update("update media_category set name = :name where id = :id", paramMap);
        return updatedRows > 0;
    }

    /**
     * Finds a {@link MediaCategory} by its ID
     */
    public Optional<MediaCategory> findById(Long id) {
        String sql = "select media_category.id, media_category.name from media_category where media_category.id = :id";
        List<MediaCategory> list = jdbcTemplate.query(sql, Map.of("id", id), (rs, rowNum) ->
            new MediaCategory(rs.getLong("id"), rs.getString("name"))
        );
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    /**
     * Finds a {@link MediaCategory} by its name
     */
    public Optional<MediaCategory> findByName(String name) {
        String sql = "select media_category.id, media_category.name from media_category where media_category.name = :name";
        List<MediaCategory> list = jdbcTemplate.query(sql, Map.of("name", name), (rs, rowNum) ->
            new MediaCategory(rs.getLong("id"), rs.getString("name"))
        );
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    /**
     * Finds  {@link MediaCategory}s by associate to a media page
     */
    public List<MediaCategory> findByPageId(Long pageId) {
        String sql = "select media_category.id, media_category.name from media_category "
                + "where exists (select 1 from media_category_relation where media_page_id = :pageId and category_id = media_category.id)";
        return jdbcTemplate.query(sql, Map.of("pageId", pageId), (rs, rowNum) -> new MediaCategory(rs.getLong("id"), rs.getString("name")));
    }

    /**
     * Deletes a {@link MediaCategory}
     */
    public void delete(MediaCategory mediaCategory) {
        String sql = "delete from media_category where media_category.id = :id";
        jdbcTemplate.update(sql, Map.of("id", mediaCategory.getId()));
    }
}
