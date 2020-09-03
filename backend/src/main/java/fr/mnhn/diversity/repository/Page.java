package fr.mnhn.diversity.repository;

import java.util.Collections;
import java.util.List;

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
     * The elements of the page, sorted by their key
     */
    private final List<Element> elements;

    public Page(Long id, String name, String modelName, List<Element> elements) {
        this.id = id;
        this.name = name;
        this.modelName = modelName;
        this.elements = Collections.unmodifiableList(elements);
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

    public List<Element> getElements() {
        return elements;
    }
}
