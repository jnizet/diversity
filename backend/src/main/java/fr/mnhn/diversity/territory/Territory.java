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
        new MapCoordinates(290, 615),
        null
    ),
    GUADELOUPE(
        "Guadeloupe",
        "Guadeloupe",
        "guadeloupe",
        new MapCoordinates(1167, 488),
        ANTILLES
    ),
    MARTINIQUE(
        "Martinique",
        "Martinique",
        "martinique",
        new MapCoordinates(1166, 483),
        ANTILLES
    ),
    SAINT_MARTIN(
        "Saint-Martin",
        "Saint-Martin", // TODO check this
        "saint-martin",
        new MapCoordinates(1156, 473),
        ANTILLES
    ),
    SAINT_BARTHELEMY(
        "Saint-Barthélemy",
        "Saint-Barthélemy", // TODO check this
        "saint-barthelemy",
        new MapCoordinates(1158, 474),
        ANTILLES
    ),
    NOUVELLE_CALEDONIE(
        "Nouvelle Calédonie",
        "Nouvelle-Calédonie",
        "nouvelle-caledonie",
        new MapCoordinates(684, 615),
        null
    ),
    GUYANE(
        "Guyane",
        "Guyane",
        "guyane",
        new MapCoordinates(1195, 525),
        null
    ),
    SAINT_PIERRE_ET_MIQUELON(
        "Saint-Pierre-Et-Miquelon",
        "Saint-Pierre-et-Miquelon",
        "saint-pierre-et-miquelon",
        new MapCoordinates(1181, 348),
        null
    ),
    POLYNESIE_FRANCAISE(
        "Polynésie Française",
        "Polynésie Française", // TODO check this
        "polynesie",
        new MapCoordinates(855, 597),
        null
    ),
    MAYOTTE(
        "Mayotte",
        "Mayotte", // TODO check this
        "mayotte",
        new MapCoordinates(248, 580),
        null
    ),
    WALLIS_ET_FUTUNA(
        "Wallis et Futuna",
        "Wallis et Futuna", // TODO check this
        "wallis-et-futuna",
        new MapCoordinates(747, 580),
        null
    ),
    CLIPPERTON(
        "Clipperton",
        "Clipperton", // TODO check this
        "clipperton",
        new MapCoordinates(992, 501),
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
