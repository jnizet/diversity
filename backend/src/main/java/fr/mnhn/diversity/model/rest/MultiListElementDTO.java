package fr.mnhn.diversity.model.rest;

import java.util.List;

import fr.mnhn.diversity.model.meta.MultiListElement;

public class MultiListElementDTO extends PageElementDTO {
    private final List<SectionElementDTO> elements;
    private final List<SectionElementDTO> templates;

    public MultiListElementDTO(MultiListElement multiListElement, List<SectionElementDTO> elements, List<SectionElementDTO> templates) {
        super(PageElementType.MULTI_LIST, multiListElement);
        this.elements = elements;
        this.templates = templates;
    }

    public List<SectionElementDTO> getElements() {
        return elements;
    }

    public List<SectionElementDTO> getTemplates() {
        return templates;
    }
}
