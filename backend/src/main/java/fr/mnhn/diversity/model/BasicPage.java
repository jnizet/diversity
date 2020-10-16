package fr.mnhn.diversity.model;

import java.util.Objects;

/**
 * A page, without its elements, as stored in the database
 * @author JB Nizet
 */
public final class BasicPage {
    /**
     * The technical ID of the page
     */
    private final Long id;

    /**
     * The name of the page, as chosen by the creator of the page
     */
    private final String name;

    /**
     * The name of the page model that this page is based on
     */
    private final String modelName;

    /**
     * The title of the page, displayed in the browser tab, and also displayed to
     * as a search result
     */
    private final String title;

    public BasicPage(Long id, String name, String modelName, String title) {
        this.id = id;
        this.name = name;
        this.modelName = modelName;
        this.title = title;
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
        if (!(o instanceof BasicPage)) {
            return false;
        }
        BasicPage page = (BasicPage) o;
        return Objects.equals(id, page.id) &&
            Objects.equals(name, page.name) &&
            Objects.equals(modelName, page.modelName) &&
            Objects.equals(title, page.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, modelName, title);
    }

    @Override
    public String toString() {
        return "BasicPage{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", modelName='" + modelName + '\'' +
            ", title='" + title + '\'' +
            '}';
    }
}
