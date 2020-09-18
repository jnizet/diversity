package fr.mnhn.diversity.indicator;

import static com.ninja_squad.dbsetup.Operations.*;
import static fr.mnhn.diversity.common.testing.Tracker.TRACKER;
import static fr.mnhn.diversity.territory.Territory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.sql.DataSource;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.generator.ValueGenerators;
import fr.mnhn.diversity.common.testing.RepositoryTest;
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
                                        .values(3L, "indicator3")
                                        .build(),
                                insertInto("indicator_category")
                                        .columns("indicator_id", "category_id")
                                        .values(2L, 102L)
                                        .values(2L, 101L)
                                        .values(1L, 102L)
                                        .build(),
                                insertInto("indicator_value")
                                        .withGeneratedValue("id", ValueGenerators.sequence())
                                        .columns("indicator_id", "territory", "value", "unit")
                                        .values(3L, OUTRE_MER, 10, "%")
                                        .values(3L, REUNION, 11, "%")
                                        .values(3L, GUADELOUPE, 12, "%")
                                        .values(3L, SAINT_PIERRE_ET_MIQUELON, 13, "%")
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
                new Indicator(1L, "indicator2", List.of(category2)),
                new Indicator(3L, "indicator3", List.of())
        );
    }

    @Test
    void shouldInsertAndGetValues() {
        Indicator indicator = new Indicator(1L, "indicator2");
        IndicatorValue valueOutreMer = new IndicatorValue(278.9, "km");
        IndicatorValue valueReunion = new IndicatorValue(12.4, "km");
        IndicatorValue valueGuadeloupe = new IndicatorValue(3.8, "km");

        repository.insertValue(indicator, OUTRE_MER, valueOutreMer);
        repository.insertValue(indicator, REUNION, valueReunion);
        repository.insertValue(indicator, GUADELOUPE, valueGuadeloupe);

        assertThat(repository.getValues(indicator).getValues()).isEqualTo(
                Map.of(
                        OUTRE_MER, valueOutreMer,
                        REUNION, valueReunion,
                        GUADELOUPE, valueGuadeloupe
                )
        );
    }

    @Test
    void shouldDeleteValues() {
        Indicator indicator = new Indicator(3L, "indicator3");
        assertThat(repository.getValues(indicator).getValues()).hasSize(4);

        repository.deleteValues(indicator, EnumSet.of(REUNION, SAINT_PIERRE_ET_MIQUELON));
        assertThat(repository.getValues(indicator).getValues()).hasSize(2);
    }

    @Test
    void shouldUpdateValue() {
        Indicator indicator = new Indicator(3L, "indicator3");
        IndicatorValue newValue = new IndicatorValue(50, "patates");
        assertThat(repository.updateValue(indicator, OUTRE_MER, newValue)).isTrue();

        assertThat(repository.getValues(indicator).getValues().get(OUTRE_MER)).isEqualTo(newValue);

        indicator = new Indicator(1L, "indicator2");
        assertThat(repository.updateValue(indicator, OUTRE_MER, newValue)).isFalse();
    }

    @Test
    void shouldGetValuesForIndicatorsAndTerritory() {
        TRACKER.skipNextLaunch();
        Indicator indicator2 = new Indicator(1L, "indicator2");
        Indicator indicator3 = new Indicator(3L, "indicator3");
        Set<Indicator> indicators = Set.of(indicator3, indicator2);

        Map<Indicator, IndicatorValue> result = repository.getValuesForIndicatorsAndTerritory(indicators, OUTRE_MER);
        assertThat(result).containsOnly(
            entry(indicator3, new IndicatorValue(10, "%"))
        );
    }
}
