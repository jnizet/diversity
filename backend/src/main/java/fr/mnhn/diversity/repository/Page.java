package fr.mnhn.diversity.repository;

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
     * The elements of the page, indexed by their key
     */
    private final Map<String, Element> elements;

    public Page(Long id, String name, String modelName, Collection<Element> elements) {
        this.id = id;
        this.name = name;
        this.modelName = modelName;
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
            Objects.equals(elements, page.elements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, modelName, elements);
    }

    @Override
    public String toString() {
        return "Page{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", modelName='" + modelName + '\'' +
            ", elements=" + elements +
            '}';
    }
}
