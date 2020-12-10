package fr.mnhn.diversity.legal;

import static fr.mnhn.diversity.model.testing.ModelTestingUtil.text;
import static org.hamcrest.CoreMatchers.containsString;
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

/**
 * MVC tests for {@link LegalTermsController}
 * @author JB Nizet
 */
@WebMvcTest(LegalTermsController.class)
@Import({RequestDialect.class, MatomoConfig.class})
class LegalTermsControllerTest extends ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PageRepository mockPageRepository;

    @MockBean
    private PageService mockPageService;

    @BeforeEach
    void prepare() {
        Page page = new Page(1L, LegalTermsModel.LEGAL_TERMS_PAGE_NAME, LegalTermsModel.LEGAL_TERMS_PAGE_MODEL.getName(), "Legal terms", Collections.emptyList());
        when(mockPageRepository.findByNameAndModel(page.getName(), LegalTermsModel.LEGAL_TERMS_PAGE_MODEL.getName()))
            .thenReturn(Optional.of(page));
        when(mockPageService.buildPageContent(LegalTermsModel.LEGAL_TERMS_PAGE_MODEL, page)).thenReturn(
            new PageContent(
                page,
                Map.of(
                    "title", text("Mentions légales"),
                    "paragraphs", List.of(
                        Map.of(
                            "title", text("Hello"),
                            "text", text("World")
                        )
                    )
                )
            )
        );
    }

    @Test
    void shouldDisplayLegalTermsPage() throws Exception {
        mockMvc.perform(get("/mentions-legales"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
            .andExpect(content().string(containsString("<title>Legal terms</title>")))
            .andExpect(content().string(containsString("<p>Mentions légales</p>")))
            .andExpect(content().string(containsString("<p>Hello</p>")))
            .andExpect(content().string(containsString("<p>World</p>")))
            .andExpect(content().string(containsString("</html>")));
    }
}
