package fr.mnhn.diversity.indicator;

import java.util.Objects;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for the indicators API
 * @author JB Nizet
 */
@ConfigurationProperties("diversity.indicators")
public class IndicatorProperties {

    /**
     * The base URL, without ending slash, of the API
     */
    private String baseUrl = "https://odata-indicateurs.mnhn.fr";

    public IndicatorProperties(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public IndicatorProperties() {
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IndicatorProperties)) {
            return false;
        }
        IndicatorProperties that = (IndicatorProperties) o;
        return Objects.equals(baseUrl, that.baseUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseUrl);
    }

    @Override
    public String toString() {
        return "IndicatorProperties{" +
            "baseUrl='" + baseUrl + '\'' +
            '}';
    }
}
