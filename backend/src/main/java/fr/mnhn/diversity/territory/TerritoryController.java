package fr.mnhn.diversity.territory;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import fr.mnhn.diversity.common.exception.NotFoundException;
import fr.mnhn.diversity.model.Page;
import fr.mnhn.diversity.model.PageContent;
import fr.mnhn.diversity.model.PageRepository;
import fr.mnhn.diversity.model.PageService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * The Controller for the territory page
 */
@Controller
@RequestMapping("/territoires")
@Transactional
public class TerritoryController {

    private final PageRepository pageRepository;
    private final PageService pageService;
    private final MapService mapService;

    public TerritoryController(PageRepository pageRepository,
                               PageService pageService,
                               MapService mapService) {
        this.pageRepository = pageRepository;
        this.pageService = pageService;
        this.mapService = mapService;
    }

    @GetMapping()
    public ModelAndView home() {
        Page page = pageRepository.findByNameAndModel(TerritoryModel.TERRITORY_HOME_PAGE_NAME, TerritoryModel.TERRITORY_HOME_PAGE_MODEL.getName()).orElseThrow(NotFoundException::new);
        PageContent pageContent = pageService.buildPageContent(TerritoryModel.TERRITORY_HOME_PAGE_MODEL, page);
        List<MapTerritoryCard> territoryCards = mapService.getTerritoryCards();

        return new ModelAndView("territory/territory-home", Map.of(
            "page", pageContent,
            "territoryCards", territoryCards
        ));
    }

    @GetMapping("/{territorySlug}")
    public ModelAndView territory(@PathVariable("territorySlug") String territorySlug) {
        Page page = pageRepository.findByNameAndModel(territorySlug, TerritoryModel.TERRITORY_PAGE_MODEL.getName()).orElseThrow(NotFoundException::new);

        TerritoryCard nextTerritoryCard = getNextTerritoryCard(page.getId()).orElse(null);

        Map<String, Object> model = Map.of(
            "page", pageService.buildPageContent(TerritoryModel.TERRITORY_PAGE_MODEL, page),
            "nextTerritoryCard", nextTerritoryCard == null ? false : nextTerritoryCard
        );
        return new ModelAndView("territory/territory", model);
    }

    private Optional<TerritoryCard> getNextTerritoryCard(Long currentTerritoryPageId) {
        return pageRepository
            .findNextOrFirstByModel(TerritoryModel.TERRITORY_PAGE_MODEL.getName(), currentTerritoryPageId)
            .flatMap(nextOrFirstTerritoryPage ->
                         EnumSet.allOf(Territory.class)
                                .stream()
                                .filter(territory -> nextOrFirstTerritoryPage.getName().equals(territory.getSlug()))
                                .findFirst()
                                .map(territory -> new TerritoryCard(
                                         territory,
                                         pageService.buildPageContent(TerritoryModel.TERRITORY_PAGE_MODEL, nextOrFirstTerritoryPage)
                                     )
                                )
            );
    }

    private static class TerritoryCard {
        private final Territory territory;
        private final PageContent page;

        public TerritoryCard(Territory territory, PageContent page) {
            this.territory = territory;
            this.page = page;
        }

        public Territory getTerritory() {
            return territory;
        }

        public PageContent getPage() {
            return page;
        }
    }
}
