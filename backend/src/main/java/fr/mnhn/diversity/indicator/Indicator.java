package fr.mnhn.diversity.indicator;

import java.util.Objects;

/**
 * An indicator, as stored in the database
 */
public final class Indicator {
    private final Long id;
    private final String biomId;

    public Indicator(Long id, String biomId) {
        this.id = id;
        this.biomId = biomId;
    }

    public Long getId() {
        return id;
    }

    public String getBiomId() {
        return biomId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Indicator)) {
            return false;
        }
        Indicator that = (Indicator) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(biomId, that.biomId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, biomId);
    }

    @Override
    public String toString() {
        return "Indicator{" +
            "id='" + id + '\'' +
            ", biomId='" + biomId + '\'' +
            '}';
    }
}
