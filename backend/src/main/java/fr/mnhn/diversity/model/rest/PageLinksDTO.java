package fr.mnhn.diversity.model.rest;

import fr.mnhn.diversity.model.meta.ListElement;
import java.util.List;
import java.util.Objects;

/**
 * The page links, organized by section, as displayed in the admin home page
 */
public final class PageLinksDTO {
    private final List<PageLinkDTO> staticPageLinks;
    private final List<PageLinkDTO> indicatorPageLinks;
    private final List<PageLinkDTO> territoryPageLinks;
    private final List<PageLinkDTO> zonePageLinks;
    private final List<PageLinkDTO> ecogesturePageLinks;
    private final List<PageLinkDTO> interviewPageLinks;
    private final List<PageLinkDTO> articlePageLinks;


    public PageLinksDTO(List<PageLinkDTO> staticPageLinks,
                        List<PageLinkDTO> indicatorPageLinks,
                        List<PageLinkDTO> territoryPageLinks,
                        List<PageLinkDTO> zonePageLinks,
                        List<PageLinkDTO> interviewPageLinks,
                        List<PageLinkDTO> articlePageLinks,
                        List<PageLinkDTO> ecogesturePageLinks) {
        this.staticPageLinks = staticPageLinks;
        this.indicatorPageLinks = indicatorPageLinks;
        this.territoryPageLinks = territoryPageLinks;
        this.zonePageLinks = zonePageLinks;
        this.ecogesturePageLinks = ecogesturePageLinks;
        this.interviewPageLinks = interviewPageLinks;
        this.articlePageLinks = articlePageLinks;

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

    public List<PageLinkDTO> getZonePageLinks() {
        return zonePageLinks;
    }

    public List<PageLinkDTO> getEcogesturePageLinks() {
        return ecogesturePageLinks;
    }

    public List<PageLinkDTO> getInterviewPageLinks() { return  interviewPageLinks; }

    public List<PageLinkDTO> getArticlePageLinks() { return  articlePageLinks; }

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
            Objects.equals(zonePageLinks, that.zonePageLinks) &&
            Objects.equals(interviewPageLinks, that.interviewPageLinks) &&
            Objects.equals(articlePageLinks, that.articlePageLinks) &&
            Objects.equals(ecogesturePageLinks, that.ecogesturePageLinks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(staticPageLinks,
                            indicatorPageLinks,
                            territoryPageLinks,
                            zonePageLinks,
                            interviewPageLinks,
                            articlePageLinks,
                            ecogesturePageLinks);
    }

    @Override
    public String toString() {
        return "PageLinksDTO{" +
            "staticPageLinks=" + staticPageLinks +
            ", indicatorPageLinks=" + indicatorPageLinks +
            ", territoryPageLinks=" + territoryPageLinks +
            ", zonePageLinks=" + zonePageLinks +
            ", interviewPageLinks=" + interviewPageLinks +
            ", articlePageLinks=" + articlePageLinks +
            ", ecogesturePageLinks=" + ecogesturePageLinks +
            '}';
    }
}
