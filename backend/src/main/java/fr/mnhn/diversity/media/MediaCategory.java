package fr.mnhn.diversity.media;

import fr.mnhn.diversity.indicator.Indicator;
import java.util.Objects;

/**
 * A category of an {@link Indicator}, as stored in the database
 */
public final class MediaCategory {
    private final Long id;
    private final String name;

    public MediaCategory(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public MediaCategory(String name) {
        this.id = null;
        this.name = name;
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
        MediaCategory category = (MediaCategory) o;
        return Objects.equals(id, category.id) &&
                Objects.equals(name, category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
