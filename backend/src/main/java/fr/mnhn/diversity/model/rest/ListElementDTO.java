package fr.mnhn.diversity.model.rest;

import java.util.List;

import fr.mnhn.diversity.model.meta.ListElement;

/**
 * DTO representing a list of the page, with its elements as list units (group of elements)
 */
public final class ListElementDTO extends PageElementDTO {
    private final List<ListUnitElementDTO> elements;

    public ListElementDTO(ListElement list, List<ListUnitElementDTO> elements) {
        super(PageElementType.LIST, list);
        this.elements = elements;
    }

    public List<ListUnitElementDTO> getElements() {
        return elements;
    }
}
