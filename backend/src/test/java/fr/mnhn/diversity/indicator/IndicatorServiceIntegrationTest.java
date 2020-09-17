package fr.mnhn.diversity.indicator;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import fr.mnhn.diversity.territory.Territory;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * A test which really connects to the indicators API
 * @author JB Nizet
 */
@RestClientTest
@Import({IndicatorConfig.class, IndicatorService.class})
public class IndicatorServiceIntegrationTest {
    @Autowired
    @Indicators
    private WebClient webClient;

    @Autowired
    private IndicatorService service;

    @Disabled("we don't want the tests to fail if the API is unavailable or if we don't have WIFI." +
        " But it's nice to have this test during development to check that everything is OK.")
    @Test
    void shouldGetIndicatorAndValue() {
        IndicatorData indicatorData = service.indicatorData("b7078fc3-bd3f-44c0-b3d0-7db78b9fbcc6").block();
        Map<Territory, IndicatorValue> indicatorValues = service.indicatorValues(indicatorData.getCalculationReference()).block();

        assertThat(indicatorData.getShortLabel()).isEqualTo("Évolution du taux de boisement dans les Outre-Mer");
        assertThat(indicatorValues.get(Territory.OUTRE_MER).getValue()).isBetween(0.0, 100.0);
    }
}
