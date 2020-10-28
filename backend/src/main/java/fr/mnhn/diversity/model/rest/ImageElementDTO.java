package fr.mnhn.diversity.model.rest;

import fr.mnhn.diversity.model.Image;
import fr.mnhn.diversity.model.meta.ImageElement;

/**
 * DTO representing an image element of the page, with its value
 */
public final class ImageElementDTO extends PageElementDTO {
    // value
    private final Long id;
    private final Long imageId;
    private final String alt;
    // model
    private final boolean multiSize;
    private final boolean document;

    public ImageElementDTO(ImageElement image, Image value) {
        super(PageElementType.IMAGE, image);
        this.id = value.getId();
        this.imageId = value.getImageId();
        this.alt = value.getAlt();
        this.multiSize = image.isMultiSize();
        this.document = image.isDocument();
    }

    public Long getId() {
        return id;
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

    public boolean isDocument() {
        return document;
    }
}
