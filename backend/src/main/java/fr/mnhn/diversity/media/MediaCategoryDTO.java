package fr.mnhn.diversity.media;

import java.util.Objects;

/**
 * DTO containing information about a category of an indicator
 */
public final class MediaCategoryDTO {
    private final Long id;
    private final String name;

    public MediaCategoryDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public MediaCategoryDTO(MediaCategory mediaCategory) {
        this(mediaCategory.getId(), mediaCategory.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MediaCategoryDTO that = (MediaCategoryDTO) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "MediaCategoryDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            '}';
    }
}
