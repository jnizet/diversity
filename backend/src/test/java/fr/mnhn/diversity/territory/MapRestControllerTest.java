package fr.mnhn.diversity.territory;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MapRestController.class)
class MapRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldGetMap() throws Exception {
        Zone zone = Zone.ANTILLES;
        Territory territory = Territory.REUNION;
        Territory territory2 = Territory.GUADELOUPE;
        mockMvc.perform(get("/map"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.zones.length()").value(Zone.values().length))
               .andExpect(jsonPath("$.zones[0].zone").value(zone.name()))
               .andExpect(jsonPath("$.zones[0].name").value(zone.getName()))
               .andExpect(jsonPath("$.zones[0].coordinates.x").value(zone.getCoordinates().getX()))
               .andExpect(jsonPath("$.zones[0].coordinates.y").value(zone.getCoordinates().getY()))
               .andExpect(jsonPath("$.territories.length()").value(Territory.values().length - 1))
               .andExpect(jsonPath("$.territories[0].territory").value(territory.name()))
               .andExpect(jsonPath("$.territories[0].name").value(territory.getName()))
               .andExpect(jsonPath("$.territories[0].coordinates.x").value(territory.getCoordinates().getX()))
               .andExpect(jsonPath("$.territories[0].coordinates.y").value(territory.getCoordinates().getY()))
               .andExpect(jsonPath("$.territories[0].zone").isEmpty())
               .andExpect(jsonPath("$.territories[0].slug").value(territory.getSlug()))
               .andExpect(jsonPath("$.territories[1].zone").value(territory2.getZone().name()));
    }
}
