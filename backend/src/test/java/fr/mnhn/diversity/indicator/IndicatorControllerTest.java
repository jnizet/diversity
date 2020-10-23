package fr.mnhn.diversity.indicator;

import static fr.mnhn.diversity.model.testing.ModelTestingUtil.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import fr.mnhn.diversity.ecogesture.EcogestureModel;
import fr.mnhn.diversity.ecogesture.Ecogesture;
import fr.mnhn.diversity.indicator.thymeleaf.IndicatorDialect;
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
 * MVC tests for {@link IndicatorController}
 */
@WebMvcTest(IndicatorController.class)
@Import(IndicatorDialect.class)
class IndicatorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PageRepository mockPageRepository;

    @MockBean
    private IndicatorRepository mockIndicatorRepository;

    @MockBean
    private PageService mockPageService;

    @BeforeEach
    void prepare() {
        IndicatorCategory category1 = new IndicatorCategory(1L, "category1");
        IndicatorCategory category2 = new IndicatorCategory(2L, "category2");
        IndicatorCategory category3 = new IndicatorCategory(3L, "category3");

        Ecogesture ecogesture = new Ecogesture(42L, "recifs");

        Indicator invasiveSpecies = new Indicator(1L, "i1", "especes-envahissantes", List.of(category1, category2), List.of(ecogesture));
        Indicator deforestation = new Indicator(2L, "i2", "deforestation", List.of(category2, category3), List.of());
        Indicator notExisting = new Indicator(3L, "i3", "not-existing", List.of(category3), List.of());

        Page invasiveSpeciesPage = new Page(1L, invasiveSpecies.getSlug(), IndicatorModel.INDICATOR_PAGE_MODEL.getName(), "Espèces envahissantes", Collections.emptyList());
        when(mockPageRepository.findByNameAndModel(invasiveSpeciesPage.getName(), IndicatorModel.INDICATOR_PAGE_MODEL.getName()))
            .thenReturn(Optional.of(invasiveSpeciesPage));
        when(mockPageService.buildPageContent(IndicatorModel.INDICATOR_PAGE_MODEL, invasiveSpeciesPage)).thenReturn(
            new PageContent(
                invasiveSpeciesPage,
                Map.of(
                    "presentation", Map.of(
                        "description", text("espèces sur les 100..."),
                        "image", image(1L),
                        "onbUrl", text("https://google.com")
                    ),
                    "understand", Map.of(
                        "image", image(2L),
                        "title1", text("Raison 1"),
                        "text1", text("Explication raison 1"),
                        "title2", text("Raison 1"),
                        "text2", text("Explication raison 1"),
                        "keyword", text("Espèces"),
                        "paragraphs", List.of(
                            Map.of(
                                "title", text("Raison 3"),
                                "text", text("Explication raison 3")
                            )
                        )
                    ),
                    "territories", Map.of(
                        "title", text("Espèces envahissantes par territoire")
                    ),
                    "ecogestures", Map.of(
                        "title", text("Eviter les espèces envahissantes")
                    )
                )
            )
        );
        when(mockIndicatorRepository.findBySlug(invasiveSpecies.getSlug())).thenReturn(Optional.of(invasiveSpecies));
        when(mockIndicatorRepository.getValues(invasiveSpecies)).thenReturn(
            Map.of(
                Territory.OUTRE_MER, new IndicatorValue(60, null),
                Territory.REUNION, new IndicatorValue(40, null),
                Territory.GUADELOUPE, new IndicatorValue(14, null)
            )
        );

        Page deforestationPage = new Page(2L, deforestation.getSlug(), IndicatorModel.INDICATOR_PAGE_MODEL.getName(), "Déforestation", Collections.emptyList());
        when(mockPageRepository.findByNameAndModel(deforestationPage.getName(), IndicatorModel.INDICATOR_PAGE_MODEL.getName()))
            .thenReturn(Optional.of(deforestationPage));
        when(mockPageService.buildPageContent(IndicatorModel.INDICATOR_PAGE_MODEL, deforestationPage)).thenReturn(
            new PageContent(
                invasiveSpeciesPage,
                Map.of(
                    "presentation", Map.of(
                        "description", text("de forêt..."),
                        "image", image(1L)
                    )
                )
            )
        );

        Page homePage = new Page(40L,
                                 IndicatorModel.INDICATOR_HOME_PAGE_NAME,
                                 IndicatorModel.INDICATOR_HOME_PAGE_MODEL.getName(),
                                 "Indicateurs",
                                 Collections.emptyList());
        when(mockPageRepository.findByNameAndModel(homePage.getName(), homePage.getModelName()))
            .thenReturn(Optional.of(homePage));

        Map<String, Object> homeContent = Map.of(
            "title", text("Tous les indicateurs"),
            "presentation", text("Presentation"),
            "image", image(1L),
            "understand", Map.of(
                "title", text("Comprendre"),
                "description", text("Comprendre description"),
                "image", image(1L)
            ),
            "onb", Map.of(
                "title", text("ONB"),
                "description", text("ONB description"),
                "image", image(1L),
                "link", link("lien")
            ),
            "quote", text("Citation"),
            "questions", List.of(
                Map.of(
                    "question", text("Question 1"),
                    "answer", text("Réponse 1"),
                    "quote", text("Citation 1")
                ),
                Map.of(
                    "question", text("Question 2"),
                    "answer", text("Réponse 2"),
                    "quote", text("Citation 2")
                )
            )
        );
        when(mockPageService.buildPageContent(IndicatorModel.INDICATOR_HOME_PAGE_MODEL, homePage))
            .thenReturn(new PageContent(homePage, homeContent));

        when(mockIndicatorRepository.list()).thenReturn(
            List.of(invasiveSpecies, deforestation, notExisting)
        );

        when(mockIndicatorRepository.getValuesForIndicatorsAndTerritory(any(), eq(Territory.OUTRE_MER)))
            .thenReturn(
                Map.of(
                    invasiveSpecies, new IndicatorValue(10, "%"),
                    deforestation, new IndicatorValue(2000, "km2")
                )
            );
        when(mockPageRepository.findByModel(IndicatorModel.INDICATOR_PAGE_MODEL.getName())).thenReturn(
            List.of(invasiveSpeciesPage, deforestationPage)
        );

        // next indicator for invasive species
        when(mockPageRepository.findNextOrFirstByModel(IndicatorModel.INDICATOR_PAGE_MODEL.getName(), invasiveSpeciesPage.getId())).thenReturn(
            Optional.of(deforestationPage)
        );
        when(mockIndicatorRepository.findBySlug(deforestationPage.getName())).thenReturn(Optional.of(deforestation));
        when(mockIndicatorRepository.getValueForIndicatorAndTerritory(deforestation, Territory.OUTRE_MER))
            .thenReturn(Optional.of(new IndicatorValue(2000, "km2")));

        Page ecogesturePage = new Page(435L, ecogesture.getSlug(), EcogestureModel.ECO_GESTURE_PAGE_MODEL.getName(), "Corals", Collections.emptyList());
        when(mockPageRepository.findByNameAndModel(ecogesture.getSlug(), EcogestureModel.ECO_GESTURE_PAGE_MODEL.getName()))
            .thenReturn(Optional.of(ecogesturePage));
        when(mockPageService.buildPageContent(EcogestureModel.ECO_GESTURE_PAGE_MODEL, ecogesturePage))
            .thenReturn(
                new PageContent(
                    ecogesturePage,
                    Map.of(
                        "presentation", Map.of(
                            "name", text("Protect the corals"),
                            "category", text("Leisure"),
                            "description", text("Description"),
                            "image", image(1L)
                        )
                    )
                )
            );
    }

    @Test
    void shouldDisplayIndicatorPageForInvasiveSpecies() throws Exception {
        mockMvc.perform(get("/indicateurs/especes-envahissantes"))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
               .andExpect(content().string(containsString("<title>Espèces envahissantes</title>")))
               .andExpect(content().string(containsString("espèces sur les 100")))
               .andExpect(content().string(containsString("60")))
               .andExpect(content().string(containsString("Raison 1")))
               .andExpect(content().string(containsString("Raison 3")))
               .andExpect(content().string(containsString("40")))
               .andExpect(content().string(containsString("Guadeloupe")))
               .andExpect(content().string(containsString("14")))
               .andExpect(content().string(containsString("Eviter les espèces envahissantes")))
               .andExpect(content().string(containsString("Protect the corals")))
               .andExpect(content().string(containsString("2\u00a0000\u00a0km2")))
               .andExpect(content().string(containsString("de forêt...")))
               .andExpect(content().string(containsString("</html>")));
    }

    @Test
    void shouldDisplayHomePage() throws Exception {
        mockMvc.perform(get("/indicateurs"))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
               .andExpect(content().string(containsString("<title>Indicateurs</title>")))
               .andExpect(content().string(containsString("category1")))
               .andExpect(content().string(containsString("category2")))
               .andExpect(content().string(containsString("category3")))
               .andExpect(content().string(not(containsString("category4"))))
               .andExpect(content().string(containsString("Comprendre")))
               .andExpect(content().string(containsString("ONB")))
               .andExpect(content().string(containsString("10\u00a0%")))
               .andExpect(content().string(containsString("espèces sur les 100")))
               .andExpect(content().string(containsString("2\u00a0000\u00a0km2")))
               .andExpect(content().string(containsString("de forêt")))
               .andExpect(content().string(containsString("Question 1")))
               .andExpect(content().string(containsString("Réponse 1")))
               .andExpect(content().string(containsString("Citation 1")))
               .andExpect(content().string(containsString("Question 2")))
               .andExpect(content().string(containsString("Réponse 2")))
               .andExpect(content().string(containsString("Citation 2")));
    }
}
