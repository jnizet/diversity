package fr.mnhn.diversity.robot;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import fr.mnhn.diversity.about.AboutModel;
import fr.mnhn.diversity.ecogesture.EcogestureModel;
import fr.mnhn.diversity.home.HomeModel;
import fr.mnhn.diversity.indicator.IndicatorModel;
import fr.mnhn.diversity.model.BasicPage;
import fr.mnhn.diversity.model.PageRepository;
import fr.mnhn.diversity.territory.Territory;
import fr.mnhn.diversity.territory.TerritoryModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(RobotRestController.class)
class RobotRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PageRepository mockPageRepository;

    @BeforeEach
    void prepare() {
        when(mockPageRepository.findAllBasic()).thenReturn(
            List.of(
                new BasicPage(1L, Territory.GUADELOUPE.getSlug(), TerritoryModel.TERRITORY_PAGE_MODEL.getName(), ""),
                new BasicPage(2L, "i1", IndicatorModel.INDICATOR_PAGE_MODEL.getName(), ""),
                new BasicPage(3L, "e1", EcogestureModel.ECO_GESTURE_PAGE_MODEL.getName(), ""),
                new BasicPage(4L, "Home", HomeModel.HOME_PAGE_MODEL.getName(), ""),
                new BasicPage(5L, "About", AboutModel.ABOUT_PAGE_MODEL.getName(), "")
            )
        );
    }

    @Test
    void shouldGetRobotsTxt() throws Exception {
        mockMvc.perform(get("/robots.txt").with(req -> {
            req.setScheme("https");
            req.setServerName("indicateurs.mnhn.fr");
            req.setServerPort(443);
            return req;
        }))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.TEXT_PLAIN))
               .andExpect(content().string(containsString("Sitemap: https://indicateurs.mnhn.fr/sitemap.txt")));
    }

    @Test
    void shouldGetSitemap() throws Exception {
        mockMvc.perform(get("/sitemap.txt").with(req -> {
            req.setScheme("https");
            req.setServerName("indicateurs.mnhn.fr");
            req.setServerPort(443);
            return req;
        }))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.TEXT_PLAIN))
               .andExpect(content().string(containsString("https://indicateurs.mnhn.fr/")))
               .andExpect(content().string(containsString("https://indicateurs.mnhn.fr/apropos")))
               .andExpect(content().string(containsString("https://indicateurs.mnhn.fr/territoires/guadeloupe")))
               .andExpect(content().string(containsString("https://indicateurs.mnhn.fr/indicateurs/i1")))
               .andExpect(content().string(containsString("https://indicateurs.mnhn.fr/ecogestes/e1")));
    }
}
