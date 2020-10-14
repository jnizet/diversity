package fr.mnhn.diversity.model;

public class PageUtils {

    /**
     * Finds the maximum index of the list with the specified prefix in the page.
     */
    public static int findMaxListIndex(Page page, String keyPrefix) {
        return page.getElements()
                   .values()
                   .stream()
                   .map(Element::getKey)
                   .filter(key -> key.startsWith(keyPrefix))
                   .mapToInt(key -> {
                       String end = key.substring(keyPrefix.length());
                       int dotIndex = end.indexOf('.');
                       if (dotIndex < 0) {
                           return -1;
                       }
                       String indexAsString = end.substring(0, dotIndex);
                       try {
                           return Integer.parseInt(indexAsString);
                       } catch (NumberFormatException e) {
                           return -1;
                       }
                   })
                   .max()
                   .orElse(-1);
    }

    /**
     * Returns the element of the page with the specified key, after checking its existence and validity.
     */
    public static Element getElement(Page page, String key, ElementType elementType) {
        Element element = page.getElements().get(key);
        if (element == null) {
            throw new IllegalStateException("No element with key " + key + " in page " + page.getId());
        }
        if (element.getType() != elementType) {
            throw new IllegalStateException(
                "Element with key " + key +
                    " in page " + page.getId() +
                    " should be of type " + elementType +
                    " but is of type " + element.getType());
        }
        return element;
    }
}
