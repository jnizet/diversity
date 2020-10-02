package fr.mnhn.diversity.ecogesture;

import java.util.Objects;

/**
 * DTO containing information about an ecogesture
 */
public final class EcogestureDTO {
    private final Long id;
    private final String slug;

    public EcogestureDTO(Long id, String slug) {
        this.id = id;
        this.slug = slug;
    }

    public EcogestureDTO(Ecogesture ecogesture) {
        this(ecogesture.getId(), ecogesture.getSlug());
    }

    public Long getId() {
        return id;
    }

    public String getSlug() {
        return slug;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EcogestureDTO that = (EcogestureDTO) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(slug, that.slug);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, slug);
    }

    @Override
    public String toString() {
        return "EcogestureDTO{" +
            "id=" + id +
            ", slug='" + slug + '\'' +
            '}';
    }
}
