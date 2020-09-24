package fr.mnhn.diversity.indicator;

import java.util.Objects;

import fr.mnhn.diversity.territory.Territory;

/**
 * DTO containing information about an indicator value for a territory
 */
public final class IndicatorValueDTO {
    private final String territory;
    private final double value;
    private final String unit;

    public IndicatorValueDTO(String territory, double value, String unit) {
        this.territory = territory;
        this.value = value;
        this.unit = unit;
    }

    public IndicatorValueDTO(Territory territory, IndicatorValue indicatorValue) {
        this(territory.getName(), indicatorValue.getValue(), indicatorValue.getUnit());
    }

    public String getTerritory() {
        return territory;
    }

    public double getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndicatorValueDTO that = (IndicatorValueDTO) o;
        return Double.compare(that.value, value) == 0 &&
            territory.equals(that.territory) &&
            Objects.equals(unit, that.unit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(territory, value, unit);
    }

    @Override
    public String toString() {
        return "IndicatorValueDTO{" +
            "territory=" + territory +
            ", value=" + value +
            ", unit='" + unit + '\'' +
            '}';
    }
}
