package fr.mnhn.diversity.ecogesture;

import static fr.mnhn.diversity.model.testing.ModelTestingUtil.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import fr.mnhn.diversity.ControllerTest;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import fr.mnhn.diversity.common.thymeleaf.RequestDialect;
import fr.mnhn.diversity.indicator.Indicator;
import fr.mnhn.diversity.indicator.IndicatorModel;
import fr.mnhn.diversity.indicator.IndicatorRepository;
import fr.mnhn.diversity.indicator.IndicatorValue;
import fr.mnhn.diversity.indicator.thymeleaf.IndicatorDialect;
import fr.mnhn.diversity.matomo.MatomoConfig;
import fr.mnhn.diversity.model.Page;
import fr.mnhn.diversity.model.PageContent;
import fr.mnhn.diversity.model.PageRepository;
import fr.mnhn.diversity.model.PageService;
import fr.mnhn.diversity.territory.Territory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * MVC tests for {@link EcogestureController}
 * @author JB Nizet
 */
@WebMvcTest(EcogestureController.class)
@Import({RequestDialect.class, IndicatorDialect.class, MatomoConfig.class})
class EcogestureControllerTest extends ControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PageRepository mockPageRepository;

    @MockBean
    private IndicatorRepository mockIndicatorRepository;

    @MockBean
    private EcogestureRepository mockEcogestureRepository;

    @MockBean
    private PageService mockPageService;

    @BeforeEach
    void prepare() {
        Page coralsPage = new Page(1L,
                                   "corals",
                                   EcogestureModel.ECO_GESTURE_PAGE_MODEL.getName(),
                                   "Protect corals",
                                   Collections.emptyList());
        when(mockPageRepository.findByNameAndModel(coralsPage.getName(), coralsPage.getModelName()))
            .thenReturn(Optional.of(coralsPage));

        Page homePage = new Page(2L,
                                 EcogestureModel.ECO_GESTURE_HOME_PAGE_NAME,
                                 EcogestureModel.ECO_GESTURE_HOME_PAGE_MODEL.getName(),
                                 "??cogestes",
                                 Collections.emptyList());
        when(mockPageRepository.findByNameAndModel(homePage.getName(), homePage.getModelName()))
            .thenReturn(Optional.of(homePage));

        List<Page> gesturePages = List.of(coralsPage);
        when(mockPageRepository.findByModel(EcogestureModel.ECO_GESTURE_PAGE_MODEL.getName()))
            .thenReturn(gesturePages);

        Ecogesture corals = new Ecogesture(101L, "corals");
        Indicator indicator = new Indicator(1L,"biom-1", "deforestation", false,1, List.of(), List.of(corals));
        when(mockIndicatorRepository.findIndicatorsForEcogesture(corals.getSlug()))
            .thenReturn(List.of(indicator));
        when(mockIndicatorRepository.getValuesForIndicatorsAndTerritory(List.of(indicator), Territory.OUTRE_MER))
            .thenReturn(Map.of(indicator, new IndicatorValue(24, "%")));
        Page deforestationPage = new Page(3L,
                                          IndicatorModel.INDICATOR_HOME_PAGE_NAME,
                                          IndicatorModel.INDICATOR_HOME_PAGE_MODEL.getName(),
                                          "Deforestation",
                                          Collections.emptyList());
        when(mockPageRepository.findByNameAndModel(indicator.getSlug(), IndicatorModel.INDICATOR_PAGE_MODEL.getName()))
            .thenReturn(Optional.of(deforestationPage));
        when(mockPageService.buildPageContent(IndicatorModel.INDICATOR_PAGE_MODEL, deforestationPage)).thenReturn(
            new PageContent(
                deforestationPage,
                Map.of(
                    "name", text("Deforestation"),
                    "presentation", Map.of(
                        "description", text("arbres"),
                        "image", image(1L),
                        "shadowed", checkbox(true)
                    )
                )
            )
        );

        Page plasticPage = new Page(4L, "plastics", EcogestureModel.ECO_GESTURE_PAGE_MODEL.getName(), "Plastics", Collections.emptyList());
        when(mockPageRepository.findNextOrFirstByModel(EcogestureModel.ECO_GESTURE_PAGE_MODEL.getName(), coralsPage.getId()))
            .thenReturn(Optional.of(plasticPage));
        when(mockEcogestureRepository.findBySlug(plasticPage.getName()))
            .thenReturn(Optional.of(new Ecogesture(102L, "plastics")));
        when(mockPageService.buildPageContent(EcogestureModel.ECO_GESTURE_PAGE_MODEL, plasticPage))
            .thenReturn(new PageContent(
                plasticPage,
                Map.of(
                    "presentation", Map.of(
                        "name", text("Plastics"),
                        "image", image(1L),
                        "shadowed", checkbox(true)
                    )
                )
            ));

        Page actSection = new Page(5L,
                                   "ecogesture-act",
                                   EcogestureActSectionModel.ECOGESTURE_ACT_SECTION_MODEL.getName(),
                                   "Agir pour la science",
                                   Collections.emptyList());
        when(mockPageRepository.findByNameAndModel(EcogestureActSectionModel.ECOGESTURE_ACT_SECTION_NAME, EcogestureActSectionModel.ECOGESTURE_ACT_SECTION_MODEL.getName()))
            .thenReturn(Optional.of(actSection));
        when(mockPageService.buildPageContent(EcogestureActSectionModel.ECOGESTURE_ACT_SECTION_MODEL, actSection))
            .thenReturn(new PageContent(
                actSection,
                Map.of(
                "title", text("Agir pour la science"),
                "description", text("description"),
                "firstActionName", text("Action 1"),
                "firstActionLink", link("Lien action 1"),
                "secondActionName", text("Action 2"),
                "secondActionLink", link("Lien action 2")
                )
            ));

        Map<String, Object> coralsContent = Map.of(
            "presentation", Map.of(
                "name", text("Corals"),
                "category", text("Leisure"),
                "description", text("Description"),
                "image", image(1L),
                "shadowed", checkbox(true),
                "file", image(2L)
            ),
            "understand", Map.of(
                "title", text("Understand"),
                "text", text("understand text"),
                "quote", text("quote text")
            ),
            "action", Map.of(
                "title", text("Action"),
                "cards", List.of(
                    Map.of(
                        "icon", image(4L),
                        "description", text("Card 1 description")
                    ),
                    Map.of(
                        "icon", image(5L),
                        "description", text("Card 2 description")
                    )
                )
            )
        );

        Map<String, Object> homeContent = Map.of(
            "title", text("Ecogestures"),
            "presentation", text("Presentation"),
            "image", image(1L),
            "shadowed", checkbox(true),
            "question", text("Qu'est-ce qu'un ??cogeste ?"),
            "answer", text("Les territoires d'outre-mer"),
            "quote", text("Des actions concr??tes"),
            "other", Map.of(
                "title", text("Retrouvez d???autres ??cogestes sur"),
                "text", text("Biodiversit??. Tous vivants !"),
                "image", image(1L)
            )
        );

        when(mockPageService.buildPageContent(EcogestureModel.ECO_GESTURE_HOME_PAGE_MODEL, homePage))
            .thenReturn(new PageContent(homePage, homeContent));
        when(mockPageService.buildPageContent(EcogestureModel.ECO_GESTURE_PAGE_MODEL, coralsPage))
            .thenReturn(new PageContent(coralsPage, coralsContent));;
    }

    @Test
    void shouldDisplayEcogestureHomePage() throws Exception {
        mockMvc.perform(get("/ecogestes"))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
               .andExpect(content().string(containsString("<title>??cogestes</title>")))
               .andExpect(content().string(containsString("<p>Ecogestures</p>")))
               .andExpect(content().string(containsString("<p>Corals</p>")))
               .andExpect(content().string(containsString("un ??cogeste ?</p>")))
               .andExpect(content().string(containsString("<p>Des actions concr??tes</p>")))
               .andExpect(content().string(containsString("<p>Retrouvez d???autres ??cogestes sur</p>")))
               .andExpect(content().string(containsString("</html>")));
    }

    @Test
    void shouldDisplayEcoGesturePage() throws Exception {
        mockMvc.perform(get("/ecogestes/corals"))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
               .andExpect(content().string(containsString("<title>Protect corals</title>")))
               .andExpect(content().string(containsString("<p>Corals</p>")))
               .andExpect(content().string(containsString("<p>Understand</p>")))
               .andExpect(content().string(containsString("href=\"/indicateurs/deforestation\"")))
               .andExpect(content().string(containsString("class=\"indicateur-number-small\"")))
               .andExpect(content().string(containsString(">24")))
               .andExpect(content().string(containsString("<div class=\"text-big\"><p>arbres</p>")))
               .andExpect(content().string(containsString("<p>Action</p>")))
               .andExpect(content().string(containsString("<p>Agir pour la science</p>")))
               .andExpect(content().string(containsString("href=\"/ecogestes/plastics")))
               .andExpect(content().string(containsString("</html>")));
    }
}
