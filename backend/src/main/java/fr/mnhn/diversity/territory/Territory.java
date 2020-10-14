package fr.mnhn.diversity.territory;

/**
 * An enum representing all the available territories
 */
public enum Territory {
    OUTRE_MER("Outre-mer", "Outre-mer", null),
    REUNION("Réunion", "La Réunion", "reunion"),
    GUADELOUPE("Guadeloupe", "Guadeloupe", "guadeloupe"),
    MARTINIQUE("Martinique", "Martinique", "martinique"),
    NOUVELLE_CALEDONIE("Nouvelle Calédonie", "Nouvelle-Calédonie", "nouvelle-caledonie"),
    GUYANE("Guyane", "Guyane", "guyane"),
    SAINT_PIERRE_ET_MIQUELON("Saint-Pierre-Et-Miquelon", "Saint-Pierre-et-Miquelon", "st-pierre-et-miquelon");

    /**
     * Name of the territory
     */
    private final String name;

    /**
     * Key representing a territory in the external HTTP services
     */
    private final String biomKey;

    /**
     * Slug identifier used in the URLs for a territory (/territoires/slug)
     */
    private final String slug;

    Territory(String name, String biomKey, String slug) {
        this.name = name;
        this.biomKey = biomKey;
        this.slug = slug;
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

    @Override
    public String toString() {
        return "Territory{" +
                "name='" + name + '\'' +
                ", biomKey='" + biomKey + '\'' +
                ", slug='" + slug + '\'' +
                "} " + super.toString();
    }
}
