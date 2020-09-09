package fr.mnhn.diversity.model;

import java.util.Objects;

/**
 * A text in a Page instance
 * @author JB Nizet
 */
public final class Text extends Element {
    /**
     * The text of the element
     */
    private final String text;

    public Text(Long id, String key, String text) {
        super(id, ElementType.TEXT, key);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Text)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Text text1 = (Text) o;
        return Objects.equals(text, text1.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), text);
    }

    @Override
    public String toString() {
        return "Text{" +
            "text='" + text + '\'' +
            "} " + super.toString();
    }
}
