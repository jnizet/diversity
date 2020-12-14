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
    private final List<Long> ecogestureIds;
    private final Boolean isRounded;


    @JsonCreator
    public IndicatorCommandDTO(
        @JsonProperty("biomId") String biomId,
        @JsonProperty("slug") String slug,
        @JsonProperty("isRounded") Boolean isRounded,
        @JsonProperty("categoryIds") List<Long> categoryIds,
        @JsonProperty("ecogestureIds") List<Long> ecogestureIds) {
        this.biomId = biomId;
        this.slug = slug;
        this.categoryIds = categoryIds;
        this.ecogestureIds = ecogestureIds;
        this.isRounded = isRounded;
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

    public List<Long> getEcogestureIds() {
        return ecogestureIds;
    }

    public Boolean getRounded() { return isRounded; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndicatorCommandDTO that = (IndicatorCommandDTO) o;
        return Objects.equals(biomId, that.biomId) &&
            Objects.equals(slug, that.slug) &&
            Objects.equals(categoryIds, that.categoryIds) &&
            Objects.equals(ecogestureIds, that.ecogestureIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(biomId, slug, categoryIds, ecogestureIds);
    }

    @Override
    public String toString() {
        return "IndicatorCommandDTO{" +
            "biomId='" + biomId + '\'' +
            ", slug='" + slug + '\'' +
            ", categoryIds=" + categoryIds +
            ", ecogestureIds=" + ecogestureIds +
            '}';
    }
}
