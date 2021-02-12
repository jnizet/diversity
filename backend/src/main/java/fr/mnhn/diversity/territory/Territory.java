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
    GUADELOUPE(
        "Guadeloupe",
        "Guadeloupe",
        "guadeloupe",
        new MapCoordinates(1166, 484),
        ANTILLES
    ),
    MARTINIQUE(
        "Martinique",
        "Martinique",
        "martinique",
        new MapCoordinates(1167, 489),
        ANTILLES
    ),
    SAINT_MARTIN(
        "Saint-Martin",
        "Saint-Martin",
        "saint-martin",
        new MapCoordinates(1156, 473),
        ANTILLES
    ),
    SAINT_BARTHELEMY(
        "Saint-Barthélemy",
        "Saint-Barthélemy",
        "saint-barthelemy",
        new MapCoordinates(1158, 474),
        ANTILLES
    ),
    NOUVELLE_CALEDONIE(
        "Nouvelle-Calédonie",
        "Nouvelle-Calédonie",
        "nouvelle-caledonie",
        new MapCoordinates(684, 615),
        null
    ),
    POLYNESIE_FRANCAISE(
        "Polynésie française",
        "Polynésie française",
        "polynesie",
        new MapCoordinates(855, 597),
        null
    ),
    WALLIS_ET_FUTUNA(
        "Wallis et Futuna",
        "Wallis et Futuna",
        "wallis-et-futuna",
        new MapCoordinates(747, 580),
        null
    ),
    REUNION(
        "La Réunion",
        "Réunion",
        "reunion",
        new MapCoordinates(290, 615),
        null
    ),
    MAYOTTE(
        "Mayotte",
        "Mayotte",
        "mayotte",
        new MapCoordinates(254, 585),
        null
    ),
    ILES_SUBANTARCTIQUES(
        "Îles sub-antarctiques",
        "TAAF : Îles sub-antarctiques",
        "iles-subantarctiques",
        new MapCoordinates(367, 687),
        null
    ),
    ILES_EPARSES(
        "Îles Éparses",
        "TAAF : Îles éparses",
        "iles-eparses",
        new MapCoordinates(237, 621),
        null
    ),
    CLIPPERTON(
        "Clipperton",
        "Île de Clipperton",
        "clipperton",
        new MapCoordinates(992, 501),
        null
    ),
    GUYANE(
        "Guyane",
        "Guyane française",
        "guyane",
        new MapCoordinates(1195, 525),
        null
    ),
    SAINT_PIERRE_ET_MIQUELON(
        "Saint-Pierre-et-Miquelon",
        "Saint-Pierre-et-Miquelon",
        "saint-pierre-et-miquelon",
        new MapCoordinates(1181, 348),
        null
    ),
    TERRE_ADELIE(
        "Terre Adélie",
        "TAAF : Terre-Adélie",
        "terre-adelie",
        new MapCoordinates(611, 804),
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
