package fr.mnhn.diversity.ecogesture;

import java.util.Objects;

/**
 * An ecogesture, as stored in the database
 * @author JB Nizet
 */
public final class Ecogesture {
    private final Long id;
    private final String slug;

    public Ecogesture(Long id, String slug) {
        this.id = id;
        this.slug = slug;
    }

    public Long getId() {
        return id;
    }

    public String getSlug() {
        return slug;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ecogesture)) {
            return false;
        }
        Ecogesture that = (Ecogesture) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(slug, that.slug);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, slug);
    }

    @Override
    public String toString() {
        return "Ecogesture{" +
            "id=" + id +
            ", slug='" + slug + '\'' +
            '}';
    }
}
