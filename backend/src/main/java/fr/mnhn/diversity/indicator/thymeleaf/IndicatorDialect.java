package fr.mnhn.diversity.indicator.thymeleaf;

import java.util.Set;

import org.springframework.stereotype.Component;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;

/**
 * A thymeleaf dialect which adds a <code>#indicators</code> expression object, of type {@link Indicators}
 * @author JB Nizet
 */
@Component
public class IndicatorDialect implements IExpressionObjectDialect {

    private static final Set<String> INDICATORS = Set.of("indicators");

    @Override
    public IExpressionObjectFactory getExpressionObjectFactory() {
        return new IExpressionObjectFactory() {
            @Override
            public Set<String> getAllExpressionObjectNames() {
                return INDICATORS;
            }

            @Override
            public Indicators buildObject(IExpressionContext context, String expressionObjectName) {
                return new Indicators(context.getLocale());
            }

            @Override
            public boolean isCacheable(String expressionObjectName) {
                return false;
            }
        };
    }

    @Override
    public String getName() {
        return "IndicatorValue";
    }
}
