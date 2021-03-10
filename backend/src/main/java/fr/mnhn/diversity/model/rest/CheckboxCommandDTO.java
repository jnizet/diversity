package fr.mnhn.diversity.model.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class CheckboxCommandDTO extends ElementCommandDTO {

    private final Boolean value;

    @JsonCreator
    public CheckboxCommandDTO(
        @JsonProperty("key") String key,
        @JsonProperty("value") Boolean value
    ) {
        super(key);
        this.value = value != null ? value : false;
    }

    @Override
    public <T> T accept(ElementCommandVisitor<T> visitor) {
        return visitor.visitCheckbox(this);
    }

    public Boolean getValue() {
        return value;
    }

}
