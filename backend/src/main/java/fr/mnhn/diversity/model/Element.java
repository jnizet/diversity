package fr.mnhn.diversity.model;

import java.util.Objects;

/**
 * An element of a page, as stored in the database
 * @author JB Nizet
 */
public abstract class Element {

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

    protected Element(Long id, ElementType type, String key) {
        this.id = id;
        this.type = type;
        this.key = key;
    }

    public static Text text(Long id, String key, String text) {
        return new Text(id, key, text);
    }

    public static Image image(Long id, String key, Long imageId, String alt) {
        return new Image(id, key, imageId, alt);
    }

    public static Link link(Long id, String key, String text, String href) {
        return new Link(id, key, text, href);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Element)) {
            return false;
        }
        Element element = (Element) o;
        return Objects.equals(id, element.id) &&
            type == element.type &&
            Objects.equals(key, element.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, key);
    }

    @Override
    public String toString() {
        return "Element{" +
            "id=" + id +
            ", type=" + type +
            ", key='" + key + '\'' +
            '}';
    }
}
