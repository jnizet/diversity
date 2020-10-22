package fr.mnhn.diversity.act;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import fr.mnhn.diversity.common.exception.NotFoundException;
import fr.mnhn.diversity.ecogesture.EcoGestureModel;
import fr.mnhn.diversity.ecogesture.Ecogesture;
import fr.mnhn.diversity.ecogesture.EcogestureRepository;
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
 * Controller used to display the "Participative Science" page
 * @author JB Nizet
 */
@Controller
@Transactional
@RequestMapping("/sciences-participatives")
public class ScienceController {
    private final PageRepository pageRepository;
    private final PageService pageService;

    public ScienceController(PageRepository pageRepository,
                             PageService pageService) {
        this.pageRepository = pageRepository;
        this.pageService = pageService;
    }

    @GetMapping
    public ModelAndView science() {
        Page page = pageRepository.findByNameAndModel(ActModel.SCIENCE_PAGE_NAME, ActModel.SCIENCE_PAGE_MODEL.getName())
                                  .orElseThrow(NotFoundException::new);

        return new ModelAndView("science", Map.of(
            "page", pageService.buildPageContent(ActModel.SCIENCE_PAGE_MODEL, page)
        ));
    }
}
