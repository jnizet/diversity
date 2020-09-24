package fr.mnhn.diversity.indicator;

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
 * Tests for {@link IndicatorRestController}
 */
@WebMvcTest(IndicatorRestController.class)
@ActiveProfiles("test")
class IndicatorRestControllerTest {
    @MockBean
    private IndicatorRepository mockIndicatorRepository;

    @MockBean
    private IndicatorCategoryRepository mockIndicatorCategoryRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IndicatorRestController controller;

    @Captor
    private ArgumentCaptor<Indicator> indicatorArgumentCaptor;

    private IndicatorCategory category;
    private Indicator indicator;

    @BeforeEach
    void prepare() {
        category = new IndicatorCategory(54L, "Végétation");
        when(mockIndicatorCategoryRepository.findById(category.getId())).thenReturn(Optional.of(category));

        indicator = new Indicator(43L, "biom_43", "deforestation", List.of(category));
        when(mockIndicatorRepository.findById(indicator.getId())).thenReturn(Optional.of(indicator));
        when(mockIndicatorRepository.findByBiomId(indicator.getBiomId())).thenReturn(Optional.of(indicator));
        when(mockIndicatorRepository.findBySlug(indicator.getSlug())).thenReturn(Optional.of(indicator));
    }

    @Test
    void shouldList() throws Exception {
        when(mockIndicatorRepository.list()).thenReturn(List.of(indicator));

        mockMvc.perform(get("/api/indicators"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].id").value(indicator.getId()))
               .andExpect(jsonPath("$[0].biomId").value(indicator.getBiomId()))
               .andExpect(jsonPath("$[0].slug").value(indicator.getSlug()))
               .andExpect(jsonPath("$[0].categories.length()").value(indicator.getCategories().size()))
               .andExpect(jsonPath("$[0].categories[0].id").value(indicator.getCategories().get(0).getId()))
               .andExpect(jsonPath("$[0].categories[0].name").value(indicator.getCategories().get(0).getName()));
    }

    @Test
    void shouldDelete() throws Exception {
        when(mockIndicatorRepository.findById(42L)).thenReturn(Optional.of(indicator));

        mockMvc.perform(delete("/api/indicators/42"))
               .andExpect(status().isNoContent());

        verify(mockIndicatorRepository).delete(indicator);
    }

    @Test
    void shouldThrowIfNotExistWhenDelete() throws Exception {
        when(mockIndicatorRepository.findById(42L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/indicators/42"))
               .andExpect(status().isNotFound());
    }

    @Test
    void shouldGet() throws Exception {
        when(mockIndicatorRepository.findById(42L)).thenReturn(Optional.of(indicator));

        mockMvc.perform(get("/api/indicators/42"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(indicator.getId()))
               .andExpect(jsonPath("$.biomId").value(indicator.getBiomId()))
               .andExpect(jsonPath("$.slug").value(indicator.getSlug()))
               .andExpect(jsonPath("$.categories.length()").value(indicator.getCategories().size()))
               .andExpect(jsonPath("$.categories[0].id").value(indicator.getCategories().get(0).getId()))
               .andExpect(jsonPath("$.categories[0].name").value(indicator.getCategories().get(0).getName()));
    }

    @Test
    void shouldCreate() throws Exception {
        IndicatorCommandDTO command = new IndicatorCommandDTO(
            "biom_67",
            "surface-forêts",
            List.of(category.getId())
        );

        when(mockIndicatorRepository.create(any())).thenReturn(new Indicator(256L, command.getBiomId(), command.getSlug(), List.of(category)));

        mockMvc.perform(post("/api/indicators")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(command)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id").value(256L));

        verify(mockIndicatorRepository).create(indicatorArgumentCaptor.capture());

        Indicator createdIndicator = indicatorArgumentCaptor.getValue();
        assertThat(createdIndicator.getBiomId()).isEqualTo(command.getBiomId());
        assertThat(createdIndicator.getSlug()).isEqualTo(command.getSlug());
        assertThat(createdIndicator.getCategories()).containsExactly(category);
    }

    @Test
    void shouldThrowWhenCreatingWithAlreadyExistingBiomId() {
        IndicatorCommandDTO command = new IndicatorCommandDTO(
            indicator.getBiomId(),
            "surface-forêts",
            List.of(category.getId())
        );
        assertThatExceptionOfType(FunctionalException.class).isThrownBy(
            () -> controller.create(command)
        ).matches(e -> e.getCode() == FunctionalException.Code.INDICATOR_BIOM_ID_ALREADY_EXISTING);
    }

    @Test
    void shouldThrowWhenCreatingWithAlreadyExistingSlug() {
        IndicatorCommandDTO command = new IndicatorCommandDTO(
            "biom_12",
            indicator.getSlug(),
            List.of(category.getId())
        );
        assertThatExceptionOfType(FunctionalException.class).isThrownBy(
            () -> controller.create(command)
        ).matches(e -> e.getCode() == FunctionalException.Code.INDICATOR_SLUG_ALREADY_EXISTING);
    }

    @Test
    void shouldUpdate() throws Exception {
        IndicatorCommandDTO command = new IndicatorCommandDTO(
            "biom_92",
            "surface-forêts",
            List.of()
        );

        mockMvc.perform(put("/api/indicators/{id}", indicator.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(command)))
               .andExpect(status().isNoContent());

        verify(mockIndicatorRepository).update(indicatorArgumentCaptor.capture());

        Indicator updatedIndicator = indicatorArgumentCaptor.getValue();
        assertThat(updatedIndicator.getBiomId()).isEqualTo(command.getBiomId());
        assertThat(updatedIndicator.getSlug()).isEqualTo(command.getSlug());
        assertThat(updatedIndicator.getCategories()).isEmpty();
    }

    @Test
    void shouldThrowWhenUpdatingWithAlreadyExistingBiomId() {
        Indicator otherIndicator = new Indicator(indicator.getBiomId(), "other", List.of());
        when(mockIndicatorRepository.findByBiomId(otherIndicator.getBiomId())).thenReturn(Optional.of(otherIndicator));
        IndicatorCommandDTO command = new IndicatorCommandDTO(
            indicator.getBiomId(),
            "surface-forets",
            List.of()
        );
        assertThatExceptionOfType(FunctionalException.class).isThrownBy(
            () -> controller.update(indicator.getId(), command)
        ).matches(e -> e.getCode() == FunctionalException.Code.INDICATOR_BIOM_ID_ALREADY_EXISTING);
    }

    @Test
    void shouldNotThrowWhenUpdatingWithTheSameBiomId() {
        IndicatorCommandDTO command = new IndicatorCommandDTO(
            indicator.getBiomId(),
            "surface-forets",
            List.of()
        );
        assertThatCode(() -> controller.update(indicator.getId(), command)).doesNotThrowAnyException();
    }

    @Test
    void shouldThrowWhenUpdatingWithAlreadyExistingSlug() {
        Indicator otherIndicator = new Indicator("other", indicator.getSlug(), List.of());
        when(mockIndicatorRepository.findBySlug(otherIndicator.getSlug())).thenReturn(Optional.of(otherIndicator));
        IndicatorCommandDTO command = new IndicatorCommandDTO(
            "biom_92",
            indicator.getSlug(),
            List.of()
        );
        assertThatExceptionOfType(FunctionalException.class).isThrownBy(
            () -> controller.update(indicator.getId(), command)
        ).matches(e -> e.getCode() == FunctionalException.Code.INDICATOR_SLUG_ALREADY_EXISTING);
    }

    @Test
    void shouldNotThrowWhenUpdatingWithTheSameSlug() {
        IndicatorCommandDTO command = new IndicatorCommandDTO(
            "other",
            indicator.getSlug(),
            List.of()
        );
        assertThatCode(() -> controller.update(indicator.getId(), command)).doesNotThrowAnyException();
    }
}
