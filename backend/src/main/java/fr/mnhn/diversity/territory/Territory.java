package fr.mnhn.diversity.territory;

import static fr.mnhn.diversity.territory.Zone.*;

import java.util.Comparator;
import java.util.List;

/**
 * An enum representing all the available territories
 */

public enum Territory {
    OUTRE_MER(
        "Outre-mer",
        "En Outre-mer",
        "Outre-mer",
        null,
        null,
        null
    ),
    GUADELOUPE(
        "Guadeloupe",
        "En Guadeloupe",
        "TER971",
        "guadeloupe",
        new MapCoordinates(1166, 484),
        ANTILLES
    ),
    MARTINIQUE(
        "Martinique",
        "En Martinique",
        "TER972",
        "martinique",
        new MapCoordinates(1167, 489),
        ANTILLES
    ),
    SAINT_MARTIN(
        "Saint-Martin",
        "À Saint-Martin",
        "TER978",
        "saint-martin",
        new MapCoordinates(1156, 473),
        ANTILLES
    ),
    SAINT_BARTHELEMY(
        "Saint-Barthélemy",
        "À Saint-Barthélemy",
        "TER977",
        "saint-barthelemy",
        new MapCoordinates(1158, 474),
        ANTILLES
    ),
    NOUVELLE_CALEDONIE(
        "Nouvelle-Calédonie",
        "En Nouvelle-Calédonie",
        "TER988",
        "nouvelle-caledonie",
        new MapCoordinates(684, 615),
        null
    ),
    POLYNESIE_FRANCAISE(
        "Polynésie française",
        "En Polynésie française",
        "TER987",
        "polynesie",
        new MapCoordinates(855, 597),
        null
    ),
    WALLIS_ET_FUTUNA(
        "Wallis-et-Futuna",
        "À Wallis-et-Futuna",
        "TER986",
        "wallis-et-futuna",
        new MapCoordinates(747, 580),
        null
    ),
    REUNION(
        "La Réunion",
        "À La Réunion",
        "TER974",
        "reunion",
        new MapCoordinates(290, 615),
        null
    ),
    MAYOTTE(
        "Mayotte",
        "À Mayotte",
        "TER976",
        "mayotte",
        new MapCoordinates(254, 585),
        null
    ),
    ILES_SUBANTARCTIQUES(
        "Terres australes françaises",
        "Aux Terres australes françaises",
        "TER984A",
        "iles-subantarctiques",
        new MapCoordinates(367, 687),
        null
    ),
    ILES_EPARSES(
        "Îles Éparses",
        "Aux Îles Éparses",
        "TER984B",
        "iles-eparses",
        new MapCoordinates(237, 621),
        null
    ),
    CLIPPERTON(
        "Clipperton",
        "À Clipperton",
        "TER989",
        "clipperton",
        new MapCoordinates(992, 501),
        null
    ),
    GUYANE(
        "Guyane",
        "En Guyane",
        "TER973",
        "guyane",
        new MapCoordinates(1195, 525),
        null
    ),
    SAINT_PIERRE_ET_MIQUELON(
        "Saint-Pierre-et-Miquelon",
        "À Saint-Pierre-et-Miquelon",
        "TER975",
        "saint-pierre-et-miquelon",
        new MapCoordinates(1181, 348),
        null
    ),
    TERRE_ADELIE(
        "Terre Adélie",
        "En Terre Adélie",
        "TER984C",
        "terre-adelie",
        new MapCoordinates(611, 804),
        null
    );

    /**
     * Name of the territory
     */
    private final String name;

    /**
     * Location of the territory, used when we want to say "En Martinique" or "A Saint-Martin"
     */
    private final String location;

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
              String location,
              String biomKey,
              String slug,
              MapCoordinates coordinates,
              Zone zone) {
        this.name = name;
        this.location = location;
        this.biomKey = biomKey;
        this.slug = slug;
        this.coordinates = coordinates;
        this.zone = zone;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
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
