package fr.mnhn.diversity.territory;

import java.util.Objects;

/**
 * The coordinate of a zone or of a territory on the map (in terms of pixels, not in terms of
 * latitude and longitude)
 */
public final class MapCoordinates {
    private final int x;
    private final int y;

    public MapCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MapCoordinates that = (MapCoordinates) o;
        return x == that.x &&
            y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "MapCoordinates{" +
            "x=" + x +
            ", y=" + y +
            '}';
    }
}
