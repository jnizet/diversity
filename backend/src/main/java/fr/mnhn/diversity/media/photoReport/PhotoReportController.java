package fr.mnhn.diversity.media.photoReport;

import fr.mnhn.diversity.common.exception.NotFoundException;
import fr.mnhn.diversity.media.MediaCategoryRepository;
import fr.mnhn.diversity.model.Page;
import fr.mnhn.diversity.model.PageContent;
import fr.mnhn.diversity.model.PageRepository;
import fr.mnhn.diversity.model.PageService;
import java.util.Map;
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
@RequestMapping("/media/report")
public class PhotoReportController {
    private final PageRepository pageRepository;
    private final MediaCategoryRepository mediaCategoryRepository;
    private final PageService pageService;

    public PhotoReportController(PageRepository pageRepository,
        MediaCategoryRepository mediaCategoryRepository,
        PageService pageService) {
        this.pageRepository = pageRepository;
        this.mediaCategoryRepository = mediaCategoryRepository;
        this.pageService = pageService;
    }

    @GetMapping("/{name}")
    public ModelAndView photoReport(@PathVariable("name") String name) {

        Page page = pageRepository.findByNameAndModel(name, PhotoReportModel.PHOTO_REPORT_PAGE_NAME)
            .orElseThrow(NotFoundException::new);

        Map<String, Object> model = Map.of(
            "page", pageService.buildPageContent(PhotoReportModel.PHOTO_REPORT_PAGE_MODEL, page),
            "nextPage", getNextPhotoReport(page.getId()),
            "categories", mediaCategoryRepository.findByPageId(page.getId())
        );

        return new ModelAndView("media/photo-report", model);
    }

    private PageContent getNextPhotoReport(Long currentArticlePageId) {
        return pageRepository
            .findNextOrFirstByModel( PhotoReportModel.PHOTO_REPORT_PAGE_NAME, currentArticlePageId)
            .map(nextOrFirstArticlePage ->  pageService.buildPageContent(PhotoReportModel.PHOTO_REPORT_PAGE_MODEL, nextOrFirstArticlePage))
            .orElse(null);
    }
}
