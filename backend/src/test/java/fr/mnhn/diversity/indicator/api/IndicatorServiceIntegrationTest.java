package fr.mnhn.diversity.indicator.api;

import static org.assertj.core.api.Assertions.assertThat;

import fr.mnhn.diversity.territory.Territory;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
        ValuedIndicator indicator = service.indicator("8816092b-1ce3-4ae7-815d-019e99ecf545").block();

        assertThat(indicator.getData().getShortLabel()).isEqualTo("Évolution du taux de boisement dans les Outre-Mer");
        assertThat(indicator.getValues().get(Territory.OUTRE_MER).getValue()).isBetween(0.0, 100.0);
    }

    @Disabled("we don't want the tests to fail if the API is unavailable or if we don't have WIFI." +
        " But it's nice to have this test during development to check that everything is OK.")
    @ParameterizedTest
    @ValueSource(strings = {
        "7be2a5a6-f226-4fa9-a383-79ca56ca8046",
        "f2a14850-23a9-43fc-b8d2-56aebb3562f8",
        "d99e52c9-7c71-47b4-a720-c0878d2993f7",
        "8816092b-1ce3-4ae7-815d-019e99ecf545",
        // "e1c91e2e-418e-4bd2-bdfe-7e1025f0b907", known to be buggy
        "10fe181c-e2b5-4267-b587-5f8c21501947",
        "298a3804-bcb0-4fdb-b3b3-31e14be2cac8",
        "0a494ee4-1c21-415e-be5d-b71e8f4b0519"
    })
    void shouldGetIndicatorAndValuesForKnownIndicators(String indicatorId) {
        ValuedIndicator indicator = service.indicator(indicatorId).block();

        assertThat(indicator.getData().getShortLabel()).isNotNull(); // yes, one of them (298a3804-bcb0-4fdb-b3b3-31e14be2cac8) has a blank short label
        assertThat(indicator.getValues().get(Territory.OUTRE_MER)).isNotNull();
        assertThat(indicator.getValues().size()).isGreaterThan(1);
    }
}
