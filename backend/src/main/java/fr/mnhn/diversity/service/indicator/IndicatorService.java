package fr.mnhn.diversity.service.indicator;

import java.util.List;

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

    private static class IndicatorBody {
        private String shortLabel;

        @JsonProperty("_links")
        private IndicatorLinks links;

        public String getShortLabel() {
            return shortLabel;
        }

        public void setShortLabel(String shortLabel) {
            this.shortLabel = shortLabel;
        }

        public IndicatorLinks getLinks() {
            return links;
        }

        public void setLinks(IndicatorLinks links) {
            this.links = links;
        }
    }

    private static class IndicatorLinks {
        @JsonProperty("calculationReference")
        private List<IndicatorLink> calculationReferences;

        public List<IndicatorLink> getCalculationReferences() {
            return calculationReferences;
        }

        public void setCalculationReferences(List<IndicatorLink> calculationReferences) {
            this.calculationReferences = calculationReferences;
        }
    }

    private static class IndicatorLink {
        private String href;

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }
    }

    private static class IndicatorValueBody {
        @JsonProperty("_embedded")
        private Embedded embedded;

        public Embedded getEmbedded() {
            return embedded;
        }

        public void setEmbedded(Embedded embedded) {
            this.embedded = embedded;
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

    private static class Embedded {
        private List<CalculationResult> calculationResults;

        public List<CalculationResult> getCalculationResults() {
            return calculationResults;
        }

        public void setCalculationResults(List<CalculationResult> calculationResults) {
            this.calculationResults = calculationResults;
        }

        public CalculationResult findCalculationResult() {
            return calculationResults
                .stream()
                .filter(r -> r.getCode().equals("R1"))
                .findAny()
                .orElseThrow(() -> new IllegalStateException("no result with code R1"));
        }
    }

    private static class CalculationResult {
        private String code;
        private List<CalculationValue> values;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public List<CalculationValue> getValues() {
            return values;
        }

        public void setValues(List<CalculationValue> values) {
            this.values = values;
        }
    }

    private static class CalculationValue {
        @JsonProperty("metric")
        private List<Metric> metrics;

        public List<Metric> getMetrics() {
            return metrics;
        }

        public void setMetrics(List<Metric> metrics) {
            this.metrics = metrics;
        }
    }

    private static class Metric {
        private String value;
        private String unit;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public double getValueAsDouble() {
            return Double.parseDouble(value.replace(',', '.'));
        }
    }
}
