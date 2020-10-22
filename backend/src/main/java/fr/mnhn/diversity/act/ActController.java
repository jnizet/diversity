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
 * Controller used to display the "Act together" page, displaying two random ecogestures
 * and a link to the participative sciences page
 * @author JB Nizet
 */
@Controller
@Transactional
@RequestMapping("/agir-ensemble")
public class ActController {
    private final PageRepository pageRepository;
    private final PageService pageService;
    private final EcogestureRepository ecogestureRepository;

    public ActController(PageRepository pageRepository,
                         PageService pageService,
                         EcogestureRepository ecogestureRepository) {
        this.pageRepository = pageRepository;
        this.pageService = pageService;
        this.ecogestureRepository = ecogestureRepository;
    }

    @GetMapping
    public ModelAndView act() {
        Page page = pageRepository.findByNameAndModel(ActModel.ACT_PAGE_NAME, ActModel.ACT_PAGE_MODEL.getName())
                                  .orElseThrow(NotFoundException::new);

        List<Ecogesture> randomEcogestures = new ArrayList<>(ecogestureRepository.list());
        Collections.shuffle(randomEcogestures);
        List<PageContent> randomEcoGestureCards =
            randomEcogestures
                .stream()
                .map(ecogesture -> pageRepository.findByNameAndModel(ecogesture.getSlug(),
                                                                     EcoGestureModel.ECO_GESTURE_PAGE_MODEL.getName()))
                .filter(Optional::isPresent)
                .limit(2)
                .map(Optional::orElseThrow)
                .map(ecogesturePage -> pageService.buildPageContent(EcoGestureModel.ECO_GESTURE_PAGE_MODEL, ecogesturePage))
                .collect(Collectors.toList());

        return new ModelAndView("act", Map.of(
            "page", pageService.buildPageContent(ActModel.ACT_PAGE_MODEL, page),
            "ecogestureCards", randomEcoGestureCards
        ));
    }
}
