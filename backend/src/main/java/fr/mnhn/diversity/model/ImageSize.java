package fr.mnhn.diversity.model;

/**
 * The various image sizes used when an image is multi-size.
 * The order of this enum is important
 * @author JB Nizet
 */
public enum ImageSize {
    SM(576),
    MD(768),
    LG(1200),
    XL(1900);

    /**
     * The width of the image size, in pixels
     */
    private final int width;

    ImageSize(int width) {
        this.width = width;
    }

    public int getWidth() {
        return width;
    }

    public String getFileNameSuffix() {
        if (this == XL) {
            return "";
        }
        return "-" + this.name().toLowerCase();
    }
}
