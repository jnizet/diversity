package fr.mnhn.diversity.indicator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuration class for indicators
 * @author JB Nizet
 */
@Configuration
public class IndicatorConfig {

    @Bean
    @Indicators
    public WebClient indicatorsWebClient(WebClient.Builder builder) {
        return builder.baseUrl("https://odata-indicateurs.mnhn.fr").build();
    }
}
