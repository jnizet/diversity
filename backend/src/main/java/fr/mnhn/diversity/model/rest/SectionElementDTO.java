package fr.mnhn.diversity.model.rest;

import java.util.List;

import fr.mnhn.diversity.model.meta.SectionElement;

/**
 * DTO representing a section of the page, with its elements
 */
public final class SectionElementDTO extends PageElementDTO {
    private final List<PageElementDTO> elements;

    public SectionElementDTO(SectionElement section, List<PageElementDTO> elements) {
        super(PageElementType.SECTION, section);
        this.elements = elements;
    }

    public List<PageElementDTO> getElements() {
        return elements;
    }
}
