package fr.mnhn.diversity.model;

import java.util.Map;
import java.util.Objects;

/**
 * A Select in a Page instance
 * @author JB Nizet
 */
public final class Select extends Element {
    /**
     * The text of the element
     */
    private final String value;

    public Select(Long id, String key, String value) {
        super(id, ElementType.SELECT, key);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public <T> T accept(ElementVisitor<T> visitor) {
        return visitor.visitSelect(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Select)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Select text1 = (Select) o;
        return Objects.equals(value, text1.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), value);
    }

    @Override
    public String toString() {
        return "Select{" +
            "value='" + value + '\'' +
            "} " + super.toString();
    }
}
