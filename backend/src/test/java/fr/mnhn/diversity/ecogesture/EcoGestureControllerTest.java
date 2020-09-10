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
import fr.mnhn.diversity.model.PageContent;
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
        Page coralsPage = new Page(1L,
                                   "corals",
                                   EcoGestureModel.ECO_GESTURE_PAGE_MODEL.getName(),
                                   "Protect corals",
                                   Collections.emptyList());
        when(mockPageRepository.findByNameAndModel(coralsPage.getName(), coralsPage.getModelName()))
            .thenReturn(Optional.of(coralsPage));

        Page homePage = new Page(2L,
                                 EcoGestureModel.ECO_GESTURE_HOME_PAGE_NAME,
                                 EcoGestureModel.ECO_GESTURE_HOME_PAGE_MODEL.getName(),
                                 "Écogestes",
                                 Collections.emptyList());
        when(mockPageRepository.findByNameAndModel(homePage.getName(), homePage.getModelName()))
            .thenReturn(Optional.of(homePage));

        List<Page> gesturePages = List.of(coralsPage);
        when(mockPageRepository.findByModel(EcoGestureModel.ECO_GESTURE_PAGE_MODEL.getName()))
            .thenReturn(gesturePages);

        Map<String, Object> coralsContent = Map.of(
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
        );

        Map<String, Object> homeContent = Map.of(
            "title", text("Ecogestures"),
            "presentation", text("Presentation"),
            "image", image(1L)
        );

        when(mockPageService.buildPageContent(EcoGestureModel.ECO_GESTURE_HOME_PAGE_MODEL, homePage))
            .thenReturn(new PageContent(homePage, homeContent));
        when(mockPageService.buildPageContent(EcoGestureModel.ECO_GESTURE_PAGE_MODEL, coralsPage))
            .thenReturn(new PageContent(coralsPage, coralsContent));;
    }

    @Test
    void shouldDisplayEcogestureHomePage() throws Exception {
        mockMvc.perform(get("/ecogestes"))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
               .andExpect(content().string(containsString("<title>Écogestes</title>")))
               .andExpect(content().string(containsString("<h1>Ecogestures</h1>")))
               .andExpect(content().string(containsString("<p>Corals</p>")));
    }

    @Test
    void shouldDisplayEcoGesturePage() throws Exception {
        mockMvc.perform(get("/ecogestes/corals"))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
               .andExpect(content().string(containsString("<title>Protect corals</title>")))
               .andExpect(content().string(containsString("<h1>Corals</h1>")))
               .andExpect(content().string(containsString("<h2>Understand</h2>")))
               .andExpect(content().string(containsString("<h2>Action</h2>")));
    }
}
