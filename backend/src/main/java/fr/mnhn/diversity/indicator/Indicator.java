package fr.mnhn.diversity.indicator;

import java.util.List;
import java.util.Objects;

/**
 * An indicator, as stored in the database
 */
public final class Indicator {
    private final Long id;
    private final String biomId;
    private final String slug;
    private final List<IndicatorCategory> categories;

    public Indicator(Long id, String biomId, String slug, List<IndicatorCategory> categories) {
        this.id = id;
        this.biomId = biomId;
        this.slug = slug;
        this.categories = categories;
    }

    public Indicator(Long id, String biomId, String slug) {
        this(id, biomId, slug, List.of());
    }

    public Indicator(String biomId, String slug, List<IndicatorCategory> categories) {
        this(null, biomId, slug, categories);
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
            Objects.equals(categories, indicator.categories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, biomId, slug, categories);
    }

    @Override
    public String toString() {
        return "Indicator{" +
            "id=" + id +
            ", biomId='" + biomId + '\'' +
            ", slug='" + slug + '\'' +
            ", categories=" + categories +
            '}';
    }
}
