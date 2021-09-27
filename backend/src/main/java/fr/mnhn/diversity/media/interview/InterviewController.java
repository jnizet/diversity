package fr.mnhn.diversity.media.interview;
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
 * Controller used to display "interview" page
 * @author Arnaud Monteils
 */
@Controller
@Transactional
@RequestMapping("/media/interview")
public class InterviewController {
    private final PageRepository pageRepository;
    private final MediaCategoryRepository mediaCategoryRepository;
    private final PageService pageService;

    public InterviewController(PageRepository pageRepository,
        MediaCategoryRepository mediaCategoryRepository,
        PageService pageService) {
        this.pageRepository = pageRepository;
        this.mediaCategoryRepository = mediaCategoryRepository;
        this.pageService = pageService;
    }

    @GetMapping("/{name}")
    public ModelAndView interview(@PathVariable("name") String name) {

        Page page = pageRepository.findByNameAndModel(name, InterviewModel.INTERVIEW_PAGE_NAME)
            .orElseThrow(NotFoundException::new);

        Map<String, Object> model = Map.of(
            "page", pageService.buildPageContent(InterviewModel.INTERVIEW_PAGE_MODEL, page),
            "categories", mediaCategoryRepository.findByPageId(page.getId())

        );

        return new ModelAndView("media/interview", model);
    }
}
