package fr.mnhn.diversity.web.home;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import fr.mnhn.diversity.repository.Element;
import fr.mnhn.diversity.repository.Image;
import fr.mnhn.diversity.repository.Page;
import fr.mnhn.diversity.repository.PageRepository;
import fr.mnhn.diversity.repository.Text;
import fr.mnhn.diversity.service.page.PageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * MVC tests for {@link HomeController}
 * @author JB Nizet
 */
@WebMvcTest(HomeController.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PageRepository mockPageRepository;

    @MockBean
    private PageService mockPageService;

    @BeforeEach
    void prepare() {
        Page page = new Page(1L, HomeModel.HOME_PAGE_NAME, HomeModel.HOME_PAGE_MODEL.getName(), Collections.emptyList());
        when(mockPageRepository.findByName(HomeModel.HOME_PAGE_NAME)).thenReturn(Optional.of(page));
        when(mockPageService.buildPage(HomeModel.HOME_PAGE_MODEL, page)).thenReturn(
            Map.of(
                "carousel", Map.of(
                    "title", text("Hello"),
                    "images", List.of(
                        Map.of("image", image("carousel1")),
                        Map.of("image", image("carousel2"))
                    ),
                    "text", text("carousel text"),
                    "territoriesButton", text("carousel button")
                ),
                "presentation", Map.of(
                    "title", text("Presentation"),
                    "image", image("presentation"),
                    "text", text("presentation text"),
                    "more", text("More")
                ),
                "indicators", Map.of(
                    "title", text("Indicators"),
                    "image", image("indicators"),
                    "text", text("indicators text")
                ),
                "science", Map.of(
                    "title", text("Science"),
                    "image", image("science"),
                    "text", text("science text")
                )
            )
        );
    }

    @Test
    void shouldDisplayHomePage() throws Exception {
        mockMvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
            .andExpect(content().string(containsString("<title>Diversité</title>")))
            .andExpect(content().string(containsString("<h1>Hello</h1>")))
            .andExpect(content().string(containsString("<h2>Presentation</h2>")))
            .andExpect(content().string(containsString("<h2>Indicators</h2>")))
            .andExpect(content().string(containsString("<h2>Science</h2>")));
    }

    private Text text(String text) {
        return Element.text(0L, "text", text);
    }

    private Image image(String imageId) {
        return Element.image(0L, "image", imageId, "alt");
    }
}
