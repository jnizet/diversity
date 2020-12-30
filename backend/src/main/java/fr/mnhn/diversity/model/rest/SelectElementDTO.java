package fr.mnhn.diversity.model.rest;

import fr.mnhn.diversity.model.Select;
import fr.mnhn.diversity.model.Text;
import fr.mnhn.diversity.model.meta.SelectElement;
import fr.mnhn.diversity.model.meta.TextElement;
import java.util.Map;

/**
 * DTO representing a select element of the page, with its value
 */
public final class SelectElementDTO extends PageElementDTO {
    // value
    private final Long id;
    private final String value;
    private final Map<String, String> options;

    public SelectElementDTO(SelectElement selectElement, Select selectValue) {
        super(PageElementType.SELECT, selectElement);
        this.id = selectValue.getId();
        this.value = selectValue.getValue();
        this.options = selectElement.getOptions();
    }

    public Long getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public Map<String, String> getOptions() {
        return options;
    }
}
