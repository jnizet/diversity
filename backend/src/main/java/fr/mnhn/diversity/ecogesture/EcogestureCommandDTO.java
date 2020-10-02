package fr.mnhn.diversity.ecogesture;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Command sent to create/update a ecogesture
 */
public final class EcogestureCommandDTO {
    private final String slug;

    @JsonCreator
    public EcogestureCommandDTO(
        @JsonProperty("slug") String slug
    ) {
        this.slug = slug;
    }

    public String getSlug() {
        return slug;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EcogestureCommandDTO that = (EcogestureCommandDTO) o;
        return Objects.equals(slug, that.slug);
    }

    @Override
    public int hashCode() {
        return Objects.hash(slug);
    }

    @Override
    public String toString() {
        return "EcogestureCommandDTO{" +
            "slug='" + slug + '\'' +
            '}';
    }
}
