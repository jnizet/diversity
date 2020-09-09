package fr.mnhn.diversity.about;

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
 * MVC tests for {@link AboutController}
 * @author JB Nizet
 */
@WebMvcTest(AboutController.class)
class AboutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PageRepository mockPageRepository;

    @MockBean
    private PageService mockPageService;

    @BeforeEach
    void prepare() {
        Page page = new Page(1L, AboutModel.ABOUT_PAGE_NAME, AboutModel.ABOUT_PAGE_MODEL.getName(), Collections.emptyList());
        when(mockPageRepository.findByName(AboutModel.ABOUT_PAGE_NAME)).thenReturn(Optional.of(page));
        when(mockPageService.buildPage(AboutModel.ABOUT_PAGE_MODEL, page)).thenReturn(
            Map.of(
                "header", Map.of(
                    "title", text("About"),
                    "subtitle", text("Hello"),
                    "background", image("background"),
                    "paragraphs", List.of(
                        Map.of("text", text("paragraph1")),
                        Map.of("text", text("paragraph2"))
                    )
                ),
                "carousel", List.of(
                    Map.of(
                        "title", text("Slide1"),
                        "text", text("Text1"),
                        "link", link("Link1"),
                        "image", image("Image1")
                    )
                ),
                "partners", Map.of(
                    "title", text("Partners"),
                    "partners", List.of(
                        Map.of("logo", image("Logo1"))
                    )
                )
            )
        );
    }

    @Test
    void shouldDisplayAboutPage() throws Exception {
        mockMvc.perform(get("/apropos"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
            .andExpect(content().string(containsString("<title>À propos</title>")))
            .andExpect(content().string(containsString("<h1>About</h1>")))
            .andExpect(content().string(containsString("<h2>Slide1</h2>")))
            .andExpect(content().string(containsString("<h2>Partners</h2>")));
    }
}
