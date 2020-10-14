package fr.mnhn.diversity.model.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class ImageCommandDTO extends ElementCommandDTO {
    private final Long imageId;
    private final String alt;
    private final boolean multiSize;

    @JsonCreator
    public ImageCommandDTO(
        @JsonProperty("key") String key,
        @JsonProperty("imageId") Long imageId,
        @JsonProperty("alt") String alt,
        @JsonProperty("multiSize") boolean multiSize
    ) {
        super(key);
        this.imageId = imageId;
        this.alt = alt;
        this.multiSize = multiSize;
    }

    public Long getImageId() {
        return imageId;
    }

    public String getAlt() {
        return alt;
    }

    public boolean isMultiSize() {
        return multiSize;
    }

    @Override
    public <T> T accept(ElementCommandVisitor<T> visitor) {
        return visitor.visitImage(this);
    }
}
