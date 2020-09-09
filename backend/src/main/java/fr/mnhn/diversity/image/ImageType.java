package fr.mnhn.diversity.image;

import org.springframework.http.MediaType;

/**
 * Enum class for image types supported by the application, defining the mapping with file extensions
 * @author JB Nizet
 */
public enum ImageType {
    JPG(MediaType.IMAGE_JPEG, "jpg"),
    PNG(MediaType.IMAGE_PNG, "png"),
    GIF(MediaType.IMAGE_GIF, "gif"),
    SVG(MediaType.valueOf("image/svg+xml"), "svg");

    private final MediaType mediaType;
    private final String extension;

    ImageType(MediaType mediaType, String extension) {
        this.mediaType = mediaType;
        this.extension = extension;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public String getExtension() {
        return extension;
    }

    public static ImageType fromContentType(String contentType) {
        MediaType mediaType = MediaType.valueOf(contentType);
        for (ImageType imageType : ImageType.values()) {
            if (imageType.mediaType.equals(mediaType)) {
                return imageType;
            }
        }
        throw new IllegalArgumentException("No image type with content type " + contentType);
    }
}
