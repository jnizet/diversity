package fr.mnhn.diversity.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A page, with its elements, as stored in the database
 * @author JB Nizet
 */
public final class Page {
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

    /**
     * The elements of the page, indexed by their key
     */
    private final Map<String, Element> elements;

    public Page(Long id, String name, String modelName, String title, Collection<Element> elements) {
        this.id = id;
        this.name = name;
        this.modelName = modelName;
        this.title = title;
        this.elements = Collections.unmodifiableMap(
            elements.stream()
                    .collect(Collectors.toMap(Element::getKey, Function.identity()))
        );
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

    public Map<String, Element> getElements() {
        return elements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Page)) {
            return false;
        }
        Page page = (Page) o;
        return Objects.equals(id, page.id) &&
            Objects.equals(name, page.name) &&
            Objects.equals(modelName, page.modelName) &&
            Objects.equals(title, page.title) &&
            Objects.equals(elements, page.elements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, modelName, title, elements);
    }

    @Override
    public String toString() {
        return "Page{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", modelName='" + modelName + '\'' +
            ", title='" + title + '\'' +
            ", elements=" + elements +
            '}';
    }
}
