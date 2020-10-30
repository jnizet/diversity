package fr.mnhn.diversity.territory;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A JSON DTO containing all the information about the map: zones and territories
 */
public final class MapDTO {

    private final List<ZoneDTO> zones;
    private final List<TerritoryDTO> territories;

    public MapDTO(List<ZoneDTO> zones, List<TerritoryDTO> territories) {
        this.zones = zones;
        this.territories = territories;
    }

    public List<ZoneDTO> getZones() {
        return zones;
    }

    public List<TerritoryDTO> getTerritories() {
        return territories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MapDTO mapDTO = (MapDTO) o;
        return Objects.equals(zones, mapDTO.zones) &&
            Objects.equals(territories, mapDTO.territories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(zones, territories);
    }

    @Override
    public String toString() {
        return "MapDTO{" +
            "zones=" + zones +
            ", territories=" + territories +
            '}';
    }
}
