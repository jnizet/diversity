package fr.mnhn.diversity.ecogesture;

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
 * Controller used to display the eco-gesture details pages and the eco-gestures home page
 * @author JB Nizet
 */
@Controller
@Transactional
@RequestMapping("/ecogestes")
public class EcoGestureController {
    private final PageRepository pageRepository;
    private final PageService pageService;

    public EcoGestureController(PageRepository pageRepository, PageService pageService) {
        this.pageRepository = pageRepository;
        this.pageService = pageService;
    }

    /**
     * Displays the details of an eco gesture.
     * @param pageName the name of the page
     */
    @GetMapping("/{pageName}")
    public ModelAndView detail(@PathVariable("pageName") String pageName) {
        Page page = pageRepository.findByNameAndModel(pageName, EcoGestureModel.ECO_GESTURE_PAGE_MODEL.getName())
                                  .orElseThrow(NotFoundException::new);
        return new ModelAndView("ecogesture/ecogesture", pageService.buildPage(EcoGestureModel.ECO_GESTURE_PAGE_MODEL, page));
    }
}
