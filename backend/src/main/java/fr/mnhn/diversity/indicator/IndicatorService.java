package fr.mnhn.diversity.indicator;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Service used to get indicators from externalHTTP services
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

    public Mono<IndicatorValue> indicatorValue(String calculationReference) {
        return webClient
            .get()
            .uri("/calculations/{calculationReference}?embed=CALCULATIONRESULTS", calculationReference)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(IndicatorValueBody.class)
            .map(IndicatorValueBody::getIndicatorValue);
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

        public IndicatorValue getIndicatorValue() {
            CalculationResult calculationResult = embedded.findMainCalculationResult();
            Metric metric = calculationResult.getValues().get(0).getMetrics().get(0);
            return new IndicatorValue(
                metric.getValueAsDouble(),
                metric.getUnit()
            );
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
    }

    private static final class CalculationResult {
        private final boolean main;
        private final List<CalculationValue> values;

        @JsonCreator
        public CalculationResult(@JsonProperty("main") boolean main,
                                 @JsonProperty("values") List<CalculationValue> values) {
            this.main = main;
            this.values = values;
        }

        public boolean isMain() {
            return main;
        }

        public List<CalculationValue> getValues() {
            return values;
        }
    }

    private static final class CalculationValue {

        private final List<Metric> metrics;

        @JsonCreator
        public CalculationValue(@JsonProperty("metric") List<Metric> metrics) {
            this.metrics = metrics;
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
