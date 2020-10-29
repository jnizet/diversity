package fr.mnhn.diversity.territory;

import java.util.Objects;

public final class TerritoryDTO {
    private final Territory territory;

    public TerritoryDTO(Territory territory) {
        this.territory = territory;
    }

    public Territory getTerritory() {
        return territory;
    }

    public String getName() {
        return territory.getName();
    }

    public String getSlug() {
        return territory.getSlug();
    }

    public MapCoordinates getCoordinates() {
        return territory.getCoordinates();
    }

    public Zone getZone() {
        return territory.getZone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TerritoryDTO that = (TerritoryDTO) o;
        return territory == that.territory;
    }

    @Override
    public int hashCode() {
        return Objects.hash(territory);
    }

    @Override
    public String toString() {
        return "TerritoryDTO{" +
            "territory=" + territory +
            '}';
    }
}
