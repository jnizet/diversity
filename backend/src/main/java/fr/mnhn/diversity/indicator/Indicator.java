package fr.mnhn.diversity.indicator;

import java.util.List;
import java.util.Objects;

/**
 * An indicator, as stored in the database
 */
public final class Indicator {
    private final Long id;
    private final String biomId;
    private final List<IndicatorCategory> categories;

    public Indicator(Long id, String biomId, List<IndicatorCategory> categories) {
        this.id = id;
        this.biomId = biomId;
        this.categories = categories;
    }

    public Indicator(Long id, String biomId) {
        this(id, biomId, List.of());
    }

    public Long getId() {
        return id;
    }

    public String getBiomId() {
        return biomId;
    }

    public List<IndicatorCategory> getCategories() {
        return categories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Indicator indicator = (Indicator) o;
        return Objects.equals(id, indicator.id) &&
                Objects.equals(biomId, indicator.biomId) &&
                Objects.equals(categories, indicator.categories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, biomId, categories);
    }

    @Override
    public String toString() {
        return "Indicator{" +
                "id=" + id +
                ", biomId='" + biomId + '\'' +
                ", categories=" + categories +
                '}';
    }
}
