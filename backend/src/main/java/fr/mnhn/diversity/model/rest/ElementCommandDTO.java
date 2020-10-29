package fr.mnhn.diversity.model.rest;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Base type for the element commands.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = TextCommandDTO.class, name = "TEXT"),
    @JsonSubTypes.Type(value = LinkCommandDTO.class, name = "LINK"),
    @JsonSubTypes.Type(value = ImageCommandDTO.class, name = "IMAGE")
})
public abstract class ElementCommandDTO {
    @NotBlank
    private final String key;

    @JsonCreator
    public ElementCommandDTO(
        @JsonProperty("key") String key
    ) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public abstract <T> T accept(ElementCommandVisitor<T> visitor);
}
