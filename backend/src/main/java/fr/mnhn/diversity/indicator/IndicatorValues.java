package fr.mnhn.diversity.indicator;

import java.util.Map;
import java.util.Objects;

import fr.mnhn.diversity.territory.Territory;

/**
 * The values of an indicator, as stored in the database, for each territory
 */
public final class IndicatorValues {
    private Map<Territory, IndicatorValue> values;

    public IndicatorValues(Map<Territory, IndicatorValue> values) {
        this.values = values;
    }

    public Map<Territory, IndicatorValue> getValues() {
        return values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndicatorValues that = (IndicatorValues) o;
        return Objects.equals(values, that.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(values);
    }

    @Override
    public String toString() {
        return "IndicatorValues{" +
                "values=" + values +
                '}';
    }
}
