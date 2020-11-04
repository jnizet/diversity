package fr.mnhn.diversity.home;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.mnhn.diversity.common.exception.NotFoundException;
import fr.mnhn.diversity.model.Page;
import fr.mnhn.diversity.model.PageContent;
import fr.mnhn.diversity.model.PageRepository;
import fr.mnhn.diversity.model.PageService;
import fr.mnhn.diversity.territory.Territory;
import fr.mnhn.diversity.territory.TerritoryModel;
import fr.mnhn.diversity.territory.Zone;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * The Controller for the home page
 * @author JB Nizet
 */
@Controller
@RequestMapping("/")
@Transactional
public class HomeController {

    private final PageRepository pageRepository;
    private final PageService pageService;

    public HomeController(PageRepository pageRepository, PageService pageService) {
        this.pageRepository = pageRepository;
        this.pageService = pageService;
    }

    @GetMapping
    public ModelAndView home() {
        Page page = pageRepository.findByNameAndModel(HomeModel.HOME_PAGE_NAME, HomeModel.HOME_PAGE_MODEL.getName())
                                  .orElseThrow(NotFoundException::new);
        PageContent pageContent = pageService.buildPageContent(HomeModel.HOME_PAGE_MODEL, page);

        List<TerritoryCard> territoryCards = getTerritoryCards();

        return new ModelAndView(
            "home",
            Map.of(
                "page", pageContent,
                "territoryCards", territoryCards
            )
        );
    }

    private List<TerritoryCard> getTerritoryCards() {
        Map<String, Page> territoryPagesByName =
            pageRepository.findByModel(TerritoryModel.TERRITORY_PAGE_MODEL.getName())
                          .stream()
                          .collect(Collectors.toMap(Page::getName, Function.identity()));

        return Stream.of(Territory.values())
            .filter(territory -> territoryPagesByName.containsKey(territory.getSlug()))
            .map(territory ->
                     new TerritoryCard(
                        territory,
                        pageService.buildPageContent(TerritoryModel.TERRITORY_PAGE_MODEL,
                                                     territoryPagesByName.get(territory.getSlug()))
                     )
            )
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
}
