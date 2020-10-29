package fr.mnhn.diversity.model.rest;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Command to create/update a page.
 * Note that it contains a flat list of elements,
 * with their keys already computed ("carousel.slides.0.link").
 */
public final class PageCommandDTO {
    @NotBlank
    private final String title;
    @NotBlank
    private final String name;
    @NotEmpty
    private final List<@Valid ElementCommandDTO> elements;

    @JsonCreator
    public PageCommandDTO(
        @JsonProperty("title") String title,
        @JsonProperty("name") String name,
        @JsonProperty("elements") List<ElementCommandDTO> elements
    ) {
        this.title = title;
        this.name = name;
        this.elements = elements;
    }

    public String getTitle() {
        return title;
    }

    public String getName() {
        return name;
    }

    public List<ElementCommandDTO> getElements() {
        return elements;
    }
}
