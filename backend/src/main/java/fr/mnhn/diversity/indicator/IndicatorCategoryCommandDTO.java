package fr.mnhn.diversity.indicator;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Command sent to create/update a category for an indicator
 */
public final class IndicatorCategoryCommandDTO {
    private final String name;

    @JsonCreator
    public IndicatorCategoryCommandDTO(
        @JsonProperty("name")  String name
    ) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndicatorCategoryCommandDTO that = (IndicatorCategoryCommandDTO) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "IndicatorCategoryCommandDTO{" +
            "name='" + name + '\'' +
            '}';
    }
}
