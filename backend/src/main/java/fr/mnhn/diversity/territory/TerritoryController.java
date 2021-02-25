package fr.mnhn.diversity.territory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.mnhn.diversity.common.exception.NotFoundException;
import fr.mnhn.diversity.indicator.IndicatorModel;
import fr.mnhn.diversity.indicator.IndicatorRepository;
import fr.mnhn.diversity.indicator.IndicatorValue;
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
    private final IndicatorRepository indicatorRepository;

    public TerritoryController(PageRepository pageRepository,
                               PageService pageService,
                               MapService mapService,
                               IndicatorRepository indicatorRepository) {
        this.pageRepository = pageRepository;
        this.pageService = pageService;
        this.mapService = mapService;
        this.indicatorRepository = indicatorRepository;
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
        Territory territory =
            Stream.of(Territory.values())
                  .filter(t -> territorySlug.equals(t.getSlug()))
                  .findAny()
                  .orElseThrow(NotFoundException::new);
        Page page = pageRepository.findByNameAndModel(territorySlug, TerritoryModel.TERRITORY_PAGE_MODEL.getName()).orElseThrow(NotFoundException::new);

        TerritoryCard nextTerritoryCard = getNextTerritoryCard(page.getId()).orElse(null);

        List<IndicatorCard> indicatorCards = getRandomIndicatorCards(territory);

        Map<String, Object> model = Map.of(
            "territory", territory,
            "page", pageService.buildPageContent(TerritoryModel.TERRITORY_PAGE_MODEL, page),
            "indicatorCards", indicatorCards,
            "nextTerritoryCard", nextTerritoryCard == null ? false : nextTerritoryCard
        );
        Collections.shuffle((List)((PageContent) model.get("page")).getContent().get("species"));
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

    private List<IndicatorCard> getRandomIndicatorCards(Territory territory) {
        List<Page> indicatorPages = pageRepository.findRandomIndicatorPagesForTerritory(2, territory);
        Set<String> indicatorSlugs = indicatorPages.stream().map(Page::getName).collect(Collectors.toSet());
        Map<String, IndicatorValue> outremerValuesByIndicatorSlug =
            indicatorRepository.getValuesForIndicatorSlugsAndTerritory(indicatorSlugs, territory);

        return indicatorPages.stream()
                             .map(page -> pageService.buildPageContent(IndicatorModel.INDICATOR_PAGE_MODEL, page))
                             .map(pageContent -> new IndicatorCard(outremerValuesByIndicatorSlug.get(pageContent.getName()), pageContent))
                             .collect(Collectors.toList());
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

    private static class IndicatorCard {
        private final IndicatorValue indicatorValue;
        private final PageContent page;

        public IndicatorCard(IndicatorValue indicatorValue, PageContent page) {
            this.indicatorValue = indicatorValue;
            this.page = page;
        }

        public IndicatorValue getIndicatorValue() {
            return indicatorValue;
        }

        public PageContent getPage() {
            return page;
        }
    }
}
