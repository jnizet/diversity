package fr.mnhn.diversity.e2e;

import java.util.Collections;

import fr.mnhn.diversity.repository.Territory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * A component only used when the app is started with the e2e profile, and which populates the e2e database
 * with known data for e2e tests
 * @author JB Nizet
 */
@Component
@Transactional
@Profile("e2e")
public class E2eDatabaseSetup implements CommandLineRunner {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public E2eDatabaseSetup(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        jdbcTemplate.update("delete from territory", Collections.emptyMap());

        jdbcTemplate.batchUpdate(
            "insert into territory (id, name) values (:id, :name)",
            new SqlParameterSource[] {
                new BeanPropertySqlParameterSource(new Territory(1L, "Guadeloupe")),
                new BeanPropertySqlParameterSource(new Territory(2L, "Guyane")),
                new BeanPropertySqlParameterSource(new Territory(3L, "Martinique")),
                new BeanPropertySqlParameterSource(new Territory(4L, "Réunion"))
            });
    }
}
