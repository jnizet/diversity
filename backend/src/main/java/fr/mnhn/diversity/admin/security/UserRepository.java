package fr.mnhn.diversity.admin.security;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository for users
 * @author JB Nizet
 */
@Repository
@Transactional
public class UserRepository {
    private NamedParameterJdbcTemplate jdbcTemplate;

    public UserRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<User> findByLogin(String login) {
        String sql = "select u.id, u.login, u.hashed_password from app_user u where u.login = :login";
        List<User> list = jdbcTemplate.query(sql, Map.of("login", login), (rs, rowNum) ->
            new User(rs.getLong("id"), rs.getString("login"), rs.getString("hashed_password"))
        );
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    public boolean existsByLogin(String login) {
        String sql = "select true from app_user u where u.login = :login";
        return jdbcTemplate.query(sql, Map.of("login", login), (ResultSet rs) -> rs.next());
    }
}
