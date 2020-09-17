package fr.mnhn.diversity.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Configuration class enabling scheduling, but not in the e2e and test profiles, where we don't want to be
 * polluted by scheduled jobs running in parallel to our tests and modifying the database.
 * @author JB Nizet
 */
@Configuration
@EnableScheduling
@Profile("!test & !e2e")
public class SchedulingConfig {
}
