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
        Page invasiveSpeciesPage = new Page(1L, "especes-envahissantes", IndicatorModel.INDICATOR_PAGE_MODEL.getName(), "Espèces envahissantes", Collections.emptyList());
        when(mockPageRepository.findByNameAndModel("especes-envahissantes", IndicatorModel.INDICATOR_PAGE_MODEL.getName()))
            .thenReturn(Optional.of(invasiveSpeciesPage));
        when(mockPageService.buildPageContent(IndicatorModel.INDICATOR_PAGE_MODEL, invasiveSpeciesPage)).thenReturn(
            new PageContent(
                invasiveSpeciesPage,
                Map.of(
                        "name", text("Les espèces envahissantes"),
                        "indicator", Map.of(
                                Territory.OUTRE_MER.name(), Map.of ("value", text("60")),
                                Territory.REUNION.name(), Map.of ("value", text("6")),
                                Territory.GUADELOUPE.name(), Map.of ("value", text("14")),
                                Territory.SAINT_PIERRE_ET_MIQUELON.name(), Map.of ("value", text("23"))
                        ),
                        "presentation", Map.of(
                                "category", text("Espèces"),
                                "description", text("espèces sur les 100..."),
                                "image", image(1L)
                        ),
                        "understand", Map.of(
                                "title", text("Comprendre"),
                                "image", image(2L),
                                "sections", List.of(
                                        Map.of(
                                                "title", text("Raison 1"),
                                                "description", text("Explication raison 1")
                                        )
                                )
                        ),
                        "indicators", Map.of(
                                "title", text("Indicateurs"),
                                "indicators", List.of(
                                        Map.of(
                                                "name", text("Réunion"),
                                                "value", text("12"),
                                                "image", image(3L)
                                        ),
                                        Map.of(
                                                "name", text("Martinique"),
                                                "value", text("31"),
                                                "image", image(4L)
                                        )
                                )
                        ),
                        "ecogestures", Map.of(
                                "title", text("Ecogestes"),
                                "ecogestures", List.of(
                                        Map.of(
                                                "name", text("Ecogesture1"),
                                                "category", text("loisirs"),
                                                "description", text("Description ecogesture1"),
                                                "image", image(6L),
                                                "link", link("/indicateurs/ecogesture1")
                                        )
                                )
                        ),
                        "next", Map.of(
                                "name", text("Surface des forêts"),
                                "image", image(8L),
                                "link", link("other")
                        )
                )
            )
        );

        Page deforestationPage = new Page(2L, "deforestation", IndicatorModel.INDICATOR_PAGE_MODEL.getName(), "Déforestation", Collections.emptyList());
        when(mockPageRepository.findByNameAndModel(deforestationPage.getName(), IndicatorModel.INDICATOR_PAGE_MODEL.getName()))
            .thenReturn(Optional.of(deforestationPage));
        when(mockPageService.buildPageContent(IndicatorModel.INDICATOR_PAGE_MODEL, deforestationPage)).thenReturn(
            new PageContent(
                invasiveSpeciesPage,
                Map.of(
                    "presentation", Map.of(
                        "category", text("Espèces"),
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
            "image", image(1L)
        );
        when(mockPageService.buildPageContent(IndicatorModel.INDICATOR_HOME_PAGE_MODEL, homePage))
            .thenReturn(new PageContent(homePage, homeContent));

        IndicatorCategory category1 = new IndicatorCategory(1L, "category1");
        IndicatorCategory category2 = new IndicatorCategory(2L, "category2");
        IndicatorCategory category3 = new IndicatorCategory(3L, "category3");

        Indicator invasiveSpecies = new Indicator(1L, invasiveSpeciesPage.getName(), List.of(category1, category2));
        Indicator deforestation = new Indicator(2L, deforestationPage.getName(), List.of(category2, category3));
        Indicator notExisting = new Indicator(3L, "not-existing", List.of(category3));
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
    }

    @Test
    void shouldDisplayIndicatorPageForInvasiveSpecies() throws Exception {
        mockMvc.perform(get("/indicateurs/especes-envahissantes"))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
               .andExpect(content().string(containsString("<title>Espèces envahissantes</title>")))
               .andExpect(content().string(containsString("<h1>Les espèces envahissantes</h1>")))
               .andExpect(content().string(containsString("<h2>Comprendre</h2>")))
               .andExpect(content().string(containsString("<h2>Indicateurs</h2>")))
               .andExpect(content().string(containsString("<h3>Réunion</h3>")))
               .andExpect(content().string(containsString("<p>6</p>")))
               .andExpect(content().string(containsString("<h3>Guadeloupe</h3>")))
               .andExpect(content().string(containsString("<p>14</p>")))
               .andExpect(content().string(containsString("<h2>Ecogestes</h2>")))
               .andExpect(content().string(containsString("<h3>Ecogesture1</h3>")));
    }

    @Test
    void shouldDisplayHomePage() throws Exception {
        mockMvc.perform(get("/indicateurs"))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
               .andExpect(content().string(containsString("<title>Indicateurs</title>")))
               .andExpect(content().string(containsString("<h1>Tous les indicateurs</h1>")))
               .andExpect(content().string(containsString("category1")))
               .andExpect(content().string(containsString("category2")))
               .andExpect(content().string(containsString("category3")))
               .andExpect(content().string(not(containsString("category4"))))
               .andExpect(content().string(containsString("10\u00a0%")))
               .andExpect(content().string(containsString("espèces sur les 100")))
               .andExpect(content().string(containsString("2\u00a0000\u00a0km2")))
               .andExpect(content().string(containsString("de forêt")));
    }
}
