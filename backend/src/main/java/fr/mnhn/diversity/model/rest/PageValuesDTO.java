package fr.mnhn.diversity.model.rest;

import java.util.List;

/**
 * A DTO containing the information about a page (its model and its values)
 */
public final class PageValuesDTO {
    private final Long id;
    private final String title;
    private final String name;
    private final String modelName;
    private final String description;
    private final List<PageElementDTO> elements;

    public PageValuesDTO(Long id, String title, String name, String modelName, String description, List<PageElementDTO> elements) {
        this.id = id;
        this.title = title;
        this.name = name;
        this.modelName = modelName;
        this.description = description;
        this.elements = elements;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getName() {
        return name;
    }

    public String getModelName() {
        return modelName;
    }

    public String getDescription() {
        return description;
    }

    public List<PageElementDTO> getElements() {
        return elements;
    }
}
