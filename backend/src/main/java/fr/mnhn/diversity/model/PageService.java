package fr.mnhn.diversity.model;

import fr.mnhn.diversity.common.api.ImportDataSource;
import fr.mnhn.diversity.model.meta.CheckboxElement;
import fr.mnhn.diversity.model.meta.MultiListElement;
import fr.mnhn.diversity.model.meta.MultiListTemplateElement;
import fr.mnhn.diversity.model.meta.SelectElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.mnhn.diversity.common.exception.BadRequestException;
import fr.mnhn.diversity.model.meta.ImageElement;
import fr.mnhn.diversity.model.meta.LinkElement;
import fr.mnhn.diversity.model.meta.ListElement;
import fr.mnhn.diversity.model.meta.PageElement;
import fr.mnhn.diversity.model.meta.PageElementVisitor;
import fr.mnhn.diversity.model.meta.PageModel;
import fr.mnhn.diversity.model.meta.SectionElement;
import fr.mnhn.diversity.model.meta.TextElement;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * A service allowing to populate a page from a persistent Page instance and its PageModel
 * @author JB Nizet
 */
@Service
public class PageService {

    private final WebClient webClient;
    private final String token;

    public PageService(@ImportDataSource WebClient webClient, @ImportDataSource String token) {
        this.webClient = webClient;
        this.token = token;
    }


    public Mono<String> getPageDataFromImportDataSource(String pageModel, String pageName){
        return webClient
            .get()
            .uri("/api/pages/{pageModel}/{pageName}", pageModel, pageName)
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization","Key " + token)
            .retrieve()
            .bodyToMono(String.class);
    }

    /**
     * Returns a {@link PageContent} containing the information of the given page, and its content, structured as
     * described in {@link PageContent#content}.
     * For example, for a page model such as
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
     */
    public PageContent buildPageContent(PageModel model, Page page) {
        PagePopulatorVisitor visitor = new PagePopulatorVisitor(page, "");
        for (PageElement pageElement : model.getElements()) {
            pageElement.accept(visitor);
        }
        return new PageContent(page, visitor.getResult());
    }

    /**
     * Validates that the given page contains all the required elements and nothing else based on the
     * given page model
     */
    public void validatePageContent(PageModel model, Page page) throws BadRequestException {
        PagePopulatorVisitor visitor = new PagePopulatorVisitor(page, "");
        try {
            for (PageElement pageElement : model.getElements()) {
                pageElement.accept(visitor);
            }
        } catch (Exception e) {
            throw new BadRequestException("Invalid page: " + e.getMessage());
        }
        if (!visitor.allElementsUsed()) {
            throw new BadRequestException("Invalid page: some elements are not part of the model");
        }
    }

    private static class PagePopulatorVisitor implements PageElementVisitor<Void> {
        private final Page page;
        private final String prefix;
        private final Map<String, Object> result = new HashMap<>();
        private final Set<Element> usedElements;

        public PagePopulatorVisitor(Page page, String prefix) {
            this.page = page;
            this.prefix = prefix;
            this.usedElements = new HashSet<>();
        }

        public PagePopulatorVisitor(Page page, String prefix, Set<Element> usedElements) {
            this.page = page;
            this.prefix = prefix;
            this.usedElements = usedElements;
        }

        @Override
        public Void visitSection(SectionElement section) {
            String name = section.getName();
            String key = prefix + name;
            PagePopulatorVisitor sectionVisitor = new PagePopulatorVisitor(page,key + ".", usedElements);
            for (PageElement element : section.getElements()) {
                element.accept(sectionVisitor);
            }
            result.put(name, sectionVisitor.getResult());
            return null;
        }

        @Override
        public Void visitMultiListElement(MultiListElement list) {
            // start by adding the empty list to the result
            List<Object> theList = new ArrayList<>();
            result.put(list.getName(), theList);

            // if the list model has no element (which should never happen), there is nothing to add to the list
            if (list.getTemplates().isEmpty()) {
                return null;
            }

            // look for a child element which is not a list nor a section (i.e. a leaf element). This element would
            // thus have the keys
            // ....listName.0.elementName,
            // ....listName.1.elementName
            // etc.
            list.getTemplates().stream().filter(PageElement::isLeaf).findAny().ifPresentOrElse(leafElement -> {
                // if we have such a leaf element, we loop until we don't find a key named ....listName.N.elementName
                String listKey = prefix + list.getName();
                String leafElementName = leafElement.getName();
                boolean stop = false;
                for (int i = 0; !stop; i++) {
                    String elementsPrefix = listKey + "." + i + ".";
                    String leafElementKey = elementsPrefix;
                    if (!page.getElements().containsKey(leafElementKey)) {
                        // the key is not found, so we there is no element to add to the list anymore
                        stop = true;
                    } else {
                        PagePopulatorVisitor listVisitor = new PagePopulatorVisitor(page, elementsPrefix, usedElements);
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
                list.getTemplates().stream().forEach(t ->
                    page.getElements().keySet().stream().filter(e ->
                        e.contains(t.getName()) && e.contains(keyPrefix)
                    ).forEach( e -> {
                        var index = e.substring(keyPrefix.length(), e.length() - 1).split("\\.")[0];
                        String elementsPrefix = keyPrefix + index + ".";
                        PagePopulatorVisitor listVisitor = new PagePopulatorVisitor(page,
                            elementsPrefix, usedElements);
                        t.accept(listVisitor);
                        theList.add(listVisitor.getResult());
                    })
                );
            });

            return null;
        }

        @Override
        public Void visitMultiListTemplateElement(
            MultiListTemplateElement multiListTemplateElement) {
            String name = multiListTemplateElement.getName();
            String key = prefix + name;
            PagePopulatorVisitor sectionVisitor = new PagePopulatorVisitor(page,key + ".", usedElements);
            for (PageElement element : multiListTemplateElement.getElements()) {
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
                        PagePopulatorVisitor listVisitor = new PagePopulatorVisitor(page, elementsPrefix, usedElements);
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
                int maxListIndex = PageUtils.findMaxListIndex(page, keyPrefix);
                for (int i = 0; i <= maxListIndex; i++) {
                    String elementsPrefix = keyPrefix + i + ".";
                    PagePopulatorVisitor listVisitor = new PagePopulatorVisitor(page, elementsPrefix, usedElements);
                    for (PageElement element : list.getElements()) {
                        var a  = page;
                        var b = element;
                        element.accept(listVisitor);
                    }
                    theList.add(listVisitor.getResult());
                }
            });

            return null;
        }

        @Override
        public Void visitText(TextElement text) {
            String name = text.getName();
            String key = prefix + name;
            Element element = PageUtils.getElement(page, key, ElementType.TEXT);
            result.put(name, element);
            usedElements.add(element);
            return null;
        }

        @Override
        public Void visitImage(ImageElement image) {
            String name = image.getName();
            String key = prefix + name;
            Image element = ((Image) PageUtils.getElement(page, key, ElementType.IMAGE));
            usedElements.add(element);
            result.put(name, element.withMultiSize(image.isMultiSize()));
            return null;
        }

        @Override
        public Void visitLink(LinkElement link) {
            String name = link.getName();
            String key = prefix + name;
            Element element = PageUtils.getElement(page, key, ElementType.LINK);
            result.put(name, element);
            usedElements.add(element);
            return null;
        }

        @Override
        public Void visitSelect(SelectElement select) {
            String name = select.getName();
            String key = prefix + name;
            Element element = PageUtils.getElement(page, key, ElementType.SELECT);
            result.put(name, element);
            usedElements.add(element);
            return null;
        }

        @Override
        public Void visitCheckbox(CheckboxElement select) {
            String name = select.getName();
            String key = prefix + name;
            Element element = PageUtils.getElement(page, key, ElementType.CHECKBOX);
            result.put(name, element);
            usedElements.add(element);
            return null;
        }

        public Map<String, Object> getResult() {
            return result;
        }

        public boolean allElementsUsed() {
            return usedElements.containsAll(page.getElements().values());
        }
    }
}
