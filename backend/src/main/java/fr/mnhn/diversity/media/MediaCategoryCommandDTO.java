package fr.mnhn.diversity.media;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 * Command sent to create/update a category for an indicator
 */
public final class MediaCategoryCommandDTO {
    private final String name;

    @JsonCreator
    public MediaCategoryCommandDTO(
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
        MediaCategoryCommandDTO that = (MediaCategoryCommandDTO) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "MediaCategoryCommandDTO{" +
            "name='" + name + '\'' +
            '}';
    }
}
