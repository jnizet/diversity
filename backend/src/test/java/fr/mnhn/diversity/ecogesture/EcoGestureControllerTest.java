package fr.mnhn.diversity.ecogesture;

import static fr.mnhn.diversity.model.testing.ModelTestingUtil.image;
import static fr.mnhn.diversity.model.testing.ModelTestingUtil.text;
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
 * MVC tests for {@link EcoGestureController}
 * @author JB Nizet
 */
@WebMvcTest(EcoGestureController.class)
class EcoGestureControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PageRepository mockPageRepository;

    @MockBean
    private PageService mockPageService;

    @BeforeEach
    void prepare() {
        Page page = new Page(1L, "corals", EcoGestureModel.ECO_GESTURE_PAGE_MODEL.getName(), Collections.emptyList());
        when(mockPageRepository.findByNameAndModel(page.getName(), page.getModelName()))
            .thenReturn(Optional.of(page));
        when(mockPageService.buildPage(EcoGestureModel.ECO_GESTURE_PAGE_MODEL, page)).thenReturn(
            Map.of(
                "presentation", Map.of(
                    "name", text("Corals"),
                    "category", text("Leisure"),
                    "description", text("Description"),
                    "image", image(1L),
                    "file", image(2L)
                ),
                "understand", Map.of(
                    "title", text("Understand"),
                    "text", text("understand text"),
                    "image", image(3L)
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
            )
        );
    }

    @Test
    void shouldDisplayEcoGesturePage() throws Exception {
        mockMvc.perform(get("/ecogestes/corals"))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
               .andExpect(content().string(containsString("<title>Écogeste</title>")))
               .andExpect(content().string(containsString("<h1>Corals</h1>")))
               .andExpect(content().string(containsString("<h2>Understand</h2>")))
               .andExpect(content().string(containsString("<h2>Action</h2>")));
    }
}
