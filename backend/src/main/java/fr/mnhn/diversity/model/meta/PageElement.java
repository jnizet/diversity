package fr.mnhn.diversity.model.meta;

import java.util.Objects;

/**
 * Common base class for all page elements
 * @author JB Nizet
 */
public abstract class PageElement {
    private final String name;
    private final String description;

    public PageElement(String name, String description) {
        Objects.requireNonNull(name, "name may not be null");
        Objects.requireNonNull(description, "description may not be null");
        if (name.isBlank()) {
            throw new IllegalArgumentException("name may not be blank");
        }
        if (description.isBlank()) {
            throw new IllegalArgumentException("description may not be blank");
        }
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public abstract <T> T accept(PageElementVisitor<T> visitor);

    public boolean isLeaf() {
        return !(this instanceof ContainerElement);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PageElement)) {
            return false;
        }
        PageElement that = (PageElement) o;
        return Objects.equals(name, that.name) &&
            Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }

    @Override
    public String toString() {
        return "PageElement{" +
            "name='" + name + '\'' +
            ", description='" + description + '\'' +
            '}';
    }
}
