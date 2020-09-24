package fr.mnhn.diversity.indicator;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Command sent to create/update an indicator
 */
public final class IndicatorCommandDTO {
    private final String biomId;
    private final String slug;
    private final List<Long> categoryIds;

    @JsonCreator
    public IndicatorCommandDTO(
        @JsonProperty("biomId") String biomId,
        @JsonProperty("slug") String slug,
        @JsonProperty("categoryIds") List<Long> categoryIds
    ) {
        this.biomId = biomId;
        this.slug = slug;
        this.categoryIds = categoryIds;
    }

    public String getBiomId() {
        return biomId;
    }

    public String getSlug() {
        return slug;
    }

    public List<Long> getCategoryIds() {
        return categoryIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndicatorCommandDTO that = (IndicatorCommandDTO) o;
        return Objects.equals(biomId, that.biomId) &&
            Objects.equals(slug, that.slug) &&
            Objects.equals(categoryIds, that.categoryIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(biomId, slug, categoryIds);
    }

    @Override
    public String toString() {
        return "IndicatorCommandDTO{" +
            "biomId='" + biomId + '\'' +
            ", slug='" + slug + '\'' +
            ", categoryIds=" + categoryIds +
            '}';
    }
}
