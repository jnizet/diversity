package fr.mnhn.diversity.admin.security;

import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static fr.mnhn.diversity.common.testing.RepositoryTests.DELETE_ALL;
import static fr.mnhn.diversity.common.testing.RepositoryTests.TRACKER;
import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import fr.mnhn.diversity.common.testing.RepositoryTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

/**
 * Tests for UserRepository
 * @author JB Nizet
 */
@RepositoryTest
@Import(UserRepository.class)
class UserRepositoryTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserRepository repository;

    @BeforeEach
    void prepare() {
        DbSetup dbSetup =
            new DbSetup(
                new DataSourceDestination(dataSource),
                sequenceOf(
                    DELETE_ALL,
                    insertInto("app_user")
                        .columns("id", "login", "hashed_password")
                        .values(1L, "jb", "secr3t")
                        .build()
                )
            );
        TRACKER.launchIfNecessary(dbSetup);
    }

    @Test
    void shouldFindByLogin() {
        TRACKER.skipNextLaunch();
        assertThat(repository.findByLogin("jb")).contains(
            new User(1L, "jb", "secr3t")
        );

        assertThat(repository.findByLogin("unknown")).isEmpty();
    }

    @Test
    void shouldExistsByLogin() {
        TRACKER.skipNextLaunch();
        assertThat(repository.existsByLogin("jb")).isTrue();

        assertThat(repository.existsByLogin("unknown")).isFalse();
    }
}
