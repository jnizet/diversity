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
 * MVC tests for {@link AboutController}
 * @author JB Nizet
 */
@WebMvcTest(AboutController.class)
@Import(RequestDialect.class)
class AboutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PageRepository mockPageRepository;

    @MockBean
    private PageService mockPageService;

    @BeforeEach
    void prepare() {
        Page page = new Page(1L,
                             AboutModel.ABOUT_PAGE_NAME,
                             AboutModel.ABOUT_PAGE_MODEL.getName(),
                             "About diversity",
                             Collections.emptyList());
        when(mockPageRepository.findByNameAndModel(AboutModel.ABOUT_PAGE_NAME, AboutModel.ABOUT_PAGE_MODEL.getName()))
            .thenReturn(Optional.of(page));
        when(mockPageService.buildPageContent(AboutModel.ABOUT_PAGE_MODEL, page)).thenReturn(
            new PageContent(
                page,
                Map.of(
                    "header", Map.of(
                        "title", text("About"),
                        "subtitle", text("Hello"),
                        "background", image(1L)
                    ),
                    "goal1", Map.of(
                        "title", text("Indicateurs"),
                        "description", text("Bla bla"),
                        "image", image(11L)
                    ),
                    "goal2", Map.of(
                        "title", text("Territoires"),
                        "description", text("Bla bla"),
                        "image", image(12L),
                        "quote", text("Quote text"),
                    "quoteImage", image(14L)
                    ),
                    "goal3", Map.of(
                        "title", text("Agir ensemble"),
                        "description", text("Bla bla"),
                        "image", image(13L)
                    ),
                    "partners", Map.of(
                        "title", text("Partners"),
                        "partners", List.of(
                            Map.of("logo", image(3L),
                                   "url", text("https://google.com"))
                        )
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
            .andExpect(content().string(containsString("<title>About diversity</title>")))
            .andExpect(content().string(containsString("About</h1>")))
            .andExpect(content().string(containsString("Indicateurs</h2>")))
            .andExpect(content().string(containsString("Territoires</h2>")))
            .andExpect(content().string(containsString("Agir ensemble</h2>")))
            .andExpect(content().string(containsString("Partners</h2>")))
            .andExpect(content().string(containsString("</html>")));
    }
}
