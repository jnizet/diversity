package fr.mnhn.diversity.about;

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
 * The controller used to display the About page
 * @author JB Nizet
 */
@Controller
@RequestMapping("/apropos")
@Transactional
public class AboutController {
    private final PageRepository pageRepository;
    private final PageService pageService;

    public AboutController(PageRepository pageRepository, PageService pageService) {
        this.pageRepository = pageRepository;
        this.pageService = pageService;
    }

    @GetMapping
    public ModelAndView home() {
        Page page = pageRepository.findByNameAndModel(AboutModel.ABOUT_PAGE_NAME, AboutModel.ABOUT_PAGE_MODEL.getName())
                                  .orElseThrow(NotFoundException::new);
        return new ModelAndView("about", pageService.buildPageContent(AboutModel.ABOUT_PAGE_MODEL, page));
    }
}
