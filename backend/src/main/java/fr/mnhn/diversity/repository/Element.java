package fr.mnhn.diversity.repository;

/**
 * An element of a page, as stored in the database
 * @author JB Nizet
 */
public class Element {

    /**
     * The technical ID of the element
     */
    private final Long id;

    /**
     * The type of the element
     */
    private final ElementType type;

    /**
     * The key of the element, identifying its position in the page structure
     */
    private final String key;

    /**
     * The text of the element, for elements of type TEXT and LINK. null otherwise
     */
    private final String text;

    /**
     * The image ID of the IMAGE element, null otherwise
     */
    private final String imageId;

    /**
     * The alt of the IMAGE element, null otherwise
     */
    private final String alt;

    /**
     * The href of the LINK element, null otherwise
     */
    private final String href;

    private Element(Long id, ElementType type, String key, String text, String imageId, String alt, String href) {
        this.id = id;
        this.type = type;
        this.key = key;
        this.text = text;
        this.imageId = imageId;
        this.alt = alt;
        this.href = href;
    }

    public static Element text(Long id, String key, String text) {
        return new Element(id, ElementType.TEXT, key, text, null, null, null);
    }

    public static Element image(Long id, String key, String imageId, String alt) {
        return new Element(id, ElementType.IMAGE, key, null, imageId, alt, null);
    }

    public static Element link(Long id, String key, String text, String href) {
        return new Element(id, ElementType.LINK, key, text, null, null, href);
    }

    public Long getId() {
        return id;
    }

    public ElementType getType() {
        return type;
    }

    public String getKey() {
        return key;
    }

    public String getText() {
        return text;
    }

    public String getImageId() {
        return imageId;
    }

    public String getAlt() {
        return alt;
    }

    public String getHref() {
        return href;
    }
}
