package fr.mnhn.diversity.ecogesture;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import fr.mnhn.diversity.common.exception.NotFoundException;
import fr.mnhn.diversity.indicator.Indicator;
import fr.mnhn.diversity.indicator.IndicatorModel;
import fr.mnhn.diversity.indicator.IndicatorRepository;
import fr.mnhn.diversity.indicator.IndicatorValue;
import fr.mnhn.diversity.model.Page;
import fr.mnhn.diversity.model.PageContent;
import fr.mnhn.diversity.model.PageRepository;
import fr.mnhn.diversity.model.PageService;
import fr.mnhn.diversity.model.Text;
import fr.mnhn.diversity.territory.Territory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller used to display the eco-gesture details pages and the eco-gestures home page
 * @author JB Nizet
 */
@Controller
@Transactional
@RequestMapping("/ecogestes")
public class EcogestureController {
    private final PageRepository pageRepository;
    private final PageService pageService;
    private final IndicatorRepository indicatorRepository;
    private final EcogestureRepository ecogestureRepository;

    public EcogestureController(PageRepository pageRepository, PageService pageService, IndicatorRepository indicatorRepository, EcogestureRepository ecogestureRepository) {
        this.pageRepository = pageRepository;
        this.pageService = pageService;
        this.indicatorRepository = indicatorRepository;
        this.ecogestureRepository = ecogestureRepository;
    }

    /**
     * Displays the home page of the eco-gestures. It displays a presentation section, and then a gallery
     * of all the eco-gestures.
     */
    @GetMapping()
    public ModelAndView home() {
        Page homePage = pageRepository.findByNameAndModel(EcogestureModel.ECO_GESTURE_HOME_PAGE_NAME,
                                                          EcogestureModel.ECO_GESTURE_HOME_PAGE_MODEL.getName())
                                      .orElseThrow(NotFoundException::new);

        // build the content of the home page
        PageContent homePageContent = pageService.buildPageContent(EcogestureModel.ECO_GESTURE_HOME_PAGE_MODEL, homePage);

        // but the home page also displays a gallery of the presentation names and images of all ecogestures,
        // so we load all the ecogesture pages too.
        // this could be optimized in two ways, but it's probably not worth it
        // - use a page model that contains only the presentation section of the gesture element to avoid populating
        //   the content with useless stuff
        // - only load the actual elements that we need from the database (the presentation section only)
        List<Page> ecoGesturePages =
            pageRepository.findByModel(EcogestureModel.ECO_GESTURE_PAGE_MODEL.getName());
        List<PageContent> gesturePageContents =
            ecoGesturePages.stream()
                           .map(page -> pageService.buildPageContent(EcogestureModel.ECO_GESTURE_PAGE_MODEL, page))
                           .collect(Collectors.toList());

        return new ModelAndView("ecogesture/ecogesture-home",
                                Map.of(
                                    "page", homePageContent,
                                    "gesturePages", gesturePageContents
                                )
        );
    }

    /**
     * Displays the details of an eco-gesture.
     * @param slug the slug of the eco-gesture, which is also the name of the eco-gesture page
     */
    @GetMapping("/{slug}")
    public ModelAndView detail(@PathVariable("slug") String slug, HttpServletRequest request) {
        Page page = pageRepository.findByNameAndModel(slug, EcogestureModel.ECO_GESTURE_PAGE_MODEL.getName())
                                  .orElseThrow(NotFoundException::new);

        IndicatorCard linkedIndicatorCard = getLinkedIndicatorCard(slug).orElse(null);

        EcogestureCard nextEcogestureCard = getNextEcogestureCard(page.getId()).orElse(null);

        Page actSection = pageRepository.findByNameAndModel(EcogestureActSectionModel.ECOGESTURE_ACT_SECTION_NAME, EcogestureActSectionModel.ECOGESTURE_ACT_SECTION_MODEL.getName())
                                  .orElseThrow(NotFoundException::new);
        PageContent actSectionContent = pageService.buildPageContent(EcogestureActSectionModel.ECOGESTURE_ACT_SECTION_MODEL, actSection);

        PageContent pageContent = pageService.buildPageContent(EcogestureModel.ECO_GESTURE_PAGE_MODEL, page);
        String twitterText = getTwitterText(pageContent, request);

        Map<String, Object> model = Map.of(
            "page", pageContent,
            "act", actSectionContent,
            "indicator", linkedIndicatorCard == null ? false : linkedIndicatorCard,
            "nextEcogestureCard", nextEcogestureCard == null ? false : nextEcogestureCard,
            "twitterText", twitterText
        );
        return new ModelAndView("ecogesture/ecogesture", model);
    }

