package fr.mnhn.diversity.indicator.api;

import java.util.Objects;

/**
 * The data returns by the service used to get the indicator name and computation UDL
 * @author JB Nizet
 */
public final class IndicatorData {
    private final String id;
    private final String shortLabel;
    private final String calculationReference;

    public IndicatorData(String id, String shortLabel, String calculationReference) {
        this.id = id;
        this.shortLabel = shortLabel;
        this.calculationReference = calculationReference;
    }

    public String getId() {
        return id;
    }

    public String getShortLabel() {
        return shortLabel;
    }

    public String getCalculationReference() {
        return calculationReference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IndicatorData)) {
            return false;
        }
        IndicatorData that = (IndicatorData) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(shortLabel, that.shortLabel) &&
                Objects.equals(calculationReference, that.calculationReference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, shortLabel, calculationReference);
    }

    @Override
    public String toString() {
        return "IndicatorData{" +
                "id='" + id + '\'' +
                ", shortLabel='" + shortLabel + '\'' +
                ", calculationReference='" + calculationReference + '\'' +
                '}';
    }
}
