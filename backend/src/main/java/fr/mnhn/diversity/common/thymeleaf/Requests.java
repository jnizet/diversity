package fr.mnhn.diversity.common.thymeleaf;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * A utility class exposed as an expression object to thymeleaf templates by {@link RequestDialect}
 * @author JB Nizet
 */
public class Requests {
    public String absoluteBaseUrl() {
        return ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
    }
}
