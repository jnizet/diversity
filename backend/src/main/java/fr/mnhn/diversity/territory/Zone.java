package fr.mnhn.diversity.territory;

/**
 * A zone grouping several territories ("bassin" in French)
 */
public enum Zone {
    ANTILLES(
        "Bassin Antillais",
        new MapCoordinates(1161, 465),
        true,
        "Les Antilles sont un « point chaud » de la biodiversité mondiale." +
            " Si chacun des quatre territoires français du bassin a son identité et ses spécificités,"
            + " les acteurs locaux collaborent autour de problématiques partagées."
    ),
    ILES_AUSTRALES(
        "Îles australes",
        new MapCoordinates(357, 749),
        false,
        null
    );

    private final String name;
    private final MapCoordinates coordinates;
    private final boolean active;
    private final String text;

    // TODO externalize text somehow
    Zone(String name, MapCoordinates coordinates, boolean active, String text) {
        this.name = name;
        this.coordinates = coordinates;
        this.active = active;
        this.text = text;
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

    public String getText() {
        return text;
    }
}
