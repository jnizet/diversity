package fr.mnhn.diversity.model.rest;

import java.util.List;

import fr.mnhn.diversity.model.meta.ListElement;

public final class ListUnitElementDTO extends PageElementDTO {
    private final List<PageElementDTO> elements;

    public ListUnitElementDTO(ListElement list, List<PageElementDTO> elements) {
        super(PageElementType.LIST_UNIT, list);
        this.elements = elements;
    }

    public List<PageElementDTO> getElements() {
        return elements;
    }
}
