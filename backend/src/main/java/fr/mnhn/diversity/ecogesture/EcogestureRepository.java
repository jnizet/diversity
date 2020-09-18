package fr.mnhn.diversity.ecogesture;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
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

    public List<Ecogesture> list() {
        String query = "select eg.id, eg.slug from ecogesture eg order by eg.slug";
        return jdbcTemplate.query(query, (rs, rowNum) -> new Ecogesture(rs.getLong("id"), rs.getString("slug")));
    }

    public Optional<Ecogesture> findBySlug(String slug) {
        String query = "select eg.id, eg.slug from ecogesture eg where eg.slug = :slug";
        List<Ecogesture> ecogestures =
            jdbcTemplate.query(query, Map.of("slug", slug), (rs, rowNum) -> new Ecogesture(rs.getLong("id"), rs.getString("slug")));
        return ecogestures.isEmpty() ? Optional.empty() : Optional.of(ecogestures.get(0));
    }

    public List<Ecogesture> findByIndicator(Long indicatorId) {
        String query = "select eg.id, eg.slug from ecogesture eg" +
            " inner join indicator_ecogesture ie on ie.ecogesture_id = eg.id" +
            " where ie.indicator_id = :indicatorId" +
            " order by eg.slug";
        return jdbcTemplate.query(query,
                                  Map.of("indicatorId", indicatorId),
                                  (rs, rowNum) -> new Ecogesture(rs.getLong("id"), rs.getString("slug")));
    }
}
