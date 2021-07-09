package fr.mnhn.diversity.model.rest;

import fr.mnhn.diversity.model.meta.ListElement;
import fr.mnhn.diversity.model.meta.MultiListTemplateElement;
import java.util.List;

public class MultiListTemplateElementDTO extends PageElementDTO {
    private final List<PageElementDTO> elements;

    public MultiListTemplateElementDTO(MultiListTemplateElement list,
        List<PageElementDTO> elements) {
        super(PageElementType.LIST_UNIT, list);
        this.elements = elements;
    }

    public List<PageElementDTO> getElements() {
        return elements;
    }
}
