package fr.mnhn.diversity.territory;

/**
 * A zone grouping several territories ("bassin" in French)
 */
public enum Zone {
    ANTILLES("Antilles", new MapCoordinates(10, 20)),
    B("Zone B", new MapCoordinates(30, 40)),
    C("Zone C", new MapCoordinates(50, 60));

    private final String name;
    private final MapCoordinates coordinates;

    Zone(String name, MapCoordinates coordinates) {
        this.name = name;
        this.coordinates = coordinates;
    }

    public String getName() {
        return name;
    }

    public MapCoordinates getCoordinates() {
        return coordinates;
    }
}
