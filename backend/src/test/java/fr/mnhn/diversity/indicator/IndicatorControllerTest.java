package fr.mnhn.diversity.indicator;

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
 * MVC tests for {@link IndicatorController}
 */
@WebMvcTest(IndicatorController.class)
class IndicatorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PageRepository mockPageRepository;

    @MockBean
    private PageService mockPageService;

    @BeforeEach
    void prepare() {
        Page page = new Page(1L, "especes-envahissantes", IndicatorModel.INDICATOR_PAGE_MODEL.getName(), Collections.emptyList());
        when(mockPageRepository.findByNameAndModel("especes-envahissantes", IndicatorModel.INDICATOR_PAGE_MODEL.getName())).thenReturn(Optional.of(page));
        when(mockPageService.buildPageContent(IndicatorModel.INDICATOR_PAGE_MODEL, page)).thenReturn(
                Map.of(
                        "name", text("Espèces envahissantes"),
                        "presentation", Map.of(
                                "category", text("Espèces"),
                                "value", text("60"),
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
        );
    }

    @Test
    void shouldDisplayIndicatorPageForInvasiveSpecies() throws Exception {
        mockMvc.perform(get("/indicateurs/especes-envahissantes"))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
               .andExpect(content().string(containsString("<title>Espèces envahissantes</title>")))
               .andExpect(content().string(containsString("<h1>Espèces envahissantes</h1>")))
               .andExpect(content().string(containsString("<h2>Comprendre</h2>")))
               .andExpect(content().string(containsString("<h2>Indicateurs</h2>")))
               .andExpect(content().string(containsString("<h3>Réunion</h3>")))
               .andExpect(content().string(containsString("<h3>Martinique</h3>")))
               .andExpect(content().string(containsString("<h2>Ecogestes</h2>")))
               .andExpect(content().string(containsString("<h3>Ecogesture1</h3>")));
    }
}