    @SuppressWarnings("unchecked")
    private String getTwitterText(PageContent pageContent, HttpServletRequest request) {
        Map<String, Object> presentation = (Map<String, Object>) pageContent.getContent().get("presentation");
        Text nameText = (Text) presentation.get("name");
        String name = nameText.getText();

        return name + "\n" + request.getRequestURL();
    }

    private Optional<IndicatorCard> getLinkedIndicatorCard(String slug) {
        // find the linked indicator to display
        // each indicator can reference various ecogestures
        // so we fetch all the indicators which are referencing this ecogesture
        List<Indicator> indicatorsForEcogesture = indicatorRepository.findIndicatorsForEcogesture(slug);
        // get the values for these indicators
        Map<Indicator, IndicatorValue> valuesByIndicator =
            indicatorRepository.getValuesForIndicatorsAndTerritory(indicatorsForEcogesture, Territory.OUTRE_MER);
        // pick a random indicator among these
        // by shuffling the list, and picking the first one with a value for OUTRE_MER and an associated page
        Collections.shuffle(indicatorsForEcogesture);
        Indicator linkedIndicator = null;
        PageContent linkedIndicatorPage = null;
        for (Indicator indicator : indicatorsForEcogesture) {
            Optional<Page> indicatorPage = pageRepository.findByNameAndModel(indicator.getSlug(), IndicatorModel.INDICATOR_PAGE_MODEL.getName());
            IndicatorValue indicatorValue = valuesByIndicator.get(indicator);
            if (indicatorValue != null && indicatorPage.isPresent()) {
                linkedIndicator = indicator;
                linkedIndicatorPage = pageService.buildPageContent(IndicatorModel.INDICATOR_PAGE_MODEL, indicatorPage.get());
                break;
            }
        }
        return linkedIndicator == null ? Optional.empty() : Optional.of(new IndicatorCard(linkedIndicator, linkedIndicatorPage, valuesByIndicator.get(linkedIndicator)));
    }

    private Optional<EcogestureCard> getNextEcogestureCard(Long currentEcogesturePageId) {
        return pageRepository
            .findNextOrFirstByModel(EcogestureModel.ECO_GESTURE_PAGE_MODEL.getName(), currentEcogesturePageId)
            .flatMap(nextOrFirstEcogesturePage ->
                         ecogestureRepository
                             .findBySlug(nextOrFirstEcogesturePage.getName())
                             .map(ecogesture -> new EcogestureCard(
                                      ecogesture,
                                      pageService.buildPageContent(EcogestureModel.ECO_GESTURE_PAGE_MODEL, nextOrFirstEcogesturePage)
                                  )
                             )
            );
    }

    private static class IndicatorCard {

        private final Indicator indicator;
        private final PageContent page;
        private final IndicatorValue indicatorValue;

        public IndicatorCard(Indicator indicator,
                             PageContent page,
                             IndicatorValue indicatorValue) {
            this.indicator = indicator;
            this.page = page;
            this.indicatorValue = indicatorValue;
        }

        public Indicator getIndicator() {
            return indicator;
        }

        public PageContent getPage() {
            return page;
        }

        public IndicatorValue getIndicatorValue() {
            return indicatorValue;
        }
    }

    private static class EcogestureCard {
        private final Ecogesture ecogesture;
        private final PageContent page;

        public EcogestureCard(Ecogesture ecogesture, PageContent page) {
            this.ecogesture = ecogesture;
            this.page = page;
        }

        public Ecogesture getEcogesture() {
            return ecogesture;
        }

        public PageContent getPage() {
            return page;
        }
    }
}
