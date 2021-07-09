package fr.mnhn.diversity.model.rest;

import fr.mnhn.diversity.model.meta.MultiListElement;
import java.util.List;

import fr.mnhn.diversity.model.meta.ListElement;

public class ListUnitElementDTO extends PageElementDTO {
    private final List<PageElementDTO> elements;

    public ListUnitElementDTO(ListElement list, List<PageElementDTO> elements) {
        super(PageElementType.LIST_UNIT, list);
        this.elements = elements;
    }
    public ListUnitElementDTO(MultiListElement list, List<PageElementDTO> elements) {
        super(PageElementType.LIST_UNIT, list);
        this.elements = elements;
    }

    public List<PageElementDTO> getElements() {
        return elements;
    }
}
