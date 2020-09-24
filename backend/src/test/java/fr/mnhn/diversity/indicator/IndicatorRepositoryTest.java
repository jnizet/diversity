package fr.mnhn.diversity.indicator;

import static com.ninja_squad.dbsetup.Operations.*;
import static fr.mnhn.diversity.common.testing.RepositoryTests.DELETE_ALL;
import static fr.mnhn.diversity.common.testing.RepositoryTests.TRACKER;
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

    IndicatorCategory category1 = new IndicatorCategory(101L, "category1");
    IndicatorCategory category2 = new IndicatorCategory(102L, "category2");

    @BeforeEach
    void prepare() {

        DbSetup dbSetup =
                new DbSetup(
                        new DataSourceDestination(dataSource),
                        sequenceOf(
                                DELETE_ALL,
                                insertInto("category")
                                        .columns("id", "name")
                                        .values(101L, "category1")
                                        .values(102L, "category2")
                                        .build(),
                                insertInto("indicator")
                                        .columns("id", "biom_id", "slug")
                                        .values(1L, "indicator2", "slug2")
                                        .values(2L, "indicator1", "slug1")
                                        .values(3L, "indicator3", "slug3")
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
        assertThat(repository.list()).containsExactly(
                new Indicator(2L, "indicator1", "slug1", List.of(category1, category2)),
                new Indicator(1L, "indicator2", "slug2", List.of(category2)),
                new Indicator(3L, "indicator3", "slug3", List.of())
        );
    }

    @Test
    void shouldFindBySlug() {
        TRACKER.skipNextLaunch();
        assertThat(repository.findBySlug("slug1")).contains(
            new Indicator(2L, "indicator1", "slug1", List.of(category1, category2))
        );
        assertThat(repository.findBySlug("unknown")).isEmpty();
    }

    @Test
    void shouldFindById() {
        TRACKER.skipNextLaunch();
        assertThat(repository.findById(2L)).contains(
            new Indicator(2L, "indicator1", "slug1", List.of(category1, category2))
        );
        assertThat(repository.findById(423L)).isEmpty();
    }

    @Test
    void shouldFindByBiomId() {
        TRACKER.skipNextLaunch();
        assertThat(repository.findByBiomId("indicator1")).contains(
            new Indicator(2L, "indicator1", "slug1", List.of(category1, category2))
        );
        assertThat(repository.findByBiomId("other")).isEmpty();
    }

    @Test
    void shouldCreateIndicator() {
        Indicator indicator = new Indicator( "indicator5", "slug5", List.of(category1, category2));

        Indicator createdIndicator = repository.create(indicator);

        assertThat(createdIndicator.getId()).isNotNull();

        assertThat(repository.findById(createdIndicator.getId())).contains(
            new Indicator(createdIndicator.getId(), "indicator5", "slug5", List.of(category1, category2))
        );
    }

    @Test
    void shouldDeleteIndicator() {
        Indicator indicator = new Indicator(2L, "indicator2", "slug2", List.of(category1));

        repository.delete(indicator);

        assertThat(repository.findById(2L)).isEmpty();
        assertThat(repository.getValues(indicator)).hasSize(0);
    }

    @Test
    void shouldUpdateIndicator() {
        Indicator indicator = new Indicator(2L, "indicator21", "slug21", List.of(category1));

        Indicator updatedIndicator = repository.update(indicator);

        assertThat(repository.findById(updatedIndicator.getId())).contains(
            new Indicator(updatedIndicator.getId(), "indicator21", "slug21", List.of(category1))
        );
    }

    @Test
    void shouldInsertAndGetValues() {
        Indicator indicator = new Indicator(1L, "indicator2", "slug2");
        IndicatorValue valueOutreMer = new IndicatorValue(278.9, "km");
        IndicatorValue valueReunion = new IndicatorValue(12.4, "km");
        IndicatorValue valueGuadeloupe = new IndicatorValue(3.8, "km");

        repository.insertValue(indicator, OUTRE_MER, valueOutreMer);
        repository.insertValue(indicator, REUNION, valueReunion);
        repository.insertValue(indicator, GUADELOUPE, valueGuadeloupe);

        assertThat(repository.getValues(indicator)).isEqualTo(
                Map.of(
                        OUTRE_MER, valueOutreMer,
                        REUNION, valueReunion,
                        GUADELOUPE, valueGuadeloupe
                )
        );
    }

    @Test
    void shouldDeleteValues() {
        Indicator indicator = new Indicator(3L, "indicator3", "slug3");
        assertThat(repository.getValues(indicator)).hasSize(4);

        repository.deleteValues(indicator, EnumSet.of(REUNION, SAINT_PIERRE_ET_MIQUELON));
        assertThat(repository.getValues(indicator)).hasSize(2);
    }

    @Test
    void shouldUpdateValue() {
        Indicator indicator = new Indicator(3L, "indicator3", "slug3");
        IndicatorValue newValue = new IndicatorValue(50, "patates");
        assertThat(repository.updateValue(indicator, OUTRE_MER, newValue)).isTrue();

        assertThat(repository.getValues(indicator).get(OUTRE_MER)).isEqualTo(newValue);

        indicator = new Indicator(1L, "indicator2", "slug2");
        assertThat(repository.updateValue(indicator, OUTRE_MER, newValue)).isFalse();
    }

    @Test
    void shouldGetValuesForIndicatorsAndTerritory() {
        TRACKER.skipNextLaunch();
        Indicator indicator2 = new Indicator(1L, "indicator2", "slug2");
        Indicator indicator3 = new Indicator(3L, "indicator3", "slug3");
        Set<Indicator> indicators = Set.of(indicator3, indicator2);

        Map<Indicator, IndicatorValue> result = repository.getValuesForIndicatorsAndTerritory(indicators, OUTRE_MER);
        assertThat(result).containsOnly(
            entry(indicator3, new IndicatorValue(10, "%"))
        );
    }
}
