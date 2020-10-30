package fr.mnhn.diversity.territory;

import java.util.Objects;

public final class TerritoryDTO {
    private final Territory territory;
    private final boolean active;

    public TerritoryDTO(Territory territory, boolean active) {
        this.territory = territory;
        this.active = active;
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

    public boolean isActive() {
        return active;
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
        return active == that.active &&
            territory == that.territory;
    }

    @Override
    public int hashCode() {
        return Objects.hash(territory, active);
    }

    @Override
    public String toString() {
        return "TerritoryDTO{" +
            "territory=" + territory +
            ", active=" + active +
            '}';
    }
}
