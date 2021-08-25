package fr.mnhn.diversity.media;

import fr.mnhn.diversity.media.article.ArticleModel;
import fr.mnhn.diversity.media.interview.InterviewModel;
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
    private final PageService pageService;

    public MediaController(PageRepository pageRepository,
        PageService pageService
       ) {
        this.pageRepository = pageRepository;
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
            "interviews", getInterviewsContent()
        ));
    }

    private List<PageContent> getArticlesContent(){
        List<Page> articles = pageRepository.findByModel(ArticleModel.ARTICLE_PAGE_MODEL.getName());
        return articles.stream()
            .map(article -> pageService.buildPageContent(ArticleModel.ARTICLE_PAGE_MODEL, article))
            .collect(Collectors.toList());
    }

    private List<PageContent> getInterviewsContent(){
        List<Page> interviews = pageRepository.findByModel(InterviewModel.INTERVIEW_PAGE_MODEL.getName());
        return interviews.stream()
            .map(article -> pageService.buildPageContent(InterviewModel.INTERVIEW_PAGE_MODEL, article))
            .collect(Collectors.toList());
    }
}
