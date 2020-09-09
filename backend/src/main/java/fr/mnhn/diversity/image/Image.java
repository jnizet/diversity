package fr.mnhn.diversity.image;

import java.util.Objects;

/**
 * An image, as stored in the database
 * @author JB Nizet
 */
final class Image {
    private final Long id;
    private final String contentType;
    private final String originalFileName;

    public Image(Long id, String contentType, String originalFileName) {
        this.id = id;
        this.contentType = contentType;
        this.originalFileName = originalFileName;
    }

    public Long getId() {
        return id;
    }

    public String getContentType() {
        return contentType;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Image)) {
            return false;
        }
        Image image = (Image) o;
        return Objects.equals(id, image.id) &&
            Objects.equals(contentType, image.contentType) &&
            Objects.equals(originalFileName, image.originalFileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentType, originalFileName);
    }

    @Override
    public String toString() {
        return "Image{" +
            "id=" + id +
            ", contentType='" + contentType + '\'' +
            ", originalFileName='" + originalFileName + '\'' +
            '}';
    }
}
