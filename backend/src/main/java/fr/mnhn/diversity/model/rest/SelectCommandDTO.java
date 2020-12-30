package fr.mnhn.diversity.model.rest;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class SelectCommandDTO extends ElementCommandDTO {

    @NotBlank
    private final String value;

    @JsonCreator
    public SelectCommandDTO(
        @JsonProperty("key") String key,
        @JsonProperty("value") String value
    ) {
        super(key);
        this.value = value;
    }

    @Override
    public <T> T accept(ElementCommandVisitor<T> visitor) {
        return visitor.visitSelect(this);
    }

    public String getValue() {
        return value;
    }

}
