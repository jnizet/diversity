package fr.mnhn.diversity.common.api;

import java.util.Objects;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for the indicators API
 * @author JB Nizet
 */
@ConfigurationProperties("diversity.import")
public class ImportDataSourceProperties {
    /**
     * The base URL, without ending slash, of the API
     */
    private String baseUrl = "https://test.biodiversite-outre-mer.fr";

    private String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTYyMTE1NTI5M30.XRZ0DrLeaQm5hLi-8jOWYv5vMrNtzU1ctUPzrA3vABA";

    public ImportDataSourceProperties(String baseUrl, String token) {
        this.token = token;
        this.baseUrl = baseUrl;
    }

    public ImportDataSourceProperties() {
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getToken() { return token; }

    public void setToken(String token) {  this.token = token; }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ImportDataSourceProperties)) {
            return false;
        }
        ImportDataSourceProperties that = (ImportDataSourceProperties) o;
        return Objects.equals(baseUrl, that.baseUrl)
            && Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseUrl, token);
    }

    @Override
    public String toString() {
        return "ImportDataSourceProperties{" +
            "baseUrl='" + baseUrl + '\'' +
            "token='" + token + '\'' +
            '}';
    }
}
