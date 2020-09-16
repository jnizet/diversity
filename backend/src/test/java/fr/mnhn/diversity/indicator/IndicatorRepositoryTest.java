package fr.mnhn.diversity.indicator;

import static com.ninja_squad.dbsetup.Operations.*;
import static fr.mnhn.diversity.common.testing.Tracker.TRACKER;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import fr.mnhn.diversity.common.testing.RepositoryTest;
import fr.mnhn.diversity.territory.Territory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@RepositoryTest
@Import(IndicatorRepository.class)
class IndicatorRepositoryTest {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private IndicatorRepository repository;

    @BeforeEach
    void prepare() {

        DbSetup dbSetup =
                new DbSetup(
                        new DataSourceDestination(dataSource),
                        sequenceOf(
                                deleteAllFrom("indicator_value", "indicator_category", "indicator", "category"),
                                insertInto("category")
                                        .columns("id", "name")
                                        .values(101L, "category1")
                                        .values(102L, "category2")
                                        .build(),
                                insertInto("indicator")
                                        .columns("id", "biom_id")
                                        .values(1L, "indicator2")
                                        .values(2L, "indicator1")
                                        .build(),
                                insertInto("indicator_category")
                                        .columns("indicator_id", "category_id")
                                        .values(2L, 102L)
                                        .values(2L, 101L)
                                        .values(1L, 102L)
                                        .build()
                        )
                );
        TRACKER.launchIfNecessary(dbSetup);
    }

    @Test
    void shouldListIndicators() {
        TRACKER.skipNextLaunch();
        IndicatorCategory category1 = new IndicatorCategory(101L, "category1");
        IndicatorCategory category2 = new IndicatorCategory(102L, "category2");
        assertThat(repository.list()).containsExactly(
                new Indicator(2L, "indicator1", List.of(category1, category2)),
                new Indicator(1L, "indicator2", List.of(category2))
        );
    }

    @Test
    void shouldSaveAndGetValues() {
        Indicator indicator = new Indicator(1L, "indicator2", List.of());
        IndicatorValue valueOutreMer = new IndicatorValue(278.9, "km");
        IndicatorValue valueReunion = new IndicatorValue(12.4, "km");
        IndicatorValue valueGuadeloupe = new IndicatorValue(3.8, "km");

        repository.saveValue(indicator, Territory.OUTRE_MER, valueOutreMer);
        repository.saveValue(indicator, Territory.REUNION, valueReunion);
        repository.saveValue(indicator, Territory.GUADELOUPE, valueGuadeloupe);

        assertThat(repository.getValues(indicator).getValues()).isEqualTo(
                Map.of(
                        Territory.OUTRE_MER, valueOutreMer,
                        Territory.REUNION, valueReunion,
                        Territory.GUADELOUPE, valueGuadeloupe
                )
        );
    }
}
