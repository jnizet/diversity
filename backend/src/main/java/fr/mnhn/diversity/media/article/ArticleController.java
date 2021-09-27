package fr.mnhn.diversity.media.article;

import fr.mnhn.diversity.media.MediaCategoryRepository;
import java.util.Map;

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
 * Controller used to display "articles" page
 * @author Arnaud Monteils
 */
@Controller
@Transactional
@RequestMapping("/media/article")
public class ArticleController {
    private final PageRepository pageRepository;
    private final MediaCategoryRepository mediaCategoryRepository;
    private final PageService pageService;

    public ArticleController(PageRepository pageRepository,
        MediaCategoryRepository mediaCategoryRepository,
        PageService pageService) {
        this.pageRepository = pageRepository;
        this.mediaCategoryRepository = mediaCategoryRepository;
        this.pageService = pageService;
    }

    @GetMapping("/{name}")
    public ModelAndView article(@PathVariable("name") String name) {

        Page page = pageRepository.findByNameAndModel(name, ArticleModel.ARTICLE_PAGE_NAME)
            .orElseThrow(NotFoundException::new);

        Map<String, Object> model = Map.of(
            "page", pageService.buildPageContent(ArticleModel.ARTICLE_PAGE_MODEL, page),
            "categories", mediaCategoryRepository.findByPageId(page.getId())
        );

        return new ModelAndView("media/article", model);
    }
}
