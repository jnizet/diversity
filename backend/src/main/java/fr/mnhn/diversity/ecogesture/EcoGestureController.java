package fr.mnhn.diversity.ecogesture;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
     * Displays the home page of the eco gestures. It displays a presentation section, and then a gallery
     * of all the eco-gestures.
     */
    @GetMapping()
    public ModelAndView home() {
        Page homePage = pageRepository.findByNameAndModel(EcoGestureModel.ECO_GESTURE_HOME_PAGE_NAME,
                                                          EcoGestureModel.ECO_GESTURE_HOME_PAGE_MODEL.getName())
                                      .orElseThrow(NotFoundException::new);

        // build the content of the home page
        PageContent homePageContent = pageService.buildPageContent(EcoGestureModel.ECO_GESTURE_HOME_PAGE_MODEL, homePage);

        // but the home page also displays a gallery of the presentation names and images of all ecogestures,
        // so we load all the ecogesture pages too.
        // this could be optimized in two ways, but it's probably not worth it
        // - use a page model that contains only the presentation section of the gesture element to avoid populating
        //   the content with useless stuff
        // - only load the actual elements that we need from the database (the presentation section only)
        List<Page> ecoGesturePages =
            pageRepository.findByModel(EcoGestureModel.ECO_GESTURE_PAGE_MODEL.getName());
        List<PageContent> gesturePageContents =
            ecoGesturePages.stream()
                           .map(page -> pageService.buildPageContent(EcoGestureModel.ECO_GESTURE_PAGE_MODEL, page))
                           .collect(Collectors.toList());

        return new ModelAndView("ecogesture/ecogesture-home",
                                Map.of(
                                    "page", homePageContent,
                                    "gesturePages", gesturePageContents
                                )
        );
    }

    /**
     * Displays the details of an eco gesture.
     * @param pageName the name of the page
     */
    @GetMapping("/{pageName}")
    public ModelAndView detail(@PathVariable("pageName") String pageName) {
        Page page = pageRepository.findByNameAndModel(pageName, EcoGestureModel.ECO_GESTURE_PAGE_MODEL.getName())
                                  .orElseThrow(NotFoundException::new);
        return new ModelAndView("ecogesture/ecogesture", "page", pageService.buildPageContent(EcoGestureModel.ECO_GESTURE_PAGE_MODEL, page));
    }
}
