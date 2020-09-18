package fr.mnhn.diversity.ecogesture;

import static com.ninja_squad.dbsetup.Operations.*;
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
 * Tests for {@link EcogestureRepository}
 * @author JB Nizet
 */
@RepositoryTest
@Import(EcogestureRepository.class)
class EcogestureRepositoryTest {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private EcogestureRepository repository;

    @BeforeEach
    void prepare() {

        DbSetup dbSetup =
            new DbSetup(
                new DataSourceDestination(dataSource),
                sequenceOf(
                    DELETE_ALL,
                    insertInto("ecogesture")
                        .columns("id", "slug")
                        .values(1L, "slug2")
                        .values(2L, "slug1")
                        .values(3L, "slug3")
                        .build(),
                    insertInto("indicator")
                        .columns("id", "biom_id", "slug")
                        .values(1L, "i1", "especes-envahissantes")
                        .build(),
                    insertInto("indicator_ecogesture")
                        .columns("indicator_id", "ecogesture_id")
                        .values(1L, 1L)
                        .values(1L, 3L)
                        .build()
                    )
            );
        TRACKER.launchIfNecessary(dbSetup);
    }

    @Test
    void shouldList() {
        TRACKER.skipNextLaunch();
        assertThat(repository.list()).containsExactly(
            new Ecogesture(2L, "slug1"),
            new Ecogesture(1L, "slug2"),
            new Ecogesture(3L, "slug3")
        );
    }

    @Test
    void shouldFindBySlug() {
        TRACKER.skipNextLaunch();
        assertThat(repository.findBySlug("slug1")).contains(
            new Ecogesture(2L, "slug1")
        );
        assertThat(repository.findBySlug("unknown")).isEmpty();
    }

    @Test
    void shouldFindByIndicator() {
        TRACKER.skipNextLaunch();
        assertThat(repository.findByIndicator(1L)).containsExactly(
            new Ecogesture(1L, "slug2"),
            new Ecogesture(3L, "slug3")
        );
        assertThat(repository.findByIndicator(3456L)).isEmpty();
    }
}
