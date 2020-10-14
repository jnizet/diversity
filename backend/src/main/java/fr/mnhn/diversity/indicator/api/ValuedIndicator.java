package fr.mnhn.diversity.indicator.api;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import fr.mnhn.diversity.indicator.IndicatorValue;
import fr.mnhn.diversity.territory.Territory;

/**
 * The combination of an Indicator data and of its values
 * @author JB Nizet
 */
public final class ValuedIndicator {
    private final IndicatorData data;
    private final Map<Territory, IndicatorValue> values;

    public ValuedIndicator(IndicatorData data,
                           Map<Territory, IndicatorValue> values) {
        this.data = data;
        this.values = Collections.unmodifiableMap(new TreeMap<>(values));
    }

    public IndicatorData getData() {
        return data;
    }

    public Map<Territory, IndicatorValue> getValues() {
        return values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ValuedIndicator)) {
            return false;
        }
        ValuedIndicator that = (ValuedIndicator) o;
        return Objects.equals(data, that.data) &&
            Objects.equals(values, that.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, values);
    }

    @Override
    public String toString() {
        return "ValuedIndicator{" +
            "data=" + data +
            ", values=" + values +
            '}';
    }
}
