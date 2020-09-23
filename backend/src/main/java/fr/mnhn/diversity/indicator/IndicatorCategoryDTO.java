package fr.mnhn.diversity.indicator;

import java.util.Objects;

/**
 * DTO containing information about a category of an indicator
 */
public final class IndicatorCategoryDTO {
    private final Long id;
    private final String name;

    public IndicatorCategoryDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public IndicatorCategoryDTO(IndicatorCategory indicatorCategory) {
        this(indicatorCategory.getId(), indicatorCategory.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndicatorCategoryDTO that = (IndicatorCategoryDTO) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "IndicatorCategoryDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            '}';
    }
}
