package fr.mnhn.diversity.indicator;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import fr.mnhn.diversity.common.exception.NotFoundException;
import fr.mnhn.diversity.ecogesture.EcogestureModel;
import fr.mnhn.diversity.ecogesture.Ecogesture;
import fr.mnhn.diversity.model.Page;
import fr.mnhn.diversity.model.PageContent;
import fr.mnhn.diversity.model.PageRepository;
import fr.mnhn.diversity.model.PageService;
import fr.mnhn.diversity.territory.Territory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * The Controller for the indicators home page and for the indicator details page
 */
@Controller
@RequestMapping("/indicateurs")
@Transactional
public class IndicatorController {

    private final PageRepository pageRepository;
    private final PageService pageService;
    private final IndicatorRepository indicatorRepository;

    public IndicatorController(PageRepository pageRepository,
                               PageService pageService,
                               IndicatorRepository indicatorRepository
    ) {
        this.pageRepository = pageRepository;
        this.pageService = pageService;
        this.indicatorRepository = indicatorRepository;
    }

    @GetMapping()
    public ModelAndView home() {
        Page page = pageRepository.findByNameAndModel(
            IndicatorModel.INDICATOR_HOME_PAGE_NAME,
            IndicatorModel.INDICATOR_HOME_PAGE_MODEL.getName()
        ).orElseThrow(NotFoundException::new);
        PageContent homePageContent = pageService.buildPageContent(IndicatorModel.INDICATOR_HOME_PAGE_MODEL, page);

        List<IndicatorCard> cards = getIndicatorCards();
        List<IndicatorCategory> categories = getCategories(cards);

        return new ModelAndView("indicator/indicator-home",
                                Map.of(
                                    "page", homePageContent,
                                    "categories", categories,
                                    "cards", cards
                                )
        );
    }

    @GetMapping("/{slug}")
    public ModelAndView indicator(@PathVariable("slug") String slug) {
        Page page = pageRepository.findByNameAndModel(slug, IndicatorModel.INDICATOR_PAGE_MODEL.getName())
                                  .orElseThrow(NotFoundException::new);
        PageContent pageContent = pageService.buildPageContent(IndicatorModel.INDICATOR_PAGE_MODEL, page);

        Indicator indicator = indicatorRepository.findBySlug(slug)
            .orElseThrow(NotFoundException::new);

        Map<Territory, IndicatorValue> valuesByTerritory = indicatorRepository.getValues(indicator);

        IndicatorValue outremerIndicatorValue = valuesByTerritory.get(Territory.OUTRE_MER);
        List<TerritoryCard> territoryCards = getTerritoryCards(valuesByTerritory);

        List<EcogestureCard> ecogestureCards = getEcogestureCards(indicator);

        IndicatorCard nextIndicatorCard = getNextIndicatorCard(page.getId()).orElse(null);

        return new ModelAndView("indicator/indicator",
                                Map.of(
                                    "page", pageContent,
                                    "indicator", indicator,
                                    "outremerIndicatorValue", outremerIndicatorValue,
                                    "territoryCards", territoryCards,
                                    "ecogestureCards", ecogestureCards,
                                    "nextIndicatorCard", nextIndicatorCard == null ? false : nextIndicatorCard
                                ));
    }

    private List<IndicatorCard> getIndicatorCards() {
        // An indicator might already exist in the database, but not have a page yet.
        // so we get all the indicator pages, and find the associated indicator by its name, which is supposed to be
        // the slug of the indicator
        Map<String, Indicator> indicatorsBySlug =
            indicatorRepository.list().stream().collect(Collectors.toMap(Indicator::getSlug, Function.identity()));
        List<Page> indicatorPages = pageRepository.findByModel(IndicatorModel.INDICATOR_PAGE_MODEL.getName());

        Map<Indicator, IndicatorValue> valuesByIndicator =
            indicatorRepository.getValuesForIndicatorsAndTerritory(indicatorsBySlug.values(), Territory.OUTRE_MER);

        return indicatorPages.stream()
                         .filter(indicatorPage -> indicatorsBySlug.containsKey(indicatorPage.getName()))
                         .filter(indicatorPage -> valuesByIndicator.containsKey(indicatorsBySlug.get(indicatorPage.getName())))
                         .map(indicatorPage -> new IndicatorCard(indicatorsBySlug.get(indicatorPage.getName()),
                                                              pageService.buildPageContent(IndicatorModel.INDICATOR_PAGE_MODEL,
                                                                                           indicatorPage),
                                                              valuesByIndicator.get(indicatorsBySlug.get(indicatorPage.getName()))))
                         .collect(Collectors.toList());
    }

