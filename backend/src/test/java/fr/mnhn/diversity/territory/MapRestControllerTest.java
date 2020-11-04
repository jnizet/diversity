package fr.mnhn.diversity.territory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import fr.mnhn.diversity.model.BasicPage;
import fr.mnhn.diversity.model.PageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MapRestController.class)
class MapRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PageRepository mockPageRepository;

    @Test
    void shouldGetMap() throws Exception {
        Zone zone = Zone.ANTILLES;
        Territory reunion = Territory.REUNION;
        int reunionIndex = Arrays.asList(Territory.values()).indexOf(reunion) - 1;
        Territory guageloupe = Territory.GUADELOUPE;
        int guadeloupeIndex = Arrays.asList(Territory.values()).indexOf(guageloupe) - 1;
        int guyaneIndex = Arrays.asList(Territory.values()).indexOf(Territory.GUYANE) - 1;

        String territoryModelName = TerritoryModel.TERRITORY_PAGE_MODEL.getName();
        when(mockPageRepository.findBasicByModel(territoryModelName)).thenReturn(
            List.of(
                new BasicPage(1L, reunion.getSlug(), territoryModelName, ""),
                new BasicPage(2L, guageloupe.getSlug(), territoryModelName, "")
            )
        );

        mockMvc.perform(get("/map"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.zones.length()").value(Zone.values().length))
               .andExpect(jsonPath("$.zones[0].zone").value(zone.name()))
               .andExpect(jsonPath("$.zones[0].name").value(zone.getName()))
               .andExpect(jsonPath("$.zones[0].coordinates.x").value(zone.getCoordinates().getX()))
               .andExpect(jsonPath("$.zones[0].coordinates.y").value(zone.getCoordinates().getY()))
               .andExpect(jsonPath("$.zones[0].active").value(true))
               .andExpect(jsonPath("$.territories.length()").value(Territory.values().length - 1))
               .andExpect(jsonPath("$.territories[" + reunionIndex + "].territory").value(reunion.name()))
               .andExpect(jsonPath("$.territories[" + reunionIndex + "].name").value(reunion.getName()))
               .andExpect(jsonPath("$.territories[" + reunionIndex + "].coordinates.x").value(reunion.getCoordinates().getX()))
               .andExpect(jsonPath("$.territories[" + reunionIndex + "].coordinates.y").value(reunion.getCoordinates().getY()))
               .andExpect(jsonPath("$.territories[" + reunionIndex + "].zone").isEmpty())
               .andExpect(jsonPath("$.territories[" + reunionIndex + "].slug").value(reunion.getSlug()))
               .andExpect(jsonPath("$.territories[" + reunionIndex + "].active").value(true))
               .andExpect(jsonPath("$.territories[" + guadeloupeIndex + "].zone").value(guageloupe.getZone().name()))
               .andExpect(jsonPath("$.territories[" + guyaneIndex + "].active").value(false));
    }
}
