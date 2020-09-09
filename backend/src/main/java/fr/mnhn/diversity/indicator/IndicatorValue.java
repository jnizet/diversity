package fr.mnhn.diversity.indicator;

import java.util.Objects;

/**
 * The value of an indicator
 * @author JB Nizet
 */
public final class IndicatorValue {
    private final double value;
    private final String unit;

    public IndicatorValue(double value, String unit) {
        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IndicatorValue)) {
            return false;
        }
        IndicatorValue that = (IndicatorValue) o;
        return Double.compare(that.value, value) == 0 &&
            Objects.equals(unit, that.unit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, unit);
    }

    @Override
    public String toString() {
        return "IndicatorValue{" +
            "value=" + value +
            ", unit='" + unit + '\'' +
            '}';
    }
}
