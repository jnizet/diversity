package fr.mnhn.diversity.indicator;

import java.util.List;
import java.util.Objects;

import fr.mnhn.diversity.ecogesture.Ecogesture;

/**
 * An indicator, as stored in the database
 */
public final class Indicator {
    private final Long id;
    private final String biomId;
    private final String slug;
    private final Boolean isRounded;
    private final List<IndicatorCategory> categories;
    private final List<Ecogesture> ecogestures;

    public Indicator(Long id, String biomId, String slug, Boolean isRounded,
        List<IndicatorCategory> categories, List<Ecogesture> ecogestures) {
        this.id = id;
        this.biomId = biomId;
        this.slug = slug;
        this.isRounded = isRounded;
        this.categories = categories;
        this.ecogestures = ecogestures;
    }

    public Indicator(Long id, String biomId, String slug, Boolean isRounded) {
        this(id, biomId, slug, isRounded, List.of(), List.of());
    }

    public Indicator(String biomId, String slug, Boolean isRounded, List<IndicatorCategory> categories, List<Ecogesture> ecogestures) {
        this(null, biomId, slug, isRounded, categories, ecogestures);
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

    public List<IndicatorCategory> getCategories() {
        return categories;
    }

    public List<Ecogesture> getEcogestures() {
        return ecogestures;
    }

    public Boolean getIsRounded() { return isRounded; }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Indicator)) {
            return false;
        }
        Indicator indicator = (Indicator) o;
        return Objects.equals(id, indicator.id) &&
            Objects.equals(biomId, indicator.biomId) &&
            Objects.equals(slug, indicator.slug) &&
            Objects.equals(categories, indicator.categories) &&
            Objects.equals(ecogestures, indicator.ecogestures) &&
            Objects.equals(isRounded, indicator.isRounded);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, biomId, slug, categories, ecogestures, isRounded);
    }

    @Override
    public String toString() {
        return "Indicator{" +
            "id=" + id +
            ", biomId='" + biomId + '\'' +
            ", slug='" + slug + '\'' +
            ", categories=" + categories +
            ", ecogestures=" + ecogestures +
            ", isRounded=" + isRounded +
            '}';
    }
}
