package fr.mnhn.diversity.common.exception;

import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class used to register a custom error attributes, used to add a <code>functionalError</code>
 * attribute to the standard JSON response returned when an exception is thrown by an MVC controller.
 */
@Configuration
public class ExceptionConfig {
    @Bean
    public ErrorAttributes errorAttributes() {
        return new CustomErrorAttributes();
    }
}
