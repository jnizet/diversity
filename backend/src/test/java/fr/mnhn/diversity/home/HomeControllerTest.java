package fr.mnhn.diversity.home;

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
import fr.mnhn.diversity.matomo.MatomoConfig;
import fr.mnhn.diversity.model.Page;
import fr.mnhn.diversity.model.PageContent;
import fr.mnhn.diversity.model.PageRepository;
import fr.mnhn.diversity.model.PageService;
import fr.mnhn.diversity.territory.MapService;
import fr.mnhn.diversity.territory.MapTerritoryCard;
import fr.mnhn.diversity.territory.Territory;
import fr.mnhn.diversity.territory.TerritoryModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * MVC tests for {@link HomeController}
 * @author JB Nizet
 */
@WebMvcTest(HomeController.class)
@Import({RequestDialect.class, MatomoConfig.class})
class HomeControllerTest extends ControllerTest {

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
        Page page = new Page(1L, HomeModel.HOME_PAGE_NAME, HomeModel.HOME_PAGE_MODEL.getName(), "Diversité", Collections.emptyList());
        when(mockPageRepository.findByNameAndModel(HomeModel.HOME_PAGE_NAME, HomeModel.HOME_PAGE_MODEL.getName()))
            .thenReturn(Optional.of(page));
        when(mockPageService.buildPageContent(HomeModel.HOME_PAGE_MODEL, page)).thenReturn(
            new PageContent(
                page,
                Map.of(
                    "carousel", Map.of(
                        "title", text("Hello"),
                        "images", List.of(
                            Map.of("image", multiSizeImage(1L)),
                            Map.of("image", multiSizeImage(2L))
                        ),
                        "text", text("carousel text"),
                        "territoriesButton", text("carousel button")
                    ),
                    "presentation", Map.of(
                        "title", text("Presentation"),
                        "text", text("presentation text"),
                        "indicators", text("indicators text"),
                        "ecogestures", text("ecogestures text"),
                        "science", text("science text"),
                        "territories", text("territories text"),
                        "quote", text("quote text")
                    ),
                    "testimony", Map.of(
                        "title", text("Testimony"),
                        "image", multiSizeImage(4L),
                        "text", text("testimony text"),
                        "quote", text("testimony quote")
                    ),
                    "science", Map.of(
                        "title", text("Science"),
                        "image", multiSizeImage(5L),
                        "text", text("science text")
                    )
                )
            )
        );

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
    }

    @Test
    void shouldDisplayHomePage() throws Exception {
        mockMvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
            .andExpect(content().string(containsString("<title>Diversité</title>")))
            .andExpect(content().string(containsString("Hello")))
            .andExpect(content().string(containsString("Presentation")))
            .andExpect(content().string(containsString("Réunion")))
            .andExpect(content().string(containsString("presentation of reunion")))
            .andExpect(content().string(containsString("Testimony")))
            .andExpect(content().string(containsString("Science")))
            .andExpect(content().string(containsString("</html>")));
    }
}
