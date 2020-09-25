package fr.mnhn.diversity.home;

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
    }

    @Test
    void shouldDisplayHomePage() throws Exception {
        mockMvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
            .andExpect(content().string(containsString("<title>Diversité</title>")))
            .andExpect(content().string(containsString("Hello")))
            .andExpect(content().string(containsString("Presentation")))
            .andExpect(content().string(containsString("Testimony")))
            .andExpect(content().string(containsString("Science")))
            .andExpect(content().string(containsString("</html>")));
    }
}
