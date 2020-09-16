package fr.mnhn.diversity.contact;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for contact
 * @author JB Nizet
 */
@Configuration
@EnableConfigurationProperties(ContactProperties.class)
public class ContactConfig {
}
