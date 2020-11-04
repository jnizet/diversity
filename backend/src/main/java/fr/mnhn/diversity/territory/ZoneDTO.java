package fr.mnhn.diversity.territory;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

public final class ZoneDTO {
    private final Zone zone;

    public ZoneDTO(Zone zone) {
        this.zone = zone;
    }

    public Zone getZone() {
        return zone;
    }

    public String getName() {
        return zone.getName();
    }

    public MapCoordinates getCoordinates() {
        return zone.getCoordinates();
    }

    public boolean isActive() {
        return zone.isActive();
    }

    public String getText() {
        return zone.getText();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ZoneDTO zoneDTO = (ZoneDTO) o;
        return zone == zoneDTO.zone;
    }

    @Override
    public int hashCode() {
        return Objects.hash(zone);
    }

    @Override
    public String toString() {
        return "ZoneDTO{" +
            "zone=" + zone +
            '}';
    }
}
