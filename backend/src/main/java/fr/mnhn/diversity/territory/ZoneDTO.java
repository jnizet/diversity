package fr.mnhn.diversity.territory;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

public final class ZoneDTO {
    private final Zone zone;
    private final String text;

    public ZoneDTO(Zone zone, String text) {
        this.zone = zone;
        this.text = text;
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
        return text;
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
        return zone == zoneDTO.zone &&
            Objects.equals(text, zoneDTO.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(zone, text);
    }

    @Override
    public String toString() {
        return "ZoneDTO{" +
            "zone=" + zone +
            ", text='" + text + '\'' +
            '}';
    }
}
