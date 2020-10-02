package fr.mnhn.diversity.ecogesture;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

/**
 * Repository for ecogestures
 * @author JB Nizet
 */
@Repository
public class EcogestureRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public EcogestureRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static Ecogesture extractRow(ResultSet rs, int rowNum) throws SQLException {
        return new Ecogesture(rs.getLong("id"), rs.getString("slug"));
    }

    /**
     * Lists all {@link Ecogesture ecogestures}, ordered by their slug
     */
    public List<Ecogesture> list() {
        String query = "select eg.id, eg.slug from ecogesture eg order by eg.slug";
        return jdbcTemplate.query(query, EcogestureRepository::extractRow);
    }

    /**
     * Finds an {@link Ecogesture} by its ID
     */
    public Optional<Ecogesture> findById(Long id) {
        String query = "select eg.id, eg.slug from ecogesture eg where eg.id = :id";
        List<Ecogesture> ecogestures =
            jdbcTemplate.query(query, Map.of("id", id), EcogestureRepository::extractRow);
        return ecogestures.isEmpty() ? Optional.empty() : Optional.of(ecogestures.get(0));
    }

    /**
     * Finds an {@link Ecogesture} by its slug
     */
    public Optional<Ecogesture> findBySlug(String slug) {
        String query = "select eg.id, eg.slug from ecogesture eg where eg.slug = :slug";
        List<Ecogesture> ecogestures =
            jdbcTemplate.query(query, Map.of("slug", slug), EcogestureRepository::extractRow);
        return ecogestures.isEmpty() ? Optional.empty() : Optional.of(ecogestures.get(0));
    }

    /**
     * Creates an {@link Ecogesture}
     */
    public Ecogesture create(Ecogesture ecogesture) {
        Map<String, Object> paramMap = Map.of(
            "slug", ecogesture.getSlug()
        );
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update("insert into ecogesture (id, slug) values (nextval('ecogesture_seq'), :slug)", new MapSqlParameterSource(paramMap), keyHolder);
        Long id = (Long) keyHolder.getKeys().get("id");
        return new Ecogesture(id, ecogesture.getSlug());
    }

    /**
     * Updates the {@link Ecogesture}
     * @return true if the ecogesture was updated, false if not, because the ecogesture didn't exist yet.
     */
    public boolean update(Ecogesture ecogesture) {
        Map<String, Object> paramMap = Map.of(
            "id", ecogesture.getId(),
            "slug", ecogesture.getSlug()
        );
        int updatedRows = jdbcTemplate.update("update ecogesture set slug = :slug where id = :id", paramMap);
        return updatedRows > 0;
    }

    public void delete(Ecogesture ecogesture) {
        String sql = "delete from ecogesture where ecogesture.id = :id";
        jdbcTemplate.update(sql, Map.of("id", ecogesture.getId()));
    }
}
