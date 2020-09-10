package fr.mnhn.diversity.home;

import fr.mnhn.diversity.common.exception.NotFoundException;
import fr.mnhn.diversity.model.Page;
import fr.mnhn.diversity.model.PageRepository;
import fr.mnhn.diversity.model.PageService;
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
        return new ModelAndView("home", pageService.buildPageContent(HomeModel.HOME_PAGE_MODEL, page));
    }
}
