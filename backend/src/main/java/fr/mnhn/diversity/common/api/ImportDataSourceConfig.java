package fr.mnhn.diversity.common.api;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuration class for indicators
 * @author JB Nizet
 */
@Configuration
@EnableConfigurationProperties(ImportDataSourceProperties.class)
public class ImportDataSourceConfig {
    private final ImportDataSourceProperties importDataSourceProperties;

    public ImportDataSourceConfig(ImportDataSourceProperties importProperties) {
        this.importDataSourceProperties = importProperties;
    }

    @Bean
    @ImportDataSource
    public WebClient importWebClient(WebClient.Builder builder) {
        return builder.baseUrl(importDataSourceProperties.getBaseUrl()).build();
    }

    @Bean
    @ImportDataSource
    public String token() {
        return importDataSourceProperties.getToken();
    }
}
