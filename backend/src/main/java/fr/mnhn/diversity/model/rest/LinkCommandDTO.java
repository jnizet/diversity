package fr.mnhn.diversity.model.rest;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class LinkCommandDTO extends ElementCommandDTO {
    @NotBlank
    private final String text;
    @NotBlank
    private final String href;

    @JsonCreator
    public LinkCommandDTO(
        @JsonProperty("key") String key,
        @JsonProperty("text") String text,
        @JsonProperty("href") String href
    ) {
        super(key);
        this.text = text;
        this.href = href;
    }

    public String getText() {
        return text;
    }

    public String getHref() {
        return href;
    }

    @Override
    public <T> T accept(ElementCommandVisitor<T> visitor) {
        return visitor.visitLink(this);
    }
}
