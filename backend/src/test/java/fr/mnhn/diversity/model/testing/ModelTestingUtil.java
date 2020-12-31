package fr.mnhn.diversity.model.testing;

import fr.mnhn.diversity.model.Element;
import fr.mnhn.diversity.model.Image;
import fr.mnhn.diversity.model.Link;
import fr.mnhn.diversity.model.Select;
import fr.mnhn.diversity.model.Text;

/**
 * Utility class for test helper functions
 * @author JB Nizet
 */
public class ModelTestingUtil {
    public static Text text(String text) {
        return Element.text(0L, "text", text);
    }

    public static Select select(String value) {
        return Element.select(0L, "value", value);
    }

    public static Image image(Long imageId) {
        return Element.image(0L, "image", imageId, "alt");
    }

    public static Image multiSizeImage(Long imageId) {
        return Element.image(0L, "image", imageId, "alt", true);
    }

    public static Link link(String text) {
        return Element.link(0L, "link", text, "https://test.com");
    }
}
