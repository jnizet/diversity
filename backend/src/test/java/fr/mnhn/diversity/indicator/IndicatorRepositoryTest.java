package fr.mnhn.diversity.indicator;

import static com.ninja_squad.dbsetup.Operations.*;
import static fr.mnhn.diversity.common.testing.Tracker.TRACKER;
import static org.assertj.core.api.Assertions.assertThat;

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
                                deleteAllFrom("indicator_value"),
                                deleteAllFrom("indicator"),
                                insertInto("indicator")
                                        .columns("id", "biom_id")
                                        .values(1L, "indicator2")
                                        .values(2L, "indicator1")
                                        .build()
                        )
                );
        TRACKER.launchIfNecessary(dbSetup);
    }

    @Test
    void shouldListIndicators() {
        TRACKER.skipNextLaunch();
        assertThat(repository.list()).containsExactly(
                new Indicator(2L, "indicator1"),
                new Indicator(1L, "indicator2")
        );
    }

    @Test
    void shouldSaveAndGetValues() {
        TRACKER.skipNextLaunch();
        Indicator indicator = new Indicator(1L, "indicator2");
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
