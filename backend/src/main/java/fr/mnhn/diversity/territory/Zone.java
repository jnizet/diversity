package fr.mnhn.diversity.territory;

/**
 * A zone grouping several territories ("bassin" in French)
 */
public enum Zone {
    ANTILLES(
        "Bassin Antillais",
        "antilles",
        new MapCoordinates(1161, 465),
        true
    );

    private final String name;
    private final String slug;
    private final MapCoordinates coordinates;
    private final boolean active;

    Zone(String name, String slug, MapCoordinates coordinates, boolean active) {
        this.name = name;
        this.slug = slug;
        this.coordinates = coordinates;
        this.active = active;
    }

    public String getSlug() {
        return slug;
    }

    public String getName() {
        return name;
    }

    public MapCoordinates getCoordinates() {
        return coordinates;
    }

    public boolean isActive() {
        return active;
    }
}
