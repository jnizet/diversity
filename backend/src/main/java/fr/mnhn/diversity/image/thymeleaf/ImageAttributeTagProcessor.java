package fr.mnhn.diversity.image.thymeleaf;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.mnhn.diversity.model.Image;
import fr.mnhn.diversity.model.ImageSize;
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
 * A tag processor making it easier to insert uploaded images in the templates.
 *
 * Usage:
 * <pre>
 *     <img biom:image="${page.content.presentation.image}" />
 * </pre>
 *
 * Result (assuming page.content.presentation.image is an instance of Image with imageId=42 and alt="foo"):
 * <pre>
 *     <img src="/images/42/image" alt="foo" />
 * </pre>
 *
 * @author JB Nizet
 */
public class ImageAttributeTagProcessor extends AbstractAttributeTagProcessor {

    public ImageAttributeTagProcessor(String dialectPrefix) {
        super(
            TemplateMode.HTML,
            dialectPrefix,     // Prefix to be applied to name for matching
            "img",             // match only img tags
            false,             // No prefix to be applied to tag name
            "image",           // Name of the attribute that will be matched
            true,              // Apply dialect prefix to attribute name
            12000,             // Precedence (inside dialect's precedence)
            true);             // Remove the matched attribute afterwards
    }


    @Override
    protected void doProcess(
        ITemplateContext context,
        IProcessableElementTag tag,
        AttributeName attributeName,
        String attributeValue,
        IElementTagStructureHandler structureHandler) {

        IEngineConfiguration configuration = context.getConfiguration();
        IStandardExpressionParser parser = StandardExpressions.getExpressionParser(configuration);
        IStandardExpression expression = parser.parseExpression(context, attributeValue);
        Image imageElement = (Image) expression.execute(context);

        String srcUrl = context.buildLink("/images/{imageId}/image", Map.of("imageId", imageElement.getImageId()));
        String alt = imageElement.getAlt();

        if (imageElement.isMultiSize()) {
            String srcset =
                Stream.of(ImageSize.values())
                      .map(imageSize -> buildSrcsetElement(context, imageElement, imageSize))
                      .collect(Collectors.joining(", "));
            structureHandler.setAttribute("srcset", srcset);
        }

        structureHandler.setAttribute("src", srcUrl);
        structureHandler.setAttribute("alt", alt);
    }

    private String buildSrcsetElement(ITemplateContext context, Image imageElement, ImageSize imageSize) {
        String url =
            context.buildLink("/images/{imageId}/image/{size}",
                              Map.of("imageId", imageElement.getImageId(),
                                     "size", imageSize.name().toLowerCase()));
        return url + " " + imageSize.getWidth() + "w";
    }
}
