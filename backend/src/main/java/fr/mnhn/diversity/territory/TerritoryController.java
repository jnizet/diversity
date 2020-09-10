package fr.mnhn.diversity.territory;

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
 * The Controller for the territory page
 */
@Controller
@RequestMapping("/territoires")
@Transactional
public class TerritoryController {

    private final PageRepository pageRepository;
    private final PageService pageService;

    public TerritoryController(PageRepository pageRepository, PageService pageService) {
        this.pageRepository = pageRepository;
        this.pageService = pageService;
    }

    @GetMapping("/{territorySlug}")
    public ModelAndView territory(@PathVariable("territorySlug") String territorySlug) {
        Page page = pageRepository.findByNameAndModel(territorySlug, TerritoryModel.TERRITORY_PAGE_MODEL.getName()).orElseThrow(NotFoundException::new);
        return new ModelAndView("territory", "page", pageService.buildPageContent(TerritoryModel.TERRITORY_PAGE_MODEL, page));
    }

}
