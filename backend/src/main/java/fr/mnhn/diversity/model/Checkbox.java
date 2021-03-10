package fr.mnhn.diversity.model;

import java.util.Map;
import java.util.Objects;

/**
 * A Select in a Page instance
 * @author JB Nizet
 */
public final class Checkbox extends Element {
    /**
     * The text of the element
     */
    private final Boolean value;

    public Checkbox(Long id, String key, Boolean value) {
        super(id, ElementType.CHECKBOX, key);
        this.value = value;
    }

    public Boolean getValue() {
        return value;
    }

    @Override
    public <T> T accept(ElementVisitor<T> visitor) {
        return visitor.visitCheckbox(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Checkbox)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Checkbox checkbox = (Checkbox) o;
        return Objects.equals(value, checkbox.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), value);
    }

    @Override
    public String toString() {
        return "Checkbox{" +
            "value='" + value + '\'' +
            "} " + super.toString();
    }
}
