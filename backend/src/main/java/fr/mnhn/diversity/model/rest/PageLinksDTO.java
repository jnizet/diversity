package fr.mnhn.diversity.model.rest;

import java.util.List;
import java.util.Objects;

/**
 * The page links, organized by section, as displayed in the admin home page
 */
public final class PageLinksDTO {
    private final List<PageLinkDTO> staticPageLinks;
    private final List<PageLinkDTO> indicatorPageLinks;
    private final List<PageLinkDTO> territoryPageLinks;
    private final List<PageLinkDTO> ecogesturePageLinks;

    public PageLinksDTO(List<PageLinkDTO> staticPageLinks,
                        List<PageLinkDTO> indicatorPageLinks,
                        List<PageLinkDTO> territoryPageLinks,
                        List<PageLinkDTO> ecogesturePageLinks) {
        this.staticPageLinks = staticPageLinks;
        this.indicatorPageLinks = indicatorPageLinks;
        this.territoryPageLinks = territoryPageLinks;
        this.ecogesturePageLinks = ecogesturePageLinks;
    }

    public List<PageLinkDTO> getStaticPageLinks() {
        return staticPageLinks;
    }

    public List<PageLinkDTO> getIndicatorPageLinks() {
        return indicatorPageLinks;
    }

    public List<PageLinkDTO> getTerritoryPageLinks() {
        return territoryPageLinks;
    }

    public List<PageLinkDTO> getEcogesturePageLinks() {
        return ecogesturePageLinks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PageLinksDTO that = (PageLinksDTO) o;
        return Objects.equals(staticPageLinks, that.staticPageLinks) &&
            Objects.equals(indicatorPageLinks, that.indicatorPageLinks) &&
            Objects.equals(territoryPageLinks, that.territoryPageLinks) &&
            Objects.equals(ecogesturePageLinks, that.ecogesturePageLinks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(staticPageLinks, indicatorPageLinks, territoryPageLinks, ecogesturePageLinks);
    }

    @Override
    public String toString() {
        return "PageLinksDTO{" +
            "staticPageLinks=" + staticPageLinks +
            ", indicatorPageLinks=" + indicatorPageLinks +
            ", territoryPageLinks=" + territoryPageLinks +
            ", ecogesturePageLinks=" + ecogesturePageLinks +
            '}';
    }
}
