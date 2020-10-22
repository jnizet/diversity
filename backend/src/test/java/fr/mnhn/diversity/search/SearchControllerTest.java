package fr.mnhn.diversity.search;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import fr.mnhn.diversity.about.AboutModel;
import fr.mnhn.diversity.act.ActModel;
import fr.mnhn.diversity.ecogesture.EcoGestureModel;
import fr.mnhn.diversity.home.HomeModel;
import fr.mnhn.diversity.indicator.IndicatorModel;
import fr.mnhn.diversity.territory.TerritoryModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

/**
 * MVC tests for {@link SearchController}
 * @author JB Nizet
 */
@WebMvcTest(SearchController.class)
public class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SearchRepository mockSearchRepository;

    @Test
    void shouldSearch() throws Exception {
        List<PageSearchResult> searchResults = List.of(
            new PageSearchResult(1L,
                                 "Home",
                                 HomeModel.HOME_PAGE_MODEL.getName(),
                                 "Accueil",
                                 "<b>Compteurs</b> de biodiversité",
                                 null),
            new PageSearchResult(2L,
                                 "About",
                                 AboutModel.ABOUT_PAGE_MODEL.getName(),
                                 "A propos",
                                 "<b>compteurs</b>",
                                 null),
            new PageSearchResult(3L,
                                 "Ecogestures",
                                 EcoGestureModel.ECO_GESTURE_HOME_PAGE_MODEL.getName(),
                                 "Ecogestes",
                                 "<b>compteurs</b",
                                 null),
            new PageSearchResult(4L,
                                 "coral",
                                 EcoGestureModel.ECO_GESTURE_PAGE_MODEL.getName(),
                                 "Protéger le corail",
                                 "<b>compteurs</b",
                                 null),
            new PageSearchResult(5L,
                                 "reunion",
                                 TerritoryModel.TERRITORY_PAGE_MODEL.getName(),
                                 "La Réunion",
                                 "<b>compteurs</b",
                                 null),
            new PageSearchResult(6L,
                                 "especes-envahissantes",
                                 IndicatorModel.INDICATOR_PAGE_MODEL.getName(),
                                 "Espèces envahissantes",
                                 "<b>compteurs</b",
                                 null),
            new PageSearchResult(7L,
                                 ActModel.ACT_PAGE_NAME,
                                 ActModel.ACT_PAGE_MODEL.getName(),
                                 "Agir ensemble",
                                 "<b>compteurs</b",
                                 null),
            new PageSearchResult(8L,
                                 ActModel.SCIENCE_PAGE_NAME,
                                 ActModel.SCIENCE_PAGE_MODEL.getName(),
                                 "Sciences participatives",
                                 "<b>compteurs</b",
                                 null)
        );
        when(mockSearchRepository.search("compteur")).thenReturn(searchResults);

        ResultActions resultActions =
            mockMvc.perform(get("/recherche?texte=compteur"))
                   .andExpect(status().isOk())
                   .andExpect(content().string(containsString("Recherche&nbsp;: <span>compteur</span>")))
                   .andExpect(content().string(containsString("</html>")));

        for (PageSearchResult searchResult : searchResults) {
            resultActions.andExpect(content().string(containsString(searchResult.getTitle())))
                         .andExpect(content().string(containsString(searchResult.getHighlight())));
        }

        resultActions.andExpect(content().string(containsString("href=\"/\"")))
                     .andExpect(content().string(containsString("href=\"/apropos\"")))
                     .andExpect(content().string(containsString("href=\"/ecogestes\"")))
                     .andExpect(content().string(containsString("href=\"/ecogestes/coral\"")))
                     .andExpect(content().string(containsString("href=\"/territoires/reunion\"")))
                     .andExpect(content().string(containsString("href=\"/indicateurs/especes-envahissantes\"")))
                     .andExpect(content().string(containsString("href=\"/agir-ensemble\"")))
                     .andExpect(content().string(containsString("href=\"/sciences-participatives\"")));
    }
}
