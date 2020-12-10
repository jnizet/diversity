package fr.mnhn.diversity.act;

import static fr.mnhn.diversity.model.testing.ModelTestingUtil.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ScienceController.class)
@Import({RequestDialect.class, MatomoConfig.class})
class ScienceControllerTest extends ControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PageRepository mockPageRepository;

    @MockBean
    private PageService mockPageService;

    @BeforeEach
    void prepare() {
        Page page = new Page(1L,
                             ActModel.SCIENCE_PAGE_NAME,
                             ActModel.SCIENCE_PAGE_MODEL.getName(),
                             "Participative science",
                             Collections.emptyList());
        when(mockPageRepository.findByNameAndModel(page.getName(), page.getModelName()))
            .thenReturn(Optional.of(page));
        when(mockPageService.buildPageContent(ActModel.SCIENCE_PAGE_MODEL, page)).thenReturn(
            new PageContent(
                page,
                Map.of(
                    "header", Map.of(
                        "title", text("Sciences participatives"),
                        "subtitle", text("Hello"),
                        "background", image(401L)
                    ),
                    "presentation", Map.of(
                        "title", text("Presentation"),
                        "description", text("Bla bla")
                    ),
                    "paragraphs", List.of(
                        Map.of(
                            "title", text("Paragraph 1"),
                            "text", text("Bla bla")
                        ),
                        Map.of(
                            "title", text("Paragraph 2"),
                            "text", text("Bla bla")
                        )
                    ),
                    "images", List.of(
                        Map.of(
                        "image", image(402L)
                        ),
                        Map.of(
                            "image", image(403L)
                        )
                    ),
                    "examples", Map.of(
                        "title", text("Example projects"),
                        "projects", List.of(
                            Map.of(
                                "title", text("Project 1"),
                                "description", text("Desc 1"),
                                "more", link("En savoir plus"),
                                "image", image(404L),
                                "subject", text("Subject 1"),
                                "actor", text("Actor 1"),
                                "target", text("Target 1")
                            ),
                            Map.of(
                                "title", text("Project 2"),
                                "description", text("Desc 2"),
                                "more", link("En savoir plus"),
                                "image", image(405L),
                                "subject", text("Subject 2"),
                                "actor", text("Actor 2"),
                                "target", text("Target 2")
                            )
                        )
                    ),
                    "application", Map.of(
                        "title", text("Agir avec son smart phone"),
                        "subtitle", text("C'est possible"),
                        "downloadLink", link("Je télécharge")
                    )
                )
            )
        );
    }

    @Test
    void shouldDisplaySciencePage() throws Exception {
        mockMvc.perform(get("/sciences-participatives"))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
               .andExpect(content().string(containsString("<title>Participative science</title>")))
               .andExpect(content().string(containsString("<p>Sciences participatives</p>")))
               .andExpect(content().string(containsString("<p>Paragraph 1</p>")))
               .andExpect(content().string(containsString("<p>Paragraph 2</p>")))
               .andExpect(content().string(containsString("<p>Example projects</p>")))
               .andExpect(content().string(containsString("<p>Project 1</p>")))
               .andExpect(content().string(containsString("<p>Project 2</p>")))
               .andExpect(content().string(containsString("</html>")));
    }
}
