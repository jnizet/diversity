package fr.mnhn.diversity.ecogesture;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.mnhn.diversity.common.exception.FunctionalException;
import fr.mnhn.diversity.model.PageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Tests for {@link EcogestureRestController}
 */
@WebMvcTest(EcogestureRestController.class)
@ActiveProfiles("test")
class EcogestureRestControllerTest {
    @MockBean
    private EcogestureRepository mockEcogestureRepository;

    @MockBean
    private PageRepository mockPageRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EcogestureRestController controller;

    @Captor
    private ArgumentCaptor<Ecogesture> categoryArgumentCaptor;

    private Ecogesture ecogesture;

    @BeforeEach
    void prepare() {
        ecogesture = new Ecogesture(54L, "trier-ses-dechets");
        when(mockEcogestureRepository.findById(ecogesture.getId())).thenReturn(Optional.of(ecogesture));
        when(mockEcogestureRepository.findBySlug(ecogesture.getSlug())).thenReturn(Optional.of(ecogesture));
    }

    @Test
    void shouldList() throws Exception {
        when(mockEcogestureRepository.list()).thenReturn(List.of(ecogesture));

        mockMvc.perform(get("/api/ecogestures"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].id").value(ecogesture.getId()))
               .andExpect(jsonPath("$[0].slug").value(ecogesture.getSlug()));
    }

    @Test
    void shouldDelete() throws Exception {
        when(mockEcogestureRepository.findById(42L)).thenReturn(Optional.of(ecogesture));

        mockMvc.perform(delete("/api/ecogestures/42"))
               .andExpect(status().isNoContent());

        verify(mockEcogestureRepository).delete(ecogesture);
        verify(mockPageRepository).deleteByNameAndModel(ecogesture.getSlug(), EcoGestureModel.ECO_GESTURE_PAGE_MODEL.getName());
    }

    @Test
    void shouldThrowIfNotExistWhenDelete() throws Exception {
        when(mockEcogestureRepository.findById(42L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/ecogestures/42"))
               .andExpect(status().isNotFound());
    }

    @Test
    void shouldGet() throws Exception {
        when(mockEcogestureRepository.findById(42L)).thenReturn(Optional.of(ecogesture));

        mockMvc.perform(get("/api/ecogestures/42"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(ecogesture.getId()))
               .andExpect(jsonPath("$.slug").value(ecogesture.getSlug()));
    }

    @Test
    void shouldCreate() throws Exception {
        EcogestureCommandDTO command = new EcogestureCommandDTO(
            "proteger-les-coraux"
        );

        when(mockEcogestureRepository.create(any())).thenReturn(new Ecogesture(256L, command.getSlug()));

        mockMvc.perform(post("/api/ecogestures")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(command)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id").value(256L));

        verify(mockEcogestureRepository).create(categoryArgumentCaptor.capture());

        Ecogesture createdEcogesture = categoryArgumentCaptor.getValue();
        assertThat(createdEcogesture.getSlug()).isEqualTo(command.getSlug());
    }

    @Test
    void shouldThrowWhenCreatingWithAlreadyExistingSlug() {
        EcogestureCommandDTO command = new EcogestureCommandDTO(
            ecogesture.getSlug()
        );
        assertThatExceptionOfType(FunctionalException.class).isThrownBy(
            () -> controller.create(command)
        ).matches(e -> e.getCode() == FunctionalException.Code.ECOGESTURE_SLUG_ALREADY_EXISTING);
    }

    @Test
    void shouldUpdate() throws Exception {
        EcogestureCommandDTO command = new EcogestureCommandDTO(
            "proteger-les-coraux"
        );

        mockMvc.perform(put("/api/ecogestures/{id}", ecogesture.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(command)))
               .andExpect(status().isNoContent());

        verify(mockEcogestureRepository).update(ecogesture.withSlug(command.getSlug()));
        verify(mockPageRepository).updateName(ecogesture.getSlug(), EcoGestureModel.ECO_GESTURE_PAGE_MODEL.getName(), command.getSlug());
    }

    @Test
    void shouldThrowWhenUpdatingWithAlreadyExistingSlug() {
        Ecogesture otherEcogesture = new Ecogesture(ecogesture.getSlug());
        when(mockEcogestureRepository.findBySlug(otherEcogesture.getSlug())).thenReturn(Optional.of(otherEcogesture));
        EcogestureCommandDTO command = new EcogestureCommandDTO(
            ecogesture.getSlug()
        );
        assertThatExceptionOfType(FunctionalException.class).isThrownBy(
            () -> controller.update(ecogesture.getId(), command)
        ).matches(e -> e.getCode() == FunctionalException.Code.ECOGESTURE_SLUG_ALREADY_EXISTING);
    }

    @Test
    void shouldNotThrowWhenUpdatingWithTheSameSlug() {
        EcogestureCommandDTO command = new EcogestureCommandDTO(
            ecogesture.getSlug()
        );
        assertThatCode(() -> controller.update(ecogesture.getId(), command)).doesNotThrowAnyException();
    }
}
