package fr.mnhn.diversity.territory;

import static fr.mnhn.diversity.territory.Zone.*;

/**
 * An enum representing all the available territories
 */
public enum Territory {
    OUTRE_MER(
        "Outre-mer",
        "Outre-mer",
        null,
        null,
        null
    ),
    REUNION(
        "Réunion",
        "La Réunion",
        "reunion",
        new MapCoordinates(12, 43),
        null
    ),
    GUADELOUPE(
        "Guadeloupe",
        "Guadeloupe",
        "guadeloupe",
        new MapCoordinates(46, 12),
        ANTILLES
    ),
    MARTINIQUE(
        "Martinique",
        "Martinique",
        "martinique",
        new MapCoordinates(46, 16),
        ANTILLES
    ),
    NOUVELLE_CALEDONIE(
        "Nouvelle Calédonie",
        "Nouvelle-Calédonie",
        "nouvelle-caledonie",
        new MapCoordinates(432, 324),
        null
    ),
    GUYANE(
        "Guyane",
        "Guyane",
        "guyane",
        new MapCoordinates(46, 256),
        null
    ),
    SAINT_PIERRE_ET_MIQUELON(
        "Saint-Pierre-Et-Miquelon",
        "Saint-Pierre-et-Miquelon",
        "saint-pierre-et-miquelon",
        new MapCoordinates(345, 312),
        null
    );

    /**
     * Name of the territory
     */
    private final String name;

    /**
     * Key representing a territory in the external HTTP services
     */
    private final String biomKey;

    /**
     * Slug identifier used in the URLs for a territory (/territoires/slug). null only for OUTRE_MER
     */
    private final String slug;

    /**
     * The coordinate of the territory on the map (null only for OUTRE_MER)
     */
    private final MapCoordinates coordinates;

    /**
     * The zone which this territory belongs to. Can be null: zome territories are not grouped in zones.
     */
    private final Zone zone;

    Territory(String name,
              String biomKey,
              String slug,
              MapCoordinates coordinates,
              Zone zone) {
        this.name = name;
        this.biomKey = biomKey;
        this.slug = slug;
        this.coordinates = coordinates;
        this.zone = zone;
    }

    public String getName() {
        return name;
    }

    public String getBiomKey() {
        return biomKey;
    }

    public String getSlug() {
        return slug;
    }

    public MapCoordinates getCoordinates() {
        return coordinates;
    }

    public Zone getZone() {
        return zone;
    }
}
