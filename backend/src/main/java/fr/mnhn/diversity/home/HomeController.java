package fr.mnhn.diversity.home;

import java.util.List;
import java.util.Map;

import fr.mnhn.diversity.common.exception.NotFoundException;
import fr.mnhn.diversity.model.Page;
import fr.mnhn.diversity.model.PageContent;
import fr.mnhn.diversity.model.PageRepository;
import fr.mnhn.diversity.model.PageService;
import fr.mnhn.diversity.territory.MapService;
import fr.mnhn.diversity.territory.MapTerritoryCard;
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
    private final MapService mapService;

    public HomeController(PageRepository pageRepository,
                          PageService pageService,
                          MapService mapService) {
        this.pageRepository = pageRepository;
        this.pageService = pageService;
        this.mapService = mapService;
    }

    @GetMapping
    public ModelAndView home() {
        Page page = pageRepository.findByNameAndModel(HomeModel.HOME_PAGE_NAME, HomeModel.HOME_PAGE_MODEL.getName())
                                  .orElseThrow(NotFoundException::new);
        PageContent pageContent = pageService.buildPageContent(HomeModel.HOME_PAGE_MODEL, page);

        List<MapTerritoryCard> territoryCards = mapService.getTerritoryCards();

        return new ModelAndView(
            "home",
            Map.of(
                "page", pageContent,
                "territoryCards", territoryCards
            )
        );
    }
}
