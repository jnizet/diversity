package fr.mnhn.diversity.indicator;

import static org.assertj.core.api.Assertions.assertThat;

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
        IndicatorData indicatorData = service.indicatorData("ff0ae3a9-fe01-41c9-a6e2-603b26b266fa").block();
        IndicatorValue indicatorValue = service.indicatorValue(indicatorData.getCalculationReference()).block();

        assertThat(indicatorData.getShortLabel()).isEqualTo("Proportion d'espèces en catégories éteintes ou menacées dans la liste rouge UICN-MNHN pour la France métropolitaine et ultramarine par rapport au nombre total d'espèces évaluées (pour les groupes évalués dans leur totalité)");
        assertThat(indicatorValue.getValue()).isBetween(0.0, 100.0);
    }
}
