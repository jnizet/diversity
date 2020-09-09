package fr.mnhn.diversity.model.testing;

import fr.mnhn.diversity.model.Element;
import fr.mnhn.diversity.model.Image;
import fr.mnhn.diversity.model.Link;
import fr.mnhn.diversity.model.Text;

/**
 * Utility class for test helper functions
 * @author JB Nizet
 */
public class ModelTestingUtil {
    public static Text text(String text) {
        return Element.text(0L, "text", text);
    }

    public static Image image(String imageId) {
        return Element.image(0L, "image", imageId, "alt");
    }

    public static Link link(String text) {
        return Element.link(0L, "link", text, "https://test.com");
    }
}
