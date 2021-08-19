package fr.mnhn.diversity.media.thymeleaf;

import java.util.Set;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;


/**
 * A thymeleaf dialect which adds a <code>#media</code> expression object, of type {@link Media}
 * @author JB Nizet
 */
@Component
public class MediaDialect implements IExpressionObjectDialect {

    private static final Set<String> MEDIA = Set.of("media");

    @Override
    public IExpressionObjectFactory getExpressionObjectFactory() {
        return new IExpressionObjectFactory() {
            @Override
            public Set<String> getAllExpressionObjectNames() {
                return MEDIA;
            }

            @Override
            public Media buildObject(IExpressionContext context, String expressionObjectName) {
                return new Media();
            }

            @Override
            public boolean isCacheable(String expressionObjectName) {
                return false;
            }
        };
    }

    @Override
    public String getName() {
        return "MediaValue";
    }
}
