package fr.mnhn.diversity.search;

import static fr.mnhn.diversity.common.PageModels.ALL_PAGE_MODELS_BY_NAME;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fr.mnhn.diversity.model.meta.PageModel;
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

    public SearchController(SearchRepository searchRepository) {
        this.searchRepository = searchRepository;
    }

    @GetMapping
    public ModelAndView search(@RequestParam("texte") String text) {
        List<PageSearchResult> resultsWithoutUrl = searchRepository.search(text);
        List<PageSearchResult> results = resultsWithoutUrl.stream()
                                                          .map(this::withPath)
                                                          .filter(result -> result.getPath() != null)
                                                          .collect(Collectors.toList());
        return new ModelAndView("search", Map.of(
            "results", results,
            "query", text
        ));
    }

    /**
     * Returns a copy of the given result with its path, deduced from its page model name
     */
    private PageSearchResult withPath(PageSearchResult result) {
        PageModel pageModel = ALL_PAGE_MODELS_BY_NAME.get(result.getModelName());
        String path = pageModel == null ? null : pageModel.toPath(result.getName()).orElse(null);
        return result.withPath(path);
    }
}
