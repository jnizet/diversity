package fr.mnhn.diversity.common.thymeleaf;

import fr.mnhn.diversity.image.thymeleaf.ImageAttributeTagProcessor;
import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Component;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.StandardDialect;

/**
 * Thymeleaf biom dialect allowing insert custom tag processor
 * @author Arnaud Monteils
 */

@Component
public class BiomDialect extends AbstractProcessorDialect {
    private static final String DIALECT_NAME = "Biom Dialect";
    public BiomDialect() {
        super(DIALECT_NAME, "biom", StandardDialect.PROCESSOR_PRECEDENCE);
    }

    public Set<IProcessor> getProcessors(final String dialectPrefix) {
        return Set.of(
            new ImageAttributeTagProcessor(dialectPrefix),
            new MarkdownAttributeTagProcessor(dialectPrefix)
        );
    }
}


