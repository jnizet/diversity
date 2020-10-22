package fr.mnhn.diversity.search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import fr.mnhn.diversity.about.AboutModel;
import fr.mnhn.diversity.ecogesture.EcoGestureModel;
import fr.mnhn.diversity.home.HomeModel;
import fr.mnhn.diversity.indicator.IndicatorModel;
import fr.mnhn.diversity.territory.TerritoryModel;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller used to search for pages
 * @author JB Nizet
 */
@Controller
@Transactional
@RequestMapping("/recherche")
public class SearchController {

    private final SearchRepository searchRepository;
    private Map<String, Function<PageSearchResult, String>> urlFactories = new HashMap<>();

    public SearchController(SearchRepository searchRepository) {
        this.searchRepository = searchRepository;

        urlFactories.put(HomeModel.HOME_PAGE_MODEL.getName(), r -> "/");
        urlFactories.put(AboutModel.ABOUT_PAGE_MODEL.getName(), r -> "/apropos");
        urlFactories.put(EcoGestureModel.ECO_GESTURE_HOME_PAGE_MODEL.getName(), r -> "/ecogestes");
        urlFactories.put(EcoGestureModel.ECO_GESTURE_PAGE_MODEL.getName(), r -> "/ecogestes/" + r.getName());
        urlFactories.put(TerritoryModel.TERRITORY_PAGE_MODEL.getName(), r -> "/territoires/" + r.getName());
        urlFactories.put(IndicatorModel.INDICATOR_HOME_PAGE_MODEL.getName(), r -> "/indicateurs");
        urlFactories.put(IndicatorModel.INDICATOR_PAGE_MODEL.getName(), r -> "/indicateurs/" + r.getName());
    }

    @GetMapping
    public ModelAndView search(@RequestParam("texte") String text) {
        List<PageSearchResult> resultsWithourUrl = searchRepository.search(text);
        List<PageSearchResult> results = resultsWithourUrl.stream()
                                                          .map(result -> withUrl(result))
                                                          .collect(Collectors.toList());
        return new ModelAndView("search", Map.of(
            "results", results,
            "query", text
        ));
    }

    private PageSearchResult withUrl(PageSearchResult result) {
        return result.withUrl(urlFactories.get(result.getModelName()).apply(result));
    }
}
