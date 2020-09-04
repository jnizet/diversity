package fr.mnhn.diversity.web;

import fr.mnhn.diversity.repository.Element;
import fr.mnhn.diversity.repository.Image;
import fr.mnhn.diversity.repository.Link;
import fr.mnhn.diversity.repository.Text;

/**
 * Utility class for test helper functions
 * @author JB Nizet
 */
public class WebTestUtil {
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
