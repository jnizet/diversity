package fr.mnhn.diversity.model.rest;

import fr.mnhn.diversity.model.meta.ListElement;
import fr.mnhn.diversity.model.meta.MultiListElement;
import fr.mnhn.diversity.model.meta.MultiListTemplateElement;
import java.util.List;

public class MultiListElementDTO extends PageElementDTO {
    private final List<? extends PageElementDTO> elements;
    private final List<PageElementDTO> templates;

    public MultiListElementDTO(MultiListElement list, List<? extends PageElementDTO> elements, List<PageElementDTO> templates) {
        super(PageElementType.MULTI_LIST, list);
        this.elements = elements;
        this.templates = templates;
    }

    public List<? extends  PageElementDTO> getElements() {
        return elements;
    }

    public List<PageElementDTO> getTemplates() {
        return templates;
    }
}
