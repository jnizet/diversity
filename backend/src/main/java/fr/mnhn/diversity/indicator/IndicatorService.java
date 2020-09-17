package fr.mnhn.diversity.indicator;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.mnhn.diversity.territory.Territory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Service used to get indicators from external HTTP services
 * @author JB Nizet
 */
@Service
public class IndicatorService {

    private final WebClient webClient;

    public IndicatorService(@Indicators WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<IndicatorData> indicatorData(String indicatorId) {
        return webClient
            .get()
            .uri("/indicators/{id}", indicatorId)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(IndicatorBody.class)
            .map(body ->
                 new IndicatorData(
                     indicatorId,
                     body.getShortLabel(),
                     body.getCalculationReference()
                 )
            );
    }

    public Mono<Map<Territory, IndicatorValue>> indicatorValues(String calculationReference) {
        return webClient
            .get()
            .uri("/calculations/{calculationReference}?embed=CALCULATIONRESULTS", calculationReference)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(IndicatorValueBody.class)
            .map(IndicatorValueBody::getIndicatorValues);
    }

    private static final class IndicatorBody {

        private final String shortLabel;
        private final String calculationReference;

        @JsonCreator
        public IndicatorBody(@JsonProperty("shortLabel") String shortLabel,
                             @JsonProperty("calculReference") String calculationReference) {
            this.shortLabel = shortLabel;
            this.calculationReference = calculationReference;
        }

        public String getShortLabel() {
            return shortLabel;
        }

        public String getCalculationReference() {
            return this.calculationReference;
        }
    }

    private static final class IndicatorValueBody {

        private Embedded embedded;

        @JsonCreator
        public IndicatorValueBody(@JsonProperty("_embedded") Embedded embedded) {
            this.embedded = embedded;
        }

        public Embedded getEmbedded() {
            return embedded;
        }

        public Map<Territory, IndicatorValue> getIndicatorValues() {
            CalculationResult mainCalculationResult = embedded.findMainCalculationResult();
            Metric mainMetric = mainCalculationResult.getValues().get(0).getMetrics().get(0);
            IndicatorValue mainIndicatorValue = new IndicatorValue(
                mainMetric.getValueAsDouble(),
                mainMetric.getUnit()
            );

            Map<Territory, IndicatorValue> result = new HashMap<>();
            result.put(Territory.OUTRE_MER, mainIndicatorValue);

            Map<String, Territory> territoriesByBiomKey =
                EnumSet.complementOf(EnumSet.of(Territory.OUTRE_MER))
                    .stream()
                    .collect(Collectors.toMap(Territory::getBiomKey, Function.identity()));

            embedded.findSecondaryCalculationResult().ifPresent(secondaryCalculationResult -> {
                for (CalculationValue calculationValue : secondaryCalculationResult.getValues()) {
                    String biomKey = calculationValue.getHold();
                    Territory territory = territoriesByBiomKey.get(biomKey);
                    if (territory != null) {
                        Metric metric = calculationValue.getMetrics().get(0);
                        IndicatorValue indicatorValue = new IndicatorValue(
                            metric.getValueAsDouble(),
                            metric.getUnit()
                        );
                        result.put(territory, indicatorValue);
                    }
                }
            });

            return result;
        }
    }

    private static final class Embedded {

        private final List<CalculationResult> calculationResults;

        @JsonCreator
        public Embedded(@JsonProperty("calculationResults") List<CalculationResult> calculationResults) {
            this.calculationResults = calculationResults;
        }

        public CalculationResult findMainCalculationResult() {
            return calculationResults
                .stream()
                .filter(r -> r.isMain())
                .findAny()
                .orElseThrow(() -> new IllegalStateException("no main calculation result found"));
        }

        public Optional<CalculationResult> findSecondaryCalculationResult() {
            return calculationResults
                .stream()
                .filter(r -> r.getCode().equals("R2"))
                .findAny();
        }
    }

    private static final class CalculationResult {
        private final boolean main;
        private final String code;
        private final List<CalculationValue> values;

        @JsonCreator
        public CalculationResult(@JsonProperty("main") boolean main,
                                 @JsonProperty("code") String code,
                                 @JsonProperty("values") List<CalculationValue> values) {
            this.main = main;
            this.code = code;
            this.values = values;
        }

        public boolean isMain() {
            return main;
        }

        public List<CalculationValue> getValues() {
            return values;
        }

        public String getCode() {
            return code;
        }
    }

    private static final class CalculationValue {

        private final String hold;
        private final List<Metric> metrics;

        @JsonCreator
        public CalculationValue(@JsonProperty("hold") String hold,
                                @JsonProperty("metric") List<Metric> metrics) {
            this.hold = hold;
            this.metrics = metrics;
        }

        public String getHold() {
            return hold;
        }

        public List<Metric> getMetrics() {
            return metrics;
        }
    }

    private static final class Metric {
        private final String value;
        private final String unit;

        @JsonCreator
        public Metric(@JsonProperty("value") String value,
                      @JsonProperty("unit") String unit) {
            this.value = value;
            this.unit = unit;
        }

        public String getValue() {
            return value;
        }

        public String getUnit() {
            return unit;
        }

        public double getValueAsDouble() {
            return Double.parseDouble(value.replace(',', '.'));
        }
    }
}
