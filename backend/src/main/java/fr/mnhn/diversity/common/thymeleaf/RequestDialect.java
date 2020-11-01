package fr.mnhn.diversity.common.thymeleaf;

import java.util.Set;

import fr.mnhn.diversity.indicator.thymeleaf.Indicators;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;

/**
 * A thymeleaf dialect which adds a <code>#requests</code> expression object, of type {@link Requests}
 * @author JB Nizet
 */
@Component
public class RequestDialect implements IExpressionObjectDialect {

    private static final Set<String> REQUESTS = Set.of("requests");

    @Override
    public IExpressionObjectFactory getExpressionObjectFactory() {
        return new IExpressionObjectFactory() {
            @Override
            public Set<String> getAllExpressionObjectNames() {
                return REQUESTS;
            }

            @Override
            public Requests buildObject(IExpressionContext context, String expressionObjectName) {
                return new Requests();
            }

            @Override
            public boolean isCacheable(String expressionObjectName) {
                return false;
            }
        };
    }

    @Override
    public String getName() {
        return "Requests";
    }
}
