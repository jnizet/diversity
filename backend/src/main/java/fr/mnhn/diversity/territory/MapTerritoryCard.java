package fr.mnhn.diversity.territory;

import java.util.Objects;

import fr.mnhn.diversity.model.PageContent;

/**
 * A territory card used to render the map shared between the home and the territories page
 */
public class MapTerritoryCard {
    private final Territory territory;
    private final PageContent page;

    public MapTerritoryCard(Territory territory, PageContent page) {
        this.territory = territory;
        this.page = page;
    }

    public Territory getTerritory() {
        return territory;
    }

    public PageContent getPage() {
        return page;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MapTerritoryCard that = (MapTerritoryCard) o;
        return territory == that.territory &&
            Objects.equals(page, that.page);
    }

    @Override
    public int hashCode() {
        return Objects.hash(territory, page);
    }

    @Override
    public String toString() {
        return "MapTerritoryCard{" +
            "territory=" + territory +
            ", page=" + page +
            '}';
    }
}
