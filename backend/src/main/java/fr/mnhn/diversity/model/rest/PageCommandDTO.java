package fr.mnhn.diversity.model.rest;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Command to create/update a page.
 * Note that it contains a flat list of elements,
 * with their keys already computed ("carousel.slides.0.link").
 */
public final class PageCommandDTO {
    private final String title;
    private final List<ElementCommandDTO> elements;

    @JsonCreator
    public PageCommandDTO(
        @JsonProperty("title") String title,
        @JsonProperty("elements") List<ElementCommandDTO> elements
    ) {
        this.title = title;
        this.elements = elements;
    }

    public String getTitle() {
        return title;
    }

    public List<ElementCommandDTO> getElements() {
        return elements;
    }
}