    private Optional<IndicatorCard> getNextIndicatorCard(Long currentIndicatorPageId) {
        return pageRepository
            .findNextOrFirstByModel(IndicatorModel.INDICATOR_PAGE_MODEL.getName(), currentIndicatorPageId)
            .flatMap(nextOrFirstIndicatorPage ->
                         indicatorRepository
                             .findBySlug(nextOrFirstIndicatorPage.getName())
                             .flatMap(indicator -> indicatorRepository
                                 .getValueForIndicatorAndTerritory(indicator, Territory.OUTRE_MER)
                                 .map(indicatorValue -> new IndicatorCard(indicator,
                                                                          pageService.buildPageContent(IndicatorModel.INDICATOR_PAGE_MODEL,
                                                                                                       nextOrFirstIndicatorPage),
                                                                          indicatorValue))));
    }

    private List<IndicatorCategory> getCategories(List<IndicatorCard> cards) {
        return cards.stream()
                    .flatMap(card -> card.getIndicator().getCategories().stream())
                    .distinct()
                    .sorted(Comparator.comparing(IndicatorCategory::getName))
                    .collect(Collectors.toList());
    }

    private List<TerritoryCard> getTerritoryCards(Map<Territory, IndicatorValue> valuesByTerritory) {
        return valuesByTerritory.entrySet()
                .stream()
                .filter(entry -> entry.getKey() != Territory.OUTRE_MER)
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> new TerritoryCard(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private List<EcogestureCard> getEcogestureCards(Indicator indicator) {
        return indicator.getEcogestures()
            .stream()
            .map(ecogesture ->
                    pageRepository.findByNameAndModel(ecogesture.getSlug(), EcogestureModel.ECO_GESTURE_PAGE_MODEL.getName())
                                  .map(page ->
                                           new EcogestureCard(
                                              ecogesture,
                                              pageService.buildPageContent(EcogestureModel.ECO_GESTURE_PAGE_MODEL, page)
                                          )
                                  )
            )
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
    }

    private static class IndicatorCard {

        private final Indicator indicator;
        private final PageContent page;
        private final IndicatorValue indicatorValue;

        public IndicatorCard(Indicator indicator,
                             PageContent page,
                             IndicatorValue indicatorValue) {
            this.indicator = indicator;
            this.page = page;
            this.indicatorValue = indicatorValue;
        }

        public Indicator getIndicator() {
            return indicator;
        }

        public PageContent getPage() {
            return page;
        }

        public IndicatorValue getIndicatorValue() {
            return indicatorValue;
        }

        public String getCategoryIdsAsJsonArray() {
            return this.indicator.getCategories().stream().map(category -> category.getId().toString()).collect(Collectors.joining(",", "[", "]"));
        }
    }

    private static class TerritoryCard {
        private final Territory territory;
        private final IndicatorValue indicatorValue;

        public TerritoryCard(Territory territory, IndicatorValue indicatorValue) {
            this.territory = territory;
            this.indicatorValue = indicatorValue;
        }

        public Territory getTerritory() {
            return territory;
        }

        public IndicatorValue getIndicatorValue() {
            return indicatorValue;
        }
    }

    private static class EcogestureCard {
        private final Ecogesture ecogesture;
        private final PageContent page;

        public EcogestureCard(Ecogesture ecogesture, PageContent page) {
            this.ecogesture = ecogesture;
            this.page = page;
        }

        public Ecogesture getEcogesture() {
            return ecogesture;
        }

        public PageContent getPage() {
            return page;
        }
    }
}
