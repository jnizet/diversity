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

    public Image(Long id, String key, Long imageId, String alt) {
        super(id, ElementType.IMAGE, key);
        this.imageId = imageId;
        this.alt = alt;
    }

    public Long getImageId() {
        return imageId;
    }

    public String getAlt() {
        return alt;
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
        return Objects.equals(imageId, image.imageId) &&
            Objects.equals(alt, image.alt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), imageId, alt);
    }

    @Override
    public String toString() {
        return "Image{" +
            "imageId='" + imageId + '\'' +
            ", alt='" + alt + '\'' +
            "} " + super.toString();
    }
}
