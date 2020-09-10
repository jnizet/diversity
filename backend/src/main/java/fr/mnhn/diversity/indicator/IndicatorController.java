package fr.mnhn.diversity.indicator;

import fr.mnhn.diversity.common.exception.NotFoundException;
import fr.mnhn.diversity.model.Page;
import fr.mnhn.diversity.model.PageRepository;
import fr.mnhn.diversity.model.PageService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * The Controller for the indicator page
 */
@Controller
@RequestMapping("/indicateurs")
@Transactional
public class IndicatorController {

    private final PageRepository pageRepository;
    private final PageService pageService;

    public IndicatorController(PageRepository pageRepository, PageService pageService) {
        this.pageRepository = pageRepository;
        this.pageService = pageService;
    }

    @GetMapping("/{indicatorSlug}")
    public ModelAndView territory(@PathVariable("indicatorSlug") String indicatorSlug) {
        Page page = pageRepository.findByNameAndModel(indicatorSlug, IndicatorModel.INDICATOR_PAGE_MODEL.getName()).orElseThrow(NotFoundException::new);
        return new ModelAndView("indicator", pageService.buildPageContent(IndicatorModel.INDICATOR_PAGE_MODEL, page));
    }

}
