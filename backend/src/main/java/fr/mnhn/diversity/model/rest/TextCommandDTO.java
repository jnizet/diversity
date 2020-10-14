package fr.mnhn.diversity.model.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class TextCommandDTO extends ElementCommandDTO {
    private final String text;

    @JsonCreator
    public TextCommandDTO(
        @JsonProperty("key") String key,
        @JsonProperty("text") String text
    ) {
        super(key);
        this.text = text;
    }

    @Override
    public <T> T accept(ElementCommandVisitor<T> visitor) {
        return visitor.visitText(this);
    }

    public String getText() {
        return text;
    }

}
