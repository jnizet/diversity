package fr.mnhn.diversity.indicator;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * The repository used to handle the categories of the indicators
 */
@Repository
public class IndicatorCategoryRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public IndicatorCategoryRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<IndicatorCategory> list() {
        return jdbcTemplate.query("select id, name from category order by name", (rs, rowNum) -> new IndicatorCategory(rs.getLong("id"), rs.getString("name")));
    }

    /**
     * Creates a new {@link IndicatorCategory}
     */
    public void create(IndicatorCategory category) {
        Map<String, Object> paramMap = Map.of(
            "name", category.getName()
        );
        jdbcTemplate.update("insert into category (id, name) values (nextval('category_seq'), :name)", paramMap);
    }

    /**
     * Updates the {@link IndicatorCategory}
     * @return true if the category was updated, false if not, because the category didn't exist yet.
     */
    public boolean update(IndicatorCategory indicatorCategory) {
        Map<String, Object> paramMap = Map.of(
            "id", indicatorCategory.getId(),
            "name", indicatorCategory.getName()
        );
        int updatedRows = jdbcTemplate.update("update category set name = :name where id = :id", paramMap);
        return updatedRows > 0;
    }

    /**
     * Finds a {@link IndicatorCategory} by its ID
     */
    public Optional<IndicatorCategory> findById(Long id) {
        String sql = "select category.id, category.name from category where category.id = :id";
        List<IndicatorCategory> list = jdbcTemplate.query(sql, Map.of("id", id), (rs, rowNum) ->
            new IndicatorCategory(rs.getLong("id"), rs.getString("name"))
        );
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    /**
     * Finds a {@link IndicatorCategory} by its name
     */
    public Optional<IndicatorCategory> findByName(String name) {
        String sql = "select category.id, category.name from category where category.name = :name";
        List<IndicatorCategory> list = jdbcTemplate.query(sql, Map.of("name", name), (rs, rowNum) ->
            new IndicatorCategory(rs.getLong("id"), rs.getString("name"))
        );
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    /**
     * Deletes a {@link IndicatorCategory}
     */
    public void delete(IndicatorCategory indicatorCategory) {
        String sql = "delete from category where category.id = :id";
        jdbcTemplate.update(sql, Map.of("id", indicatorCategory.getId()));
    }
}
