package fr.mnhn.diversity.admin;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class used to install the admin index filter
 * @author JB Nizet
 */
@Configuration
public class AdminWebConfig {
    @Bean
    public FilterRegistrationBean<AdminIndexFilter> adminIndexFilter() {
        FilterRegistrationBean<AdminIndexFilter> result = new FilterRegistrationBean<>(
            new AdminIndexFilter()
        );
        result.addUrlPatterns("/admin/*");
        return result;
    }
}
