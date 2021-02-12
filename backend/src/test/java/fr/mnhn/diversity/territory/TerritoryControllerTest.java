package fr.mnhn.diversity.territory;

import static fr.mnhn.diversity.model.testing.ModelTestingUtil.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import fr.mnhn.diversity.ControllerTest;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import fr.mnhn.diversity.common.thymeleaf.RequestDialect;
import fr.mnhn.diversity.indicator.IndicatorModel;
import fr.mnhn.diversity.indicator.IndicatorRepository;
import fr.mnhn.diversity.indicator.IndicatorValue;
import fr.mnhn.diversity.indicator.thymeleaf.IndicatorDialect;
import fr.mnhn.diversity.matomo.MatomoConfig;
import fr.mnhn.diversity.model.Page;
import fr.mnhn.diversity.model.PageContent;
import fr.mnhn.diversity.model.PageRepository;
import fr.mnhn.diversity.model.PageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * MVC tests for {@link TerritoryController}
 */
@WebMvcTest({TerritoryController.class, MatomoConfig.class})
@Import({RequestDialect.class, IndicatorDialect.class})
class TerritoryControllerTest extends ControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PageRepository mockPageRepository;

    @MockBean
    private PageService mockPageService;

    @MockBean
    private MapService mockMapService;

    @MockBean
    private IndicatorRepository mockIndicatorRepository;

    @BeforeEach
    void prepare() {
        Page page = new Page(1L, Territory.REUNION.getSlug(), TerritoryModel.TERRITORY_PAGE_MODEL.getName(), "Territoire - La Réunion", Collections.emptyList());
        when(mockPageRepository.findByNameAndModel(page.getName(), TerritoryModel.TERRITORY_PAGE_MODEL.getName())).thenReturn(Optional.of(page));
        Map<String, Object> interests = Map.of(
            "title", text("Interests"),
            "locations", List.of(
                Map.of(
                    "image", image(2L),
                    "name", text("interest1"),
                    "description", text("interestDesc1")
                ),
                Map.of(
                    "image", image(3L),
                    "name", text("interest2"),
                    "description", text("interestDesc2")
                )
            )
        );
        List<Map<String, Object>> species = List.of(
            Map.of(
                "name", text("Specie1"),
                "description", text("specie1"),
                "image", image(5L)
            )
        );
        List<Map<String, Object>> events = List.of(
            Map.of(
                "date", text("1503"),
                "description", text("event1")
            )
        );
        Map<String, Object> ecosystems = Map.of(
            "image", image(34567L),
            "ecosystems", List.of(
                Map.of(
                    "name", text("Ecosystem1"),
                    "description", text("ecosystem1"),
                    "image", image(6L)
                )
            )
        );
        Map<String, Object> risks = Map.of(
            "title", text("Risks"),
            "risks", List.of(
                Map.of(
                    "name", text("Risk1"),
                    "description", text("risk1"),
                    "image", image(7L)
                )
            )
        );
        when(mockPageService.buildPageContent(TerritoryModel.TERRITORY_PAGE_MODEL, page)).thenReturn(
            new PageContent(
                page,
                Map.of(
                    "identity", Map.ofEntries(
                                Map.entry("title", text("La Réunion")),
                                Map.entry("subtitle", text("Une bien jolie île")),
                                Map.entry("presentation", text("presentation")),
                                Map.entry("image", image(1L)),
                                Map.entry("area", text("234")),
                                Map.entry("marineArea", text("345")),
                                Map.entry("population", text("45678")),
                                Map.entry("populationYear", text("2017")),
                                Map.entry("species", text("5432")),
                                Map.entry("highestPoint", text("98")),
                                Map.entry("highestPointName", text("Morne"))
                        ),
                    "interests", interests,
                    "species", species,
                    "events", events,
                    "ecosystems", ecosystems,
                    "risks", risks
                )
            )
        );

        Page saintPierreEtMiquelon = new Page(2L, Territory.SAINT_PIERRE_ET_MIQUELON.getSlug(), TerritoryModel.TERRITORY_PAGE_MODEL.getName(), "Territoire - Saint-Pierre-Et-Miquelon", Collections.emptyList());
        when(mockPageRepository.findNextOrFirstByModel(TerritoryModel.TERRITORY_PAGE_MODEL.getName(), 1L)).thenReturn(Optional.of(saintPierreEtMiquelon));
        when(mockPageService.buildPageContent(TerritoryModel.TERRITORY_PAGE_MODEL, saintPierreEtMiquelon)).thenReturn(
            new PageContent(
                page,
                Map.of(
                    "name", text("Saint-Pierre-Et-Miquelon"),
                    "identity", Map.of(
                        "title", text("Saint-Pierre-Et-Miquelon"),
                        "presentation", text("presentation"),
                        "image", image(1L)
                    )
                )
            )
        );

        Page homePage = new Page(2L, TerritoryModel.TERRITORY_HOME_PAGE_NAME, TerritoryModel.TERRITORY_HOME_PAGE_MODEL.getName(), "Territoires", Collections.emptyList());
        when(mockPageRepository.findByNameAndModel(homePage.getName(), TerritoryModel.TERRITORY_HOME_PAGE_MODEL.getName())).thenReturn(Optional.of(homePage));
        when(mockPageService.buildPageContent(TerritoryModel.TERRITORY_HOME_PAGE_MODEL, homePage)).thenReturn(
            new PageContent(
                homePage,
                Map.of(
                    "header", Map.of(
                        "title", text("Découvrez les territoires"),
                        "text", text("Bla bla"),
                        "population", text("100000"),
                        "species", text("65432")
                    )
                )
            )
        );

        Page i1Page = new Page(7654L, "i1", IndicatorModel.INDICATOR_PAGE_MODEL.getName(), "", List.of());
        Page i2Page = new Page(7655L, "i2", IndicatorModel.INDICATOR_PAGE_MODEL.getName(), "", List.of());
        when(mockPageRepository.findRandomIndicatorPagesForTerritory(eq(2), any())).thenReturn(
            List.of(
                i1Page,
                i2Page
            )
        );
        when(mockPageService.buildPageContent(IndicatorModel.INDICATOR_PAGE_MODEL, i1Page)).thenReturn(
            new PageContent(i1Page, Map.of("presentation", Map.of("descriptionTerritories", text("i1 desc"))))
        );
        when(mockPageService.buildPageContent(IndicatorModel.INDICATOR_PAGE_MODEL, i2Page)).thenReturn(
            new PageContent(i2Page, Map.of("presentation", Map.of("descriptionTerritories", text("i2 desc"))))
        );
        when(mockIndicatorRepository.getValuesForIndicatorSlugsAndTerritory(Set.of("i1", "i2"), Territory.REUNION)).thenReturn(
            Map.of(
                "i1", new IndicatorValue(10, "%"),
                "i2", new IndicatorValue(20, "%")
            )
        );
    }

    @Test
    void shouldDisplayTerritoryPageForLaReunion() throws Exception {
        mockMvc.perform(get("/territoires/{slug}", Territory.REUNION.getSlug()))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
               .andExpect(content().string(containsString("<title>Territoire - La Réunion</title>")))
               .andExpect(content().string(containsString("<p>La Réunion</p>")))
               .andExpect(content().string(containsString("<p>Une bien jolie île</p>")))
               .andExpect(content().string(containsString("<p>presentation</p>")))
               .andExpect(content().string(containsString("<p>Interests</p>")))
               .andExpect(content().string(containsString("<p>interest1</p>")))
               .andExpect(content().string(containsString("<p>interestDesc1</p>")))
               .andExpect(content().string(containsString("<p>interest2</p>")))
               .andExpect(content().string(containsString("<p>interestDesc2</p>")))
               .andExpect(content().string(containsString("<p>10\u00a0%</p>")))
               .andExpect(content().string(containsString("<p>i1 desc</p>")))
               .andExpect(content().string(containsString("<p>20\u00a0%</p>")))
               .andExpect(content().string(containsString("<p>i2 desc</p>")))
               .andExpect(content().string(containsString("<p>Specie1</p>")))
               .andExpect(content().string(containsString("<p>specie1</p>")))
               .andExpect(content().string(containsString("<p>1503</p>")))
               .andExpect(content().string(containsString("<p>event1</p>")))
               .andExpect(content().string(containsString("<p>Ecosystem1</p>")))
               .andExpect(content().string(containsString("<p>ecosystem1</p>")))
               .andExpect(content().string(containsString("<p>Risks</p>")))
               .andExpect(content().string(containsString("<p>Risk1</p>")))
               .andExpect(content().string(containsString("<p>risk1</p>")))
               .andExpect(content().string(containsString("<p>Saint-Pierre-et-Miquelon</p>")))
               .andExpect(content().string(containsString("</html>")));
    }

    @Test
    void shouldDisplayTerritoryHome() throws Exception {
        Page reunionPage = new Page(43L, Territory.REUNION.getSlug(), TerritoryModel.TERRITORY_PAGE_MODEL.getName(), "", List.of());
        PageContent reunionPageContent =
            new PageContent(
                reunionPage,
                Map.of(
                    "identity", Map.of(
                        "title", text("La Réunion"),
                        "presentation", text("presentation of reunion"),
                        "area", text("2 512"),
                        "population", text("859 959"),
                        "species", text("3456")
                    ),
                    "statistics", List.of(
                        Map.of(
                            "number", text("89%"),
                            "text", text("du territoire classé...")
                        )
                    )
                )
            );
        when(mockMapService.getTerritoryCards()).thenReturn(List.of(new MapTerritoryCard(Territory.REUNION, reunionPageContent)));

        mockMvc.perform(get("/territoires"))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
               .andExpect(content().string(containsString("<title>Territoires</title>")))
               .andExpect(content().string(containsString("<p>Découvrez les territoires</p>")))
               .andExpect(content().string(containsString("<p>La Réunion</p>")))
               .andExpect(content().string(containsString("<p>presentation of reunion</p>")))
               .andExpect(content().string(containsString("</html>")));
    }
}
