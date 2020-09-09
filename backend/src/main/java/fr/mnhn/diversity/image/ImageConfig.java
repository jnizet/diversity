package fr.mnhn.diversity.image;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class to enable image configuration properties
 * @author JB Nizet
 */
@Configuration
@EnableConfigurationProperties(ImageProperties.class)
public class ImageConfig {
}
