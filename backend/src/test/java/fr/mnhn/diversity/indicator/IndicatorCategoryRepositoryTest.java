package fr.mnhn.diversity.indicator;

import static com.ninja_squad.dbsetup.Operations.*;
import static fr.mnhn.diversity.common.testing.Tracker.TRACKER;
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
                                deleteAllFrom("indicator_category", "category"),
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
        repository.save(new IndicatorCategory(null, "category3"));

        List<String> categoryNames = repository.list().stream().map(IndicatorCategory::getName).collect(Collectors.toList());
        assertThat(categoryNames).containsExactly(
                "category1",
                "category2",
                "category3"
        );
    }
}
