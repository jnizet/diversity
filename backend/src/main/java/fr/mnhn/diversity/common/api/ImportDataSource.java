package fr.mnhn.diversity.common.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Qualifier annotation to refer to the indicators-specific beans, such as the WebCLient used to query
 * the indicators web services
 * @author JB Nizet
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface ImportDataSource {
}
