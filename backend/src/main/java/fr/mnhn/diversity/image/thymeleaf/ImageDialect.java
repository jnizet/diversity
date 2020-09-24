package fr.mnhn.diversity.image.thymeleaf;

import java.util.Set;

import org.springframework.stereotype.Component;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.StandardDialect;

/**
 * Thymeleaf dialect allowing to more easily insert uploaded images in templates easily
 * @author JB Nizet
 */
@Component
public class ImageDialect extends AbstractProcessorDialect {
    private static final String DIALECT_NAME = "Image Dialect";

    public ImageDialect() {
        super(DIALECT_NAME, "biom", StandardDialect.PROCESSOR_PRECEDENCE);
    }

    public Set<IProcessor> getProcessors(final String dialectPrefix) {
        return Set.of(new ImageAttributeTagProcessor(dialectPrefix));
    }
}

