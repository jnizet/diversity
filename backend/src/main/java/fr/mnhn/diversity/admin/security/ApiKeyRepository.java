package fr.mnhn.diversity.admin.security;

import java.sql.ResultSet;
import java.util.Map;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ApiKeyRepository {
    private NamedParameterJdbcTemplate jdbcTemplate;

    public ApiKeyRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean existsByHostAndKey(String host, String key) {
        String sql = "select true from api_key k where k.host = :host and k.key = :key";
        return jdbcTemplate.query(sql, Map.of("host", host, "key", key), (ResultSet rs) -> rs.next());
    }
}
