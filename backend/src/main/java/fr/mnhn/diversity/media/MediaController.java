package fr.mnhn.diversity.media;

import fr.mnhn.diversity.media.article.ArticleModel;
import fr.mnhn.diversity.media.interview.InterviewModel;
import fr.mnhn.diversity.media.photoReport.PhotoReportModel;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller used to display the "" page, displaying two random ecogestures
 * and a link to the participative sciences page
 * @author JB Nizet
 */
@Controller
@Transactional
@RequestMapping("/media")
public class MediaController {
    private final PageRepository pageRepository;
    private final MediaCategoryRepository mediaCategoryRepository;
    private final PageService pageService;

    public MediaController(PageRepository pageRepository,
        PageService pageService,
        MediaCategoryRepository mediaCategoryRepository
       ) {
        this.pageRepository = pageRepository;
        this.mediaCategoryRepository = mediaCategoryRepository;
        this.pageService = pageService;
    }


    @GetMapping
    public ModelAndView media() {

        Page page = pageRepository.findByNameAndModel(MediaModel.MEDIA_PAGE_NAME, MediaModel.MEDIA_PAGE_MODEL.getName())
            .orElseThrow(NotFoundException::new);
        PageContent pageContent = pageService.buildPageContent(MediaModel.MEDIA_PAGE_MODEL, page);

        return new ModelAndView("media/medias", Map.of(
            "page", pageContent,
            "articles", getArticlesContent(),
            "interviews", getInterviewsContent(),
            "reports", getReportsContent(),
            "categories", getMediaCategories()
        ));
    }

    private List<MediaCard> getArticlesContent() {
        List<Page> articles = pageRepository.findByModel(ArticleModel.ARTICLE_PAGE_MODEL.getName());
        return articles.stream()
            .map(article -> new MediaCard(pageService.buildPageContent(ArticleModel.ARTICLE_PAGE_MODEL, article), this.mediaCategoryRepository.findByPageId(article.getId())))
            .collect(Collectors.toList());
    }

    private List<MediaCard> getReportsContent() {
        List<Page> reports = pageRepository.findByModel(PhotoReportModel.PHOTO_REPORT_PAGE_MODEL.getName());
        return reports.stream()
            .map(report -> new MediaCard(pageService.buildPageContent(PhotoReportModel.PHOTO_REPORT_PAGE_MODEL, report), this.mediaCategoryRepository.findByPageId(report.getId())))
            .collect(Collectors.toList());
    }

    private List<MediaCard> getInterviewsContent() {
        List<Page> interviews = pageRepository.findByModel(InterviewModel.INTERVIEW_PAGE_MODEL.getName());
        return interviews.stream()
            .map(interview -> new MediaCard(pageService.buildPageContent(InterviewModel.INTERVIEW_PAGE_MODEL, interview), this.mediaCategoryRepository.findByPageId(interview.getId())))
            .collect(Collectors.toList());
    }

    private List<MediaCategory> getMediaCategories() {
        return this.mediaCategoryRepository.listUsedCategory()
            .stream()
            .collect(Collectors.toList());
    }

    private static class MediaCard {
        private final PageContent page;
        private final  List<MediaCategory> categories;

        private MediaCard(PageContent page,
            List<MediaCategory> categories) {
            this.page = page;
            this.categories = categories;
        }

        public PageContent getPage() {
            return page;
        }

        public String getCategoryIdsAsJsonArray() {
            return this.categories.stream().map(category -> category.getId().toString()).collect(Collectors.joining(",", "[", "]"));
        }
    }
}
