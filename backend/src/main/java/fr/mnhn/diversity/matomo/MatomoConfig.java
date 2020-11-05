package fr.mnhn.diversity.matomo;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@EnableConfigurationProperties(MatomoProperties.class)
public class MatomoConfig {

    @Bean
    public Matomo matomo(MatomoProperties properties) {
        return new Matomo(properties);
    }
}
