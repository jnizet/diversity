package fr.mnhn.diversity.territory;

import static fr.mnhn.diversity.model.testing.ModelTestingUtil.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import fr.mnhn.diversity.common.thymeleaf.RequestDialect;
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
@WebMvcTest(TerritoryController.class)
@Import(RequestDialect.class)
class TerritoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PageRepository mockPageRepository;

    @MockBean
    private PageService mockPageService;

    @MockBean
    private MapService mockMapService;

    @BeforeEach
    void prepare() {
        Page page = new Page(1L, Territory.REUNION.getSlug(), TerritoryModel.TERRITORY_PAGE_MODEL.getName(), "Territoire - La Réunion", Collections.emptyList());
        when(mockPageRepository.findByNameAndModel(page.getName(), TerritoryModel.TERRITORY_PAGE_MODEL.getName())).thenReturn(Optional.of(page));
        Map<String, Object> interests = Map.of(
            "title", text("Interests"),
            "images", List.of(
                Map.of("image", image(2L)),
                Map.of("image", image(3L))
            )
        );
        Map<String, Object> indicators = Map.of(
            "title", text("Indicators"),
            "indicators", List.of(
                Map.of(
                    "name", text("Indicator1"),
                    "value", text("12"),
                    "image", image(4L),
                    "link", link("12")
                )
            )
        );
        Map<String, Object> species = Map.of(
            "title", text("Species"),
            "species", List.of(
                Map.of(
                    "name", text("Specie1"),
                    "description", text("specie1"),
                    "image", image(5L)
                )
            )
        );
        Map<String, Object> ecosystems = Map.of(
            "title", text("Ecosystems"),
            "ecosystems", List.of(
                Map.of(
                    "name", text("Ecosystem1"),
                    "description", text("ecosystem1"),
                    "image", image(6L)
                )
            )
        );
        Map<String, Object> timeline = Map.of(
            "title", text("Timeline"),
            "events", List.of(
                Map.of(
                    "name", text("1535"),
                    "description", text("desc 1535")
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
                    "name", text("La Réunion"),
                    "identity", Map.of(
                                "title", text("La Réunion"),
                                "presentation", text("presentation"),
                                "infography", image(1L)
                        ),
                    "interests", interests,
                    "indicators", indicators,
                    "species", species,
                    "ecosystems", ecosystems,
                    "timeline", timeline,
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
                        "infography", image(1L)
                    ),
                    "interests", interests,
                    "indicators", indicators,
                    "species", species,
                    "ecosystems", ecosystems,
                    "timeline", timeline,
                    "risks", risks
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
    }

    @Test
    void shouldDisplayTerritoryPageForLaReunion() throws Exception {
        mockMvc.perform(get("/territoires/{slug}", Territory.REUNION.getSlug()))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
               .andExpect(content().string(containsString("<title>Territoire - La Réunion</title>")))
               .andExpect(content().string(containsString("<h1>La Réunion</h1>")))
               .andExpect(content().string(containsString("<h2>Interests</h2>")))
               .andExpect(content().string(containsString("<h2>Indicators</h2>")))
               .andExpect(content().string(containsString("<h3>Indicator1</h3>")))
               .andExpect(content().string(containsString("<h2>Species</h2>")))
               .andExpect(content().string(containsString("<h3>Specie1</h3>")))
               .andExpect(content().string(containsString("<h2>Ecosystems</h2>")))
               .andExpect(content().string(containsString("<h3>Ecosystem1</h3>")))
               .andExpect(content().string(containsString("<h2>Timeline</h2>")))
               .andExpect(content().string(containsString("<h3>1535</h3>")))
               .andExpect(content().string(containsString("<h2>Risks</h2>")))
               .andExpect(content().string(containsString("<h3>Risk1</h3>")))
               .andExpect(content().string(containsString("Saint-Pierre-Et-Miquelon</a>")))
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
               .andExpect(content().string(containsString("Découvrez les territoires</h1>")))
               .andExpect(content().string(containsString("Réunion")))
               .andExpect(content().string(containsString("presentation of reunion")))
               .andExpect(content().string(containsString("</html>")));
    }
}
