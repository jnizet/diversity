package fr.mnhn.diversity.indicator;

import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static fr.mnhn.diversity.common.testing.RepositoryTests.DELETE_ALL;
import static fr.mnhn.diversity.common.testing.RepositoryTests.TRACKER;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import javax.sql.DataSource;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import fr.mnhn.diversity.common.testing.RepositoryTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@RepositoryTest
@Import(IndicatorCategoryRepository.class)
class IndicatorCategoryRepositoryTest {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private IndicatorCategoryRepository repository;

    @BeforeEach
    void prepare() {
        DbSetup dbSetup =
            new DbSetup(
                new DataSourceDestination(dataSource),
                sequenceOf(
                    DELETE_ALL,
                    insertInto("category")
                        .columns("id", "name")
                        .values(1L, "category2")
                        .values(2L, "category1")
                        .build()
                )
            );
        TRACKER.launchIfNecessary(dbSetup);
    }

    @Test
    void shouldListCategories() {
        TRACKER.skipNextLaunch();
        assertThat(repository.list()).containsExactly(
            new IndicatorCategory(2L, "category1"),
            new IndicatorCategory(1L, "category2")
        );
    }

    @Test
    void shouldSave() {
        IndicatorCategory category3 = repository.create(new IndicatorCategory(null, "category3"));
        assertThat(category3.getId()).isNotNull();

        List<String> categoryNames = repository.list().stream().map(IndicatorCategory::getName).collect(Collectors.toList());
        assertThat(categoryNames).containsExactly(
            "category1",
            "category2",
            "category3"
        );
    }

    @Test
    void shouldUpdate() {
        assertThat(repository.update(new IndicatorCategory(1L, "category3"))).isEqualTo(true);

        List<String> categoryNames = repository.list().stream().map(IndicatorCategory::getName).collect(Collectors.toList());
        assertThat(categoryNames).containsExactly(
            "category1",
            "category3"
        );

        assertThat(repository.update(new IndicatorCategory(5L, "category3"))).isEqualTo(false);
    }

    @Test
    void shouldFindById() {
        TRACKER.skipNextLaunch();
        assertThat(repository.findById(3456789L)).isEmpty();
        assertThat(repository.findById(1L)).contains(
            new IndicatorCategory(1L, "category2")
        );
    }

    @Test
    void shouldFindByName() {
        TRACKER.skipNextLaunch();
        assertThat(repository.findByName("something")).isEmpty();
        assertThat(repository.findByName("category1")).contains(
            new IndicatorCategory(2L, "category1")
        );
    }

    @Test
    void shouldDelete() {
        assertThat(repository.findByName("category1")).contains(
            new IndicatorCategory(2L, "category1")
        );
        repository.delete(new IndicatorCategory(2L, "category1"));
        assertThat(repository.findByName("category1")).isEmpty();
    }
}
