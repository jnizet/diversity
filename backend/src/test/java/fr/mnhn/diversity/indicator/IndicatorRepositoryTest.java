package fr.mnhn.diversity.indicator;

import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static fr.mnhn.diversity.common.testing.RepositoryTests.DELETE_ALL;
import static fr.mnhn.diversity.common.testing.RepositoryTests.TRACKER;
import static fr.mnhn.diversity.territory.Territory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.sql.DataSource;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.generator.ValueGenerators;
import fr.mnhn.diversity.common.testing.RepositoryTest;
import fr.mnhn.diversity.ecogesture.Ecogesture;
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

    Ecogesture ecogesture1 = new Ecogesture(301L, "ecogesture-1");
    Ecogesture ecogesture2 = new Ecogesture(302L, "ecogesture-2");

    Indicator indicator1;
    Indicator indicator2;
    Indicator indicator3;

    @BeforeEach
    void prepare() {
        indicator1 = new Indicator(2L, "indicator1", "slug1", false, 2,
            List.of(category1, category2), List.of(ecogesture1, ecogesture2));
        indicator2 = new Indicator(1L, "indicator2", "slug2", false, 1, List.of(category2), List.of(ecogesture2));
        indicator3 = new Indicator(3L, "indicator3", "slug3", false, 3, List.of(), List.of());

        DbSetup dbSetup =
                new DbSetup(
                        new DataSourceDestination(dataSource),
                        sequenceOf(
                                DELETE_ALL,
                                insertInto("category")
                                        .columns("id", "name")
                                        .values(category1.getId(), category1.getName())
                                        .values(category2.getId(), category2.getName())
                                        .build(),
                                insertInto("ecogesture")
                                    .columns("id", "slug")
                                    .values(ecogesture1.getId(), ecogesture1.getSlug())
                                    .values(ecogesture2.getId(), ecogesture2.getSlug())
                                    .build(),
                                insertInto("indicator")
                                        .columns("id", "biom_id", "slug", "is_rounded", "rank")
                                        .values(1L, "indicator2", "slug2", false, 1)
                                        .values(2L, "indicator1", "slug1", false, 2)
                                        .values(3L, "indicator3", "slug3", false, 3)
                                        .build(),
                                insertInto("indicator_ecogesture")
                                    .columns("indicator_id", "ecogesture_id")
                                    .values(2L, ecogesture2.getId())
                                    .values(2L, ecogesture1.getId())
                                    .values(1L, ecogesture2.getId())
                                    .build(),
                                insertInto("indicator_category")
                                        .columns("indicator_id", "category_id")
                                        .values(2L, category2.getId())
                                        .values(2L, category1.getId())
                                        .values(1L, category2.getId())
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
            indicator1,
            indicator2,
            indicator3
        );
    }

    @Test
    void shouldFindBySlug() {
        TRACKER.skipNextLaunch();
        assertThat(repository.findBySlug("slug1")).contains(indicator1);
        assertThat(repository.findBySlug("unknown")).isEmpty();
    }

    @Test
    void shouldFindById() {
        TRACKER.skipNextLaunch();
        assertThat(repository.findById(2L)).contains(indicator1);
        assertThat(repository.findById(423L)).isEmpty();
    }

    @Test
    void shouldFindByBiomId() {
        TRACKER.skipNextLaunch();
        assertThat(repository.findByBiomId("indicator1")).contains(indicator1);
        assertThat(repository.findByBiomId("other")).isEmpty();
    }

    @Test
    void shouldCreateIndicator() {
        Indicator indicator = new Indicator( "indicator5", "slug5", false, List.of(category1, category2), List.of(ecogesture1, ecogesture2));

        Indicator createdIndicator = repository.create(indicator);

        assertThat(createdIndicator.getId()).isNotNull();

        assertThat(repository.findById(createdIndicator.getId())).contains(
            new Indicator(createdIndicator.getId(), "indicator5", "slug5", false, createdIndicator.getRank(),
                List.of(category1, category2), List.of(ecogesture1, ecogesture2))
        );
    }

    @Test
    void shouldDeleteIndicator() {
        repository.delete(indicator1);

        assertThat(repository.findById(2L)).isEmpty();
        assertThat(repository.getValues(indicator1)).hasSize(0);
    }

    @Test
    void shouldUpdateIndicator() {
        Indicator indicator = new Indicator(2L, "indicator21", "slug21", false, 1, List.of(category1), List.of(ecogesture1));

        Indicator updatedIndicator = repository.update(indicator);

        assertThat(repository.findById(updatedIndicator.getId())).contains(
            new Indicator(updatedIndicator.getId(), "indicator21", "slug21", false, 1, List.of(category1), List.of(ecogesture1))
        );
    }

    @Test
    void shouldInsertAndGetValues() {
        Indicator indicator = new Indicator(1L, "indicator2", "slug2", false, 1);
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
        assertThat(repository.getValues(indicator3)).hasSize(4);

        repository.deleteValues(indicator3, EnumSet.of(REUNION, SAINT_PIERRE_ET_MIQUELON));
        assertThat(repository.getValues(indicator3)).hasSize(2);
    }

    @Test
    void shouldUpdateValue() {
        IndicatorValue newValue = new IndicatorValue(50, "patates");
        assertThat(repository.updateValue(indicator3, OUTRE_MER, newValue)).isTrue();

        assertThat(repository.getValues(indicator3).get(OUTRE_MER)).isEqualTo(newValue);

        Indicator indicator = new Indicator(1L, "indicator2", "slug2", false , 1);
        assertThat(repository.updateValue(indicator, OUTRE_MER, newValue)).isFalse();
    }

    @Test
    void shouldGetValuesForIndicatorsAndTerritory() {
        TRACKER.skipNextLaunch();
        Indicator indicator2 = new Indicator(1L, "indicator2", "slug2", false ,1);
        Indicator indicator3 = new Indicator(3L, "indicator3", "slug3", false, 1);
        Set<Indicator> indicators = Set.of(indicator3, indicator2);

        Map<Indicator, IndicatorValue> result = repository.getValuesForIndicatorsAndTerritory(indicators, OUTRE_MER);
        assertThat(result).containsOnly(
            entry(indicator3, new IndicatorValue(10, "%"))
        );
    }

    @Test
    void shouldGetValuesForIndicatorSlugsAndTerritory() {
        TRACKER.skipNextLaunch();
        Set<String> slugs = Set.of("slug2", "slug3");
        Map<String, IndicatorValue> result = repository.getValuesForIndicatorSlugsAndTerritory(slugs, OUTRE_MER);
        assertThat(result).containsOnly(
            entry("slug3", new IndicatorValue(10, "%"))
        );
    }
    @Test
    void shouldFindIndicatorsReferencingAnEcogesture() {
        TRACKER.skipNextLaunch();
        List<Long> indicatorsForEcogesture1 = repository.findIndicatorsForEcogesture(ecogesture1.getSlug())
            .stream().map(Indicator::getId).collect(Collectors.toList());
        assertThat(indicatorsForEcogesture1).containsExactly(indicator1.getId());

        List<Long> indicatorsForEcogesture2 = repository.findIndicatorsForEcogesture(ecogesture2.getSlug())
                                                        .stream().map(Indicator::getId).collect(Collectors.toList());
        assertThat(indicatorsForEcogesture2).containsExactly(indicator1.getId(), indicator2.getId());

        List<Long> indicatorsForEcogesture3 = repository.findIndicatorsForEcogesture("ecogesture-3")
                                                        .stream().map(Indicator::getId).collect(Collectors.toList());
        assertThat(indicatorsForEcogesture3).isEmpty();
    }
}
