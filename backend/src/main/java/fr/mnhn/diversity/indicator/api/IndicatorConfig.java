package fr.mnhn.diversity.indicator.api;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuration class for indicators
 * @author JB Nizet
 */
@Configuration
@EnableConfigurationProperties(IndicatorProperties.class)
public class IndicatorConfig {

    private final IndicatorProperties indicatorProperties;

    public IndicatorConfig(IndicatorProperties indicatorProperties) {
        this.indicatorProperties = indicatorProperties;
    }

    @Bean
    @Indicators
    public WebClient indicatorsWebClient(WebClient.Builder builder) {
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024 * 10)).build();
        return builder.exchangeStrategies(exchangeStrategies).baseUrl(indicatorProperties.getBaseUrl()).build();
    }
}
