package fr.mnhn.diversity.model.rest;

import java.util.Objects;

import fr.mnhn.diversity.model.meta.PageElement;

/**
 * DTo representing a page element with its type
 */
public class PageElementDTO {
    private final PageElementType type;
    private final String name;
    private final String description;

    public PageElementDTO(PageElementType type, PageElement element) {
        this.type = type;
        this.name = element.getName();
        this.description = element.getDescription();
    }

    public PageElementType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageElementDTO that = (PageElementDTO) o;
        return type == that.type &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, name, description);
    }

    @Override
    public String toString() {
        return "PageElementDTO{" +
            "type=" + type +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            '}';
    }
}
