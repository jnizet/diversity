package fr.mnhn.diversity.admin.security;

import java.util.List;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for security-related stuff
 * @author JB Nizet
 */
@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityConfig {
    @Bean
    public FilterRegistrationBean<AuthenticationFilter> authenticationFilterRegistration(JwtHelper jwtHelper,
                                                                                         UserRepository userRepository, ApiKeyRepository apiKeyRepository) {
        FilterRegistrationBean<AuthenticationFilter> registration = new FilterRegistrationBean<>(
            new AuthenticationFilter(jwtHelper, userRepository, apiKeyRepository)
        );
        registration.setUrlPatterns(List.of("/api/*"));
        return registration;
    }
}
