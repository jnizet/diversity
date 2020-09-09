package fr.mnhn.diversity.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.mnhn.diversity.model.meta.ImageElement;
import fr.mnhn.diversity.model.meta.LinkElement;
import fr.mnhn.diversity.model.meta.ListElement;
import fr.mnhn.diversity.model.meta.PageElement;
import fr.mnhn.diversity.model.meta.PageElementVisitor;
import fr.mnhn.diversity.model.meta.PageModel;
import fr.mnhn.diversity.model.meta.SectionElement;
import fr.mnhn.diversity.model.meta.TextElement;
import org.springframework.stereotype.Service;

/**
 * A service allowing to populate a page from a persistent Page instance and its PageModel
 * @author JB Nizet
 */
@Service
public class PageService {

    /**
     * Returns a Map containing the structure of the page, matching with the given model.
     * The map keys are the names of the PageModel elements.
     * It's values are
     * <ul>
     *   <li>another Map if the element with that name is a SectionElement</li>
     *   <li>a List if the element with that name is a ListElement</li>
     *   <li>an Element of the matching type if the element with that name is a TextElement, a LinkElement or an ImageElement</li>
     * </ul>
     *
     * The elements of the inner maps and lists, similarly, are of the same types, depending on the content of the
     * SectionElement and ListElement.
     *
     * So, for example, for a page model such as
     *
     * <pre>
     *     - ImageElement background
     *     - SectionElement part1
     *         - TextElement title
     *         - ListElement carousel
     *             - ImageElement image
     * </pre>
     *
     * this method will look in the Page for elements with the following keys and values:
     * <ul>
     *     <li>background of type Image</li>
     *     <li>part1.title of type Text</li>
     *     <li>part1.carousel.0.image of type Image</li>
     *     <li>part1.carousel.1.image of type Image</li>
     *     <li>...</li>
     * </ul>
     *
     * As soon as an index in the list is not found, it's considered ended. So there can't be null elements in the list.
     *
     * And that will create a Map such as
     *
     * <pre>
     *     {
     *         background: Image
     *         part1: {
     *             title: Text
     *             carousel: [
     *                 Image
     *                 Image
     *             ]
     *         }
     *     }
     * </pre>
     */
    public Map<String, Object> buildPage(PageModel model, Page page) {
        PagePopulatorVisitor visitor = new PagePopulatorVisitor(page, "");
        for (PageElement pageElement : model.getElements()) {
            pageElement.accept(visitor);
        }
        return visitor.getResult();
    }

    private static class PagePopulatorVisitor implements PageElementVisitor<Void> {
        private final Page page;
        private final String prefix;
        private final Map<String, Object> result = new HashMap<>();

        public PagePopulatorVisitor(Page page, String prefix) {
            this.page = page;
            this.prefix = prefix;
        }

        @Override
        public Void visitSection(SectionElement section) {
            String name = section.getName();
            String key = prefix + name;
            PagePopulatorVisitor sectionVisitor = new PagePopulatorVisitor(page,key + ".");
            for (PageElement element : section.getElements()) {
                element.accept(sectionVisitor);
            }
            result.put(name, sectionVisitor.getResult());
            return null;
        }

        @Override
        public Void visitList(ListElement list) {
            // start by adding the empty list to the result
            List<Object> theList = new ArrayList<>();
            result.put(list.getName(), theList);

            // if the list model has no element (which should never happen), there is nothing to add to the list
            if (list.getElements().isEmpty()) {
                return null;
            }

            // look for a child element which is not a list nor a section (i.e. a leaf element). This element would
            // thus have the keys
            // ....listName.0.elementName,
            // ....listName.1.elementName
            // etc.
            list.getElements().stream().filter(PageElement::isLeaf).findAny().ifPresentOrElse(leafElement -> {
                // if we have such a leaf element, we loop until we don't find a key named ....listName.N.elementName
                String listKey = prefix + list.getName();
                String leafElementName = leafElement.getName();
                boolean stop = false;
                for (int i = 0; !stop; i++) {
                    String elementsPrefix = listKey + "." + i + ".";
                    String leafElementKey = elementsPrefix + leafElementName;
                    if (!page.getElements().containsKey(leafElementKey)) {
                        // the key is not found, so we there is no element to add to the list anymore
                        stop = true;
                    } else {
                        PagePopulatorVisitor listVisitor = new PagePopulatorVisitor(page, elementsPrefix);
                        for (PageElement element : list.getElements()) {
                            element.accept(listVisitor);
                        }
                        theList.add(listVisitor.getResult());
                    }
                }
            }, () -> {
                // there is no leaf element. This should happen very rarely: it means there are only sections or lists
                // in the list. Since those only exist in the model, to structure the page, but not in the database
                // as page elements, we have to find the biggest value of N in the keys looking like like
                // ....listName.N....
                // This is less efficient because we have to iterate through the
                // entries of the map
                String listKey = prefix + list.getName();
                String keyPrefix = listKey + ".";
                int maxListIndex = findMaxListIndex(keyPrefix);
                for (int i = 0; i <= maxListIndex; i++) {
                    String elementsPrefix = keyPrefix + i + ".";
                    PagePopulatorVisitor listVisitor = new PagePopulatorVisitor(page, elementsPrefix);
                    for (PageElement element : list.getElements()) {
                        element.accept(listVisitor);
                    }
                    theList.add(listVisitor.getResult());
                }
            });

            return null;
        }

        private int findMaxListIndex(String keyPrefix) {
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
                           }
                           catch (NumberFormatException e) {
                               return -1;
                           }
                       })
                       .max()
                       .orElse(-1);
        }

        @Override
        public Void visitText(TextElement text) {
            String name = text.getName();
            String key = prefix + name;
            result.put(name, getElement(key, ElementType.TEXT));
            return null;
        }

        @Override
        public Void visitImage(ImageElement image) {
            String name = image.getName();
            String key = prefix + name;
            result.put(name, getElement(key, ElementType.IMAGE));
            return null;
        }

        @Override
        public Void visitLink(LinkElement link) {
            String name = link.getName();
            String key = prefix + name;
            result.put(name, getElement(key, ElementType.LINK));
            return null;
        }

        public Map<String, Object> getResult() {
            return result;
        }

        private Element getElement(String key, ElementType elementType) {
            Element element = this.page.getElements().get(key);
            if (element == null) {
                throw new IllegalStateException("No text element with key " + key + " in page " + page.getId());
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
}