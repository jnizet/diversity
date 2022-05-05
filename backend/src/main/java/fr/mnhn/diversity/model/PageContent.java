package fr.mnhn.diversity.model;

import java.util.Map;
import java.util.Objects;

/**
 * The content of a page, containing the page name, its model, its title, and the content as a Map.
 * @author JB Nizet
 */
public final class PageContent {
    private final Long id;
    private final String name;
    private final String modelName;
    private final String title;

    /**
     * a Map containing the content of the page, in a structure matching with its model.
     * The map keys are the names of the PageModel elements.
     * It's values are
     * <ul>
     *   <li>another Map if the element with that name is a SectionElement</li>
     *   <li>a List if the element with that name is a ListElement</li>
     *   <li>an Element of the matching type if the element with that name is a TextElement, a LinkElement or an ImageElement</li>
     * </ul>
     *
     * The elements of the inner maps, similarly, are of the same types, depending on the content of the
     * SectionElement.
     *
     * The elements of the lists are always Maps: a list is basically a repeating section.
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
     * and persistent page elements with keys
     * <ul>
     *     <li>background of type Image</li>
     *     <li>part1.title of type Text</li>
     *     <li>part1.carousel.0.image of type Image</li>
     *     <li>part1.carousel.1.image of type Image</li>
     *     <li>...</li>
     * </ul>
     *
     * The content will be a Map looking like
     *
     * <pre>
     *     {
     *         background: Image
     *         part1: {
     *             title: Text
     *             carousel: [
     *                 {
     *                     image: Image
     *                 },
     *                 {
     *                     image: Image
     *                 }
     *             ]
     *         }
     *     }
     * </pre>
     *
     * Note that for multi lists, the list elements will look like
     * <pre>
     *     {
     *         template: "name of the template"
     *         content: { ... }
     *     }
     * </pre>
     * so that the UI can know what type of section each element of the multi list has
     */
    private final Map<String, Object> content;

    public PageContent(Long id,
                       String name,
                       String modelName,
                       String title,
                       Map<String, Object> content) {
        this.id = id;
        this.name = name;
        this.modelName = modelName;
        this.title = title;
        this.content = content;
    }

    public PageContent(Page page, Map<String, Object> content) {
        this(page.getId(), page.getName(), page.getModelName(), page.getTitle(), content);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getModelName() {
        return modelName;
    }

    public String getTitle() {
        return title;
    }

    public Map<String, Object> getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PageContent)) {
            return false;
        }
        PageContent that = (PageContent) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(modelName, that.modelName) &&
            Objects.equals(title, that.title) &&
            Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, modelName, title, content);
    }

    @Override
    public String toString() {
        return "PageContent{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", modelName='" + modelName + '\'' +
            ", title='" + title + '\'' +
            ", content=" + content +
            '}';
    }
}
