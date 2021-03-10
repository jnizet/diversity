package fr.mnhn.diversity.model.rest;

import fr.mnhn.diversity.model.Checkbox;
import fr.mnhn.diversity.model.Select;
import fr.mnhn.diversity.model.Text;
import fr.mnhn.diversity.model.meta.CheckboxElement;
import fr.mnhn.diversity.model.meta.SelectElement;
import fr.mnhn.diversity.model.meta.TextElement;
import java.util.Map;

/**
 * DTO representing a select element of the page, with its value
 */
public final class CheckboxElementDTO extends PageElementDTO {
    // value
    private final Long id;
    private final Boolean value;

    public CheckboxElementDTO(CheckboxElement checkboxElement, Checkbox checkboxValue) {
        super(PageElementType.CHECKBOX, checkboxElement);
        this.id = checkboxValue.getId();
        this.value = checkboxValue.getValue();
    }

    public Long getId() {
        return id;
    }

    public Boolean getValue() {
        return value;
    }
}
