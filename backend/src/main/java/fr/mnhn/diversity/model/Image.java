package fr.mnhn.diversity.model;

import java.util.Objects;

/**
 * An image of a Page instance
 * @author JB Nizet
 */
public final class Image extends Element {

    /**
     * The image ID of the element
     */
    private final Long imageId;

    /**
     * The alt of the element
     */
    private final String alt;

    /**
     * Is the image multisize
     */
    private final boolean multiSize;

    public Image(Long id, String key, Long imageId, String alt, boolean multiSize) {
        super(id, ElementType.IMAGE, key);
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

    /**
     * Creates a copy of this image with the given multiSize value
     */
    public Image withMultiSize(boolean multiSize) {
        return new Image(this.getId(), this.getKey(), this.imageId, this.alt, multiSize);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Image)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Image image = (Image) o;
        return multiSize == image.multiSize &&
            Objects.equals(imageId, image.imageId) &&
            Objects.equals(alt, image.alt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), imageId, alt, multiSize);
    }

    @Override
    public String toString() {
        return "Image{" +
            "imageId=" + imageId +
            ", alt='" + alt + '\'' +
            ", multiSize=" + multiSize +
            "} " + super.toString();
    }
}
