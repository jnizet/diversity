package fr.mnhn.diversity.territory;

import java.util.List;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * The repository used to handle territories
 * @author JB Nizet
 */
@Repository
public class TerritoryRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public TerritoryRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Territory> list() {
        return jdbcTemplate.query("select id, name from territory order by name", (rs, rowNum) -> {
            return new Territory(rs.getLong("id"), rs.getString("name"));
        });
    }
}
