package fr.mnhn.diversity.territory;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.mnhn.diversity.model.BasicPage;
import fr.mnhn.diversity.model.Page;
import fr.mnhn.diversity.model.PageContent;
import fr.mnhn.diversity.model.PageRepository;
import fr.mnhn.diversity.model.PageService;
import fr.mnhn.diversity.model.Text;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/map")
@Transactional
public class MapRestController {

    private final PageRepository pageRepository;
    private final PageService pageService;

    public MapRestController(PageRepository pageRepository, PageService pageService) {
        this.pageRepository = pageRepository;
        this.pageService = pageService;
    }

    @GetMapping
    public MapDTO get() {
        return new MapDTO(getZones(), getTerritories());
    }

    private List<ZoneDTO> getZones() {
        Map<String, PageContent> zonePagesByZoneSlug =
            pageRepository.findByModel(TerritoryModel.ZONE_PAGE_MODEL.getName())
                          .stream()
                          .collect(Collectors.toMap(Page::getName,
                                                    page -> pageService.buildPageContent(TerritoryModel.ZONE_PAGE_MODEL, page)));
        return Stream.of(Zone.values())
            .map(zone -> new ZoneDTO(zone, getZoneText(zone, zonePagesByZoneSlug)))
            .collect(Collectors.toList());
    }

    private String getZoneText(Zone zone, Map<String, PageContent> zonePagesByZoneSlug) {
        PageContent pageContent = zonePagesByZoneSlug.get(zone.getSlug());
        if (pageContent == null) {
            return null;
        }
        return ((Text) pageContent.getContent().get("description")).getText();
    }

    private List<TerritoryDTO> getTerritories() {
        Set<String> activeTerritorySlugs =
            pageRepository.findBasicByModel(TerritoryModel.TERRITORY_PAGE_MODEL.getName())
                          .stream()
                          .map(BasicPage::getName)
                          .collect(Collectors.toSet());

        return Stream.of(Territory.values())
                     .filter(t -> t != Territory.OUTRE_MER)
                     .map(t -> new TerritoryDTO(t, activeTerritorySlugs.contains(t.getSlug())))
                     .collect(Collectors.toList());
    }
}
