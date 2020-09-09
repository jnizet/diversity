package fr.mnhn.diversity.territory;

import static com.ninja_squad.dbsetup.Operations.*;
import static fr.mnhn.diversity.common.testing.Tracker.TRACKER;
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
 * Tests for {@link TerritoryRepository}
 * @author JB Nizet
 */
@RepositoryTest
@Import(TerritoryRepository.class)
class TerritoryRepositoryTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private TerritoryRepository repository;

    @BeforeEach
    void prepare() {

        DbSetup dbSetup =
            new DbSetup(
                new DataSourceDestination(dataSource),
                sequenceOf(
                    deleteAllFrom("territory"),
                    insertInto("territory")
                        .columns("id", "name")
                        .values(1L, "Réunion")
                        .values(2L, "Guadeloupe")
                        .build()
                )
            );
        TRACKER.launchIfNecessary(dbSetup);
    }

    @Test
    void shouldListTerritories() {
        TRACKER.skipNextLaunch();
        assertThat(repository.list()).containsExactly(
            new Territory(2L, "Guadeloupe"),
            new Territory(1L, "Réunion")
        );
    }
}
