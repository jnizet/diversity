package fr.mnhn.diversity.model.rest;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import fr.mnhn.diversity.ecogesture.EcogestureModel;
import fr.mnhn.diversity.ecogesture.Ecogesture;
import fr.mnhn.diversity.ecogesture.EcogestureRepository;
import fr.mnhn.diversity.home.HomeModel;
import fr.mnhn.diversity.indicator.Indicator;
import fr.mnhn.diversity.indicator.IndicatorModel;
import fr.mnhn.diversity.indicator.IndicatorRepository;
import fr.mnhn.diversity.model.BasicPage;
import fr.mnhn.diversity.model.PageRepository;
import fr.mnhn.diversity.territory.Territory;
import fr.mnhn.diversity.territory.TerritoryModel;
import fr.mnhn.diversity.territory.Zone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * MVC tests for {@link PageLinkRestController}
 */
@WebMvcTest(PageLinkRestController.class)
class PageLinkRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PageRepository mockPageRepository;

    @MockBean
    private IndicatorRepository mockIndicatorRepository;

    @MockBean
    private EcogestureRepository mockEcogestureRepository;

    @BeforeEach
    void prepare() {
        when(mockPageRepository.findBasicByNameAndModel(HomeModel.HOME_PAGE_NAME, HomeModel.HOME_PAGE_MODEL.getName()))
            .thenReturn(Optional.of(new BasicPage(1L, HomeModel.HOME_PAGE_NAME, HomeModel.HOME_PAGE_MODEL.getName(), "Accueil")));
        when(mockPageRepository.findBasicByModel(IndicatorModel.INDICATOR_PAGE_MODEL.getName()))
            .thenReturn(List.of(new BasicPage(11L, "i1", IndicatorModel.INDICATOR_PAGE_MODEL.getName(), "Indicateur i1")));
        when(mockPageRepository.findBasicByModel(TerritoryModel.TERRITORY_PAGE_MODEL.getName()))
            .thenReturn(List.of(new BasicPage(21L, Territory.GUADELOUPE.getSlug(), TerritoryModel.TERRITORY_PAGE_MODEL.getName(), "Guadeloupe")));
        when(mockPageRepository.findBasicByModel(EcogestureModel.ECO_GESTURE_PAGE_MODEL.getName()))
            .thenReturn(List.of(new BasicPage(31L, "e1", EcogestureModel.ECO_GESTURE_PAGE_MODEL.getName(), "Ecogeste e1")));
        when(mockPageRepository.findBasicByModel(TerritoryModel.ZONE_PAGE_MODEL.getName()))
            .thenReturn(List.of(new BasicPage(41L, Zone.ANTILLES.getSlug(), TerritoryModel.ZONE_PAGE_MODEL.getName(), "Antilles")));

        when(mockIndicatorRepository.list()).thenReturn(
            List.of(
                new Indicator(52L, "biom-11", "i1", false),
                new Indicator(64L, "biom-12", "i2", false)
            )
        );

        when(mockEcogestureRepository.list()).thenReturn(
            List.of(
                new Ecogesture(34L, "e1"),
                new Ecogesture(45L, "e2")
            )
        );
    }

    @Test
    void shouldGetPageLinks() throws Exception {
        int reunionIndex = Arrays.asList(Territory.values()).indexOf(Territory.REUNION) - 1;
        int guadeloupeIndex = Arrays.asList(Territory.values()).indexOf(Territory.GUADELOUPE) - 1;
        int antillesIndex = Arrays.asList(Zone.values()).indexOf(Zone.ANTILLES) - 1;
        mockMvc.perform(get("/api/pages/links"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.staticPageLinks.length()").value(9))
               .andExpect(jsonPath("$.staticPageLinks[0].id").value(1L))
               .andExpect(jsonPath("$.staticPageLinks[0].name").value(HomeModel.HOME_PAGE_NAME))
               .andExpect(jsonPath("$.staticPageLinks[0].modelName").value(HomeModel.HOME_PAGE_MODEL.getName()))
               .andExpect(jsonPath("$.staticPageLinks[0].title").value("Accueil"))
               .andExpect(jsonPath("$.staticPageLinks[1].id").value(nullValue()))
               .andExpect(jsonPath("$.staticPageLinks[1].title").value(nullValue()))
               .andExpect(jsonPath("$.indicatorPageLinks.length()").value(2))
               .andExpect(jsonPath("$.indicatorPageLinks[0].id").value(11L))
               .andExpect(jsonPath("$.indicatorPageLinks[0].name").value("i1"))
               .andExpect(jsonPath("$.indicatorPageLinks[1].id").value(nullValue()))
               .andExpect(jsonPath("$.indicatorPageLinks[1].name").value("i2"))
               .andExpect(jsonPath("$.territoryPageLinks.length()").value(Territory.values().length - 1))
               .andExpect(jsonPath("$.territoryPageLinks[" + reunionIndex + "].id").value(nullValue()))
               .andExpect(jsonPath("$.territoryPageLinks[" + reunionIndex + "].name").value(Territory.REUNION.getSlug()))
               .andExpect(jsonPath("$.territoryPageLinks["  + guadeloupeIndex + "].id").value(21L))
               .andExpect(jsonPath("$.territoryPageLinks["  + guadeloupeIndex + "].name").value(Territory.GUADELOUPE.getSlug()))
               .andExpect(jsonPath("$.zonePageLinks.length()").value(Zone.values().length))
               .andExpect(jsonPath("$.zonePageLinks["  + antillesIndex + "].id").value(41L))
               .andExpect(jsonPath("$.zonePageLinks["  + antillesIndex + "].name").value(Zone.ANTILLES.getSlug()))
               .andExpect(jsonPath("$.ecogesturePageLinks.length()").value(2))
               .andExpect(jsonPath("$.ecogesturePageLinks[0].id").value(31L))
               .andExpect(jsonPath("$.ecogesturePageLinks[0].name").value("e1"))
               .andExpect(jsonPath("$.ecogesturePageLinks[1].id").value(nullValue()))
               .andExpect(jsonPath("$.ecogesturePageLinks[1].name").value("e2"));
    }
}
