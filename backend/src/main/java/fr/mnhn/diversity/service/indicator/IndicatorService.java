package fr.mnhn.diversity.service.indicator;

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
                     extractComputationUrl(body.getLinks().getCalculationReferences().get(0).getHref())
                 )
            );
    }

    public Mono<IndicatorValue> indicatorValue(String url) {
        return webClient
            .get()
            .uri(url)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(IndicatorValueBody.class)
            .map(IndicatorValueBody::getIndicatorValue);
    }

    private String extractComputationUrl(String href) {
        return href.replace("http://", "https://")
                   .replace("{?embed}", "?embed=CALCULATIONRESULTS");
    }

    private static final class IndicatorBody {

        private final String shortLabel;
        private final IndicatorLinks links;

        @JsonCreator
        public IndicatorBody(@JsonProperty("shortLabel") String shortLabel,
                             @JsonProperty("_links") IndicatorLinks links) {
            this.shortLabel = shortLabel;
            this.links = links;
        }

        public String getShortLabel() {
            return shortLabel;
        }

        public IndicatorLinks getLinks() {
            return links;
        }
    }

    private static final class IndicatorLinks {

        private final List<IndicatorLink> calculationReferences;

        @JsonCreator
        public IndicatorLinks(@JsonProperty("calculationReference") List<IndicatorLink> calculationReferences) {
            this.calculationReferences = calculationReferences;
        }

        public List<IndicatorLink> getCalculationReferences() {
            return calculationReferences;
        }
    }

    private static final class IndicatorLink {

        private final String href;

        @JsonCreator
        public IndicatorLink(@JsonProperty("href") String href) {
            this.href = href;
        }

        public String getHref() {
            return href;
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
            CalculationResult calculationResult = embedded.findCalculationResult();
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

        public List<CalculationResult> getCalculationResults() {
            return calculationResults;
        }

        public CalculationResult findCalculationResult() {
            return calculationResults
                .stream()
                .filter(r -> r.getCode().equals("R1"))
                .findAny()
                .orElseThrow(() -> new IllegalStateException("no result with code R1"));
        }
    }

    private static final class CalculationResult {
        private final String code;
        private final List<CalculationValue> values;

        @JsonCreator
        public CalculationResult(@JsonProperty("code") String code,
                                 @JsonProperty("values") List<CalculationValue> values) {
            this.code = code;
            this.values = values;
        }

        public String getCode() {
            return code;
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
