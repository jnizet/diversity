package fr.mnhn.diversity.act;

import static fr.mnhn.diversity.model.testing.ModelTestingUtil.image;
import static fr.mnhn.diversity.model.testing.ModelTestingUtil.text;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import fr.mnhn.diversity.common.thymeleaf.RequestDialect;
import fr.mnhn.diversity.ecogesture.EcogestureModel;
import fr.mnhn.diversity.ecogesture.Ecogesture;
import fr.mnhn.diversity.ecogesture.EcogestureRepository;
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

@WebMvcTest(ActController.class)
@Import(RequestDialect.class)
class ActControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PageRepository mockPageRepository;

    @MockBean
    private PageService mockPageService;

    @MockBean
    private EcogestureRepository mockEcogestureRepository;

    @BeforeEach
    void prepare() {
        Page page = new Page(1L,
                             ActModel.ACT_PAGE_NAME,
                             ActModel.ACT_PAGE_MODEL.getName(),
                             "Act together",
                             Collections.emptyList());
        when(mockPageRepository.findByNameAndModel(page.getName(), page.getModelName()))
            .thenReturn(Optional.of(page));
        when(mockPageService.buildPageContent(ActModel.ACT_PAGE_MODEL, page)).thenReturn(
            new PageContent(
                page,
                Map.of(
                    "header", Map.of(
                        "title", text("Agir ensemble"),
                        "subtitle", text("Hello"),
                        "background", image(201L)
                    ),
                    "ecogestures", Map.of(
                        "title", text("Ecogestes"),
                        "subtitle", text("Bla bla")
                    ),
                    "science", Map.of(
                        "title", text("Sciences participatives"),
                        "subtitle", text("Bla bla"),
                        "project", Map.of(
                            "title", text("Projet original"),
                            "description", text("Bla bla"),
                            "image", image(202L)
                        )
                    )
                )
            )
        );

        List<Ecogesture> ecogestures = List.of(
            new Ecogesture(1L, "g1"),
            new Ecogesture(2L, "g2"),
            new Ecogesture(3L, "g3"),
            new Ecogesture(4L, "g4"),
            new Ecogesture(5L, "g5"),
            new Ecogesture(6L, "g6")
        );
        when(mockEcogestureRepository.list()).thenReturn(ecogestures);

        Page ecogesturePage = new Page(2L,
                                       "g1",
                                       EcogestureModel.ECO_GESTURE_PAGE_MODEL.getName(),
                                       "Some Ecogesture",
                                       Collections.emptyList()
        );
        when(mockPageRepository.findByNameAndModel(any(), eq(EcogestureModel.ECO_GESTURE_PAGE_MODEL.getName())))
            .thenAnswer(invocation -> {
                String requestedPageName = invocation.getArgument(0, String.class);
                if (requestedPageName.compareTo("g3") <= 0) {
                    return Optional.of(ecogesturePage);
                } else {
                    return Optional.empty();
                }
            });
        when(mockPageService.buildPageContent(eq(EcogestureModel.ECO_GESTURE_PAGE_MODEL), any())).thenReturn(
            new PageContent(
                ecogesturePage,
                Map.of(
                    "presentation", Map.of(
                        "name", text("Corals"),
                        "category", text("Leisure"),
                        "image", image(1L)
                    )
                )
            )
        );
    }

    @Test
    void shouldDisplayActPage() throws Exception {
        mockMvc.perform(get("/agir-ensemble"))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
               .andExpect(content().string(containsString("<title>Act together</title>")))
               .andExpect(content().string(containsString("Agir ensemble</h1>")))
               .andExpect(content().string(containsString("Ecogestes</h2>")))
               .andExpect(content().string(containsString("Sciences participatives</h2>")))
               .andExpect(content().string(containsString("</html>")));
    }
}
