package fr.mnhn.diversity.common.thymeleaf;

import fr.mnhn.diversity.model.Image;
import fr.mnhn.diversity.model.Text;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * Thymeleaf tag processor to interpret markdown
 * @author Arnaud Monteils
 */

public class MarkdownAttributeTagProcessor extends AbstractAttributeTagProcessor {

    public MarkdownAttributeTagProcessor(String dialectPrefix){
        super(
            TemplateMode.HTML,
            dialectPrefix,     // Prefix to be applied to name for matching
            null,             // match only img tags
            false,             // No prefix to be applied to tag name
            "markdown",           // Name of the attribute that will be matched
            true,              // Apply dialect prefix to attribute name
            12000,             // Precedence (inside dialect's precedence)
            true);           // Remove the matched attribute afterwards
    }

    @Override
    protected void doProcess(ITemplateContext context,
        IProcessableElementTag tag,
        AttributeName attributeName, String attributeValue,
        IElementTagStructureHandler structureHandler) {

        IEngineConfiguration configuration = context.getConfiguration();
        IStandardExpressionParser expressionParserparser = StandardExpressions.getExpressionParser(configuration);
        IStandardExpression expression = expressionParserparser.parseExpression(context, attributeValue);
        Object element = expression.execute(context);
        if(element != null){
            String textElement = element.toString();
            Parser parser = Parser.builder().build();
            Node document = parser.parse(textElement);
            HtmlRenderer renderer = HtmlRenderer.builder().build();

            structureHandler.setBody(renderer.render(document), false);
        }
    }
}
