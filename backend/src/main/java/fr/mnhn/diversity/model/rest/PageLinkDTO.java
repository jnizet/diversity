package fr.mnhn.diversity.model.rest;

import java.util.Objects;

import fr.mnhn.diversity.model.BasicPage;

/**
 * A link to a page that exist or doesn't exist yet, as shown on the admin home page
 */
public final class PageLinkDTO {
    /**
     * The ID of the page, if it exists
     */
    private final Long id;

    /**
     * The name of the page
     */
    private final String name;

    /**
     * The model name of the page
     */
    private final String modelName;

    /**
     * The title of the page, if it exists
     */
    private final String title;

    public PageLinkDTO(Long id, String name, String modelName, String title) {
        this.id = id;
        this.name = name;
        this.modelName = modelName;
        this.title = title;
    }

    public PageLinkDTO(BasicPage page) {
        this(page.getId(), page.getName(), page.getModelName(), page.getTitle());
    }

    public PageLinkDTO(String name, String modelName) {
        this(null, name, modelName, null);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PageLinkDTO that = (PageLinkDTO) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(modelName, that.modelName) &&
            Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, modelName, title);
    }

    @Override
    public String toString() {
        return "PageLinkDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", modelName='" + modelName + '\'' +
            ", title='" + title + '\'' +
            '}';
    }
}
