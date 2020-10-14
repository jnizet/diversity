package fr.mnhn.diversity.image;

import java.util.Objects;

/**
 * The DTO returned after an image has been uploaded
 * @author JB Nizet
 */
public final class ImageDTO {
    private final Long id;
    private final String contentType;
    private final String originalFileName;

    public ImageDTO(Long id, String contentType, String originalFileName) {
        this.id = id;
        this.contentType = contentType;
        this.originalFileName = originalFileName;
    }

    public ImageDTO(Image image) {
        this(image.getId(), image.getContentType(), image.getOriginalFileName());
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
        if (!(o instanceof ImageDTO)) {
            return false;
        }
        ImageDTO imageDTO = (ImageDTO) o;
        return Objects.equals(id, imageDTO.id) &&
            Objects.equals(contentType, imageDTO.contentType) &&
            Objects.equals(originalFileName, imageDTO.originalFileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentType, originalFileName);
    }

    @Override
    public String toString() {
        return "ImageDTO{" +
            "id=" + id +
            ", contentType='" + contentType + '\'' +
            ", originalFileName='" + originalFileName + '\'' +
            '}';
    }
}
