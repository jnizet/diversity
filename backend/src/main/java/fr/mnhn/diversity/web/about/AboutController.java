package fr.mnhn.diversity.web.about;

import fr.mnhn.diversity.exception.NotFoundException;
import fr.mnhn.diversity.repository.Page;
import fr.mnhn.diversity.repository.PageRepository;
import fr.mnhn.diversity.service.page.PageService;
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
        Page page = pageRepository.findByName(AboutModel.ABOUT_PAGE_NAME).orElseThrow(NotFoundException::new);
        return new ModelAndView("about", pageService.buildPage(AboutModel.ABOUT_PAGE_MODEL, page));
    }
}
