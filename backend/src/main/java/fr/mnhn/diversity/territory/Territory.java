package fr.mnhn.diversity.territory;

import java.util.Objects;

/**
 * A territory
 * @author JB Nizet
 */
public final class Territory {
    private final Long id;
    private final String name;

    public Territory(Long id, String name) {
        this.id = id;
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
        if (this == o) {
            return true;
        }
        if (!(o instanceof Territory)) {
            return false;
        }
        Territory territory = (Territory) o;
        return Objects.equals(id, territory.id) &&
            Objects.equals(name, territory.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Territory{" +
            "id=" + id +
            ", name='" + name + '\'' +
            '}';
    }
}
