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

import fr.mnhn.diversity.model.Page;
import fr.mnhn.diversity.model.PageRepository;
import fr.mnhn.diversity.model.PageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * MVC tests for {@link TerritoryController}
 */
@WebMvcTest(TerritoryController.class)
class TerritoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PageRepository mockPageRepository;

    @MockBean
    private PageService mockPageService;

    @BeforeEach
    void prepare() {
        Page page = new Page(1L, "reunion", TerritoryModel.TERRITORY_PAGE_MODEL.getName(), Collections.emptyList());
        when(mockPageRepository.findByNameAndModel("reunion", TerritoryModel.TERRITORY_PAGE_MODEL.getName())).thenReturn(Optional.of(page));
        when(mockPageService.buildPage(TerritoryModel.TERRITORY_PAGE_MODEL, page)).thenReturn(
                Map.of(
                        "name", text("La Réunion"),
                        "identity", Map.of(
                                "title", text("La Réunion"),
                                "presentation", text("presentation"),
                                "infography", image(1L)
                        ),
                        "interests", Map.of(
                                "title", text("Interests"),
                                "images", List.of(
                                        Map.of("image", image(2L)),
                                        Map.of("image", image(3L))
                                )
                        ),
                        "indicators", Map.of(
                                "title", text("Indicators"),
                                "indicators", List.of(
                                        Map.of(
                                                "name", text("Indicator1"),
                                                "value", text("12"),
                                                "image", image(4L),
                                                "link", link("12")
                                        )
                                )
                        ),
                        "species", Map.of(
                                "title", text("Species"),
                                "species", List.of(
                                        Map.of(
                                                "name", text("Specie1"),
                                                "description", text("specie1"),
                                                "image", image(5L)
                                        )
                                )
                        ),
                        "ecosystems", Map.of(
                                "title", text("Ecosystems"),
                                "ecosystems", List.of(
                                        Map.of(
                                                "name", text("Ecosystem1"),
                                                "description", text("ecosystem1"),
                                                "image", image(6L)
                                        )
                                )
                        ),
                        "timeline", Map.of(
                                "title", text("Timeline"),
                                "events", List.of(
                                        Map.of(
                                                "name", text("1535"),
                                                "description", text("desc 1535")
                                        )
                                )
                        ),
                        "risks", Map.of(
                                "title", text("Risks"),
                                "risks", List.of(
                                        Map.of(
                                                "name", text("Risk1"),
                                                "description", text("risk1"),
                                                "image", image(7L)
                                        )
                                )
                        ),
                        "other", Map.of(
                                "image", image(8L),
                                "link", link("other")
                        )
                )
        );
    }

    @Test
    void shouldDisplayTerritoryPageForLaReunion() throws Exception {
        mockMvc.perform(get("/territoires/reunion"))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
               .andExpect(content().string(containsString("<title>La Réunion</title>")))
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
               .andExpect(content().string(containsString("<h3>Risk1</h3>")));
    }
}
