package fr.mnhn.diversity.territory;

import java.util.Objects;

/**
 * A territory
 * @author JB Nizet
 */
public final class Territory {
    private final Long id;
    private final String name;
    /**
     * Field used as a functional ID and in the URLS,
     * for example `/territoires/st-pierre-et-miquelon`
     */
    private final String slug;

    public Territory(Long id, String name, String slug) {
        this.id = id;
        this.name = name;
        this.slug = slug;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Territory)) {
            return false;
        }
        Territory territory = (Territory) o;
        return Objects.equals(id, territory.id) &&
            Objects.equals(name, territory.name)&&
            Objects.equals(slug, territory.slug);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, slug);
    }

    @Override
    public String toString() {
        return "Territory{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", slug='" + slug + '\'' +
            '}';
    }
}
