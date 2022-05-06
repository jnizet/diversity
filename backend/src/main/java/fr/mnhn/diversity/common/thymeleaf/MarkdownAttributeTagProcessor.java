package fr.mnhn.diversity.common.thymeleaf;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
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

    private final Parser markdownParser;
    private final HtmlRenderer markdownHtmlRenderer;

    public MarkdownAttributeTagProcessor(String dialectPrefix){
        super(
            TemplateMode.HTML,
            dialectPrefix,     // Prefix to be applied to name for matching
            null,              // match all tags
            false,             // No prefix to be applied to tag name
            "markdown",        // Name of the attribute that will be matched
            true,              // Apply dialect prefix to attribute name
            12000,             // Precedence (inside dialect's precedence)
            true);             // Remove the matched attribute afterwards

        this.markdownParser = Parser.builder().build();
        this.markdownHtmlRenderer = HtmlRenderer.builder().build();
    }

    @Override
    protected void doProcess(ITemplateContext context,
        IProcessableElementTag tag,
        AttributeName attributeName, String attributeValue,
        IElementTagStructureHandler structureHandler) {

        IEngineConfiguration configuration = context.getConfiguration();
        IStandardExpressionParser expressionParser = StandardExpressions.getExpressionParser(configuration);
        IStandardExpression expression = expressionParser.parseExpression(context, attributeValue);
        Object element = expression.execute(context);
        if (element != null) {
            String textElement = element.toString();
            Node document = markdownParser.parse(textElement);
            structureHandler.setBody(markdownHtmlRenderer.render(document), false);
        } else {
            structureHandler.setBody("", false);
        }
    }
}
