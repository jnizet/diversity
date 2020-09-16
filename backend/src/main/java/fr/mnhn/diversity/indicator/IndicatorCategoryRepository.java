package fr.mnhn.diversity.indicator;

import java.util.List;
import java.util.Map;

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
     * Saves a new {@link IndicatorCategory}
     */
    public void save(IndicatorCategory category) {
        Map<String, Object> paramMap = Map.of(
                "name", category.getName()
        );
        jdbcTemplate.update("insert into category (id, name) values (nextval('category_seq'), :name)", paramMap);
    }
}
