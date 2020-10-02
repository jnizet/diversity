package fr.mnhn.diversity.indicator;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import fr.mnhn.diversity.ecogesture.EcogestureDTO;

/**
 * DTO containing information about an indicator
 */
public final class IndicatorDTO {
    private final Long id;
    private final String biomId;
    private final String slug;
    private final List<IndicatorCategoryDTO> categories;
    private final List<EcogestureDTO> ecogestures;

    public IndicatorDTO(Long id, String biomId, String slug, List<IndicatorCategoryDTO> categories, List<EcogestureDTO> ecogestures) {
        this.id = id;
        this.biomId = biomId;
        this.slug = slug;
        this.categories = categories;
        this.ecogestures = ecogestures;
    }

    public IndicatorDTO(Indicator indicator) {
        this(
            indicator.getId(),
            indicator.getBiomId(),
            indicator.getSlug(),
            indicator.getCategories().stream().map(IndicatorCategoryDTO::new).collect(Collectors.toList()),
            indicator.getEcogestures().stream().map(EcogestureDTO::new).collect(Collectors.toList())
        );
    }

    public Long getId() {
        return id;
    }

    public String getBiomId() {
        return biomId;
    }

    public String getSlug() {
        return slug;
    }

    public List<IndicatorCategoryDTO> getCategories() {
        return categories;
    }

    public List<EcogestureDTO> getEcogestures() {
        return ecogestures;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndicatorDTO that = (IndicatorDTO) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(biomId, that.biomId) &&
            Objects.equals(slug, that.slug) &&
            Objects.equals(categories, that.categories) &&
            Objects.equals(ecogestures, that.ecogestures);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, biomId, slug, categories, ecogestures);
    }

    @Override
    public String toString() {
        return "IndicatorDTO{" +
            "id=" + id +
            ", biomId='" + biomId + '\'' +
            ", slug='" + slug + '\'' +
            ", categories=" + categories +
            ", ecogestures=" + ecogestures +
            '}';
    }
}
