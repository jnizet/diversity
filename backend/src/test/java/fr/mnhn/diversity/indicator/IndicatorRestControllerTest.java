package fr.mnhn.diversity.indicator;

import static fr.mnhn.diversity.territory.Territory.GUADELOUPE;
import static fr.mnhn.diversity.territory.Territory.OUTRE_MER;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.mnhn.diversity.common.exception.FunctionalException;
import fr.mnhn.diversity.ecogesture.Ecogesture;
import fr.mnhn.diversity.ecogesture.EcogestureRepository;
import fr.mnhn.diversity.indicator.api.IndicatorData;
import fr.mnhn.diversity.indicator.api.IndicatorService;
import fr.mnhn.diversity.indicator.api.ValuedIndicator;
import fr.mnhn.diversity.model.PageRepository;
import fr.mnhn.diversity.territory.Territory;
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
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

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

    @MockBean
    private EcogestureRepository mockEcogestureRepository;

    @MockBean
    private PageRepository mockPageRepository;

    @MockBean
    private IndicatorService mockIndicatorService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IndicatorRestController controller;

    @Captor
    private ArgumentCaptor<Indicator> indicatorArgumentCaptor;

    private IndicatorCategory category;
    private Ecogesture ecogesture;
    private Indicator indicator;

    @BeforeEach
    void prepare() {
        category = new IndicatorCategory(54L, "Végétation");
        when(mockIndicatorCategoryRepository.findById(category.getId())).thenReturn(Optional.of(category));

        ecogesture = new Ecogesture(43L, "trier-ses-dechets");
        when(mockEcogestureRepository.findById(ecogesture.getId())).thenReturn(Optional.of(ecogesture));

        indicator = new Indicator(43L, "biom_43", "deforestation", List.of(category), List.of(ecogesture));
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
               .andExpect(jsonPath("$[0].categories[0].name").value(indicator.getCategories().get(0).getName()))
               .andExpect(jsonPath("$[0].ecogestures.length()").value(indicator.getEcogestures().size()))
               .andExpect(jsonPath("$[0].ecogestures[0].id").value(indicator.getEcogestures().get(0).getId()))
               .andExpect(jsonPath("$[0].ecogestures[0].slug").value(indicator.getEcogestures().get(0).getSlug()));
    }

    @Test
    void shouldDelete() throws Exception {
        when(mockIndicatorRepository.findById(42L)).thenReturn(Optional.of(indicator));

        mockMvc.perform(delete("/api/indicators/42"))
               .andExpect(status().isNoContent());

        verify(mockIndicatorRepository).delete(indicator);
        verify(mockPageRepository).deleteByNameAndModel(indicator.getSlug(), IndicatorModel.INDICATOR_PAGE_MODEL.getName());
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
    void shouldGetValues() throws Exception {
        when(mockIndicatorService.indicator("biom-42")).thenReturn(
            Mono.just(
                new ValuedIndicator(
                    new IndicatorData("biom-42", "i1", "r1"),
                    Map.of(OUTRE_MER, new IndicatorValue(10, "%"),
                           GUADELOUPE, new IndicatorValue(20, "%"))
                )
            )
        );

        mockMvc.perform(get("/api/indicators/biom-42/values"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.shortLabel").value("i1"))
               .andExpect(jsonPath("$.values.length()").value(2))
               .andExpect(jsonPath("$.values[0].territory").value(OUTRE_MER.getName()))
               .andExpect(jsonPath("$.values[0].value").value(10))
               .andExpect(jsonPath("$.values[0].unit").value("%"))
               .andExpect(jsonPath("$.values[1].territory").value(GUADELOUPE.getName()))
               .andExpect(jsonPath("$.values[1].value").value(20))
               .andExpect(jsonPath("$.values[1].unit").value("%"));
    }

    @Test
    void shouldThrowIfValuesDoNotExistForIndicator() throws Exception {
        when(mockIndicatorService.indicator("biom-42")).thenThrow(WebClientResponseException.create(404, "Not found", null, null, null));

        assertThatExceptionOfType(FunctionalException.class).isThrownBy(
            () -> controller.getValues("biom-42")
        ).matches(e -> e.getCode() == FunctionalException.Code.INDICATOR_VALUES_NOT_FOUND);
    }

    @Test
    void shouldThrowIfUnexpectedExceptionForIndicator() throws Exception {
        when(mockIndicatorService.indicator("biom-42")).thenThrow(new IllegalStateException());

        assertThatExceptionOfType(FunctionalException.class).isThrownBy(
            () -> controller.getValues("biom-42")
        ).matches(e -> e.getCode() == FunctionalException.Code.UNEXPECTED_ERROR_WHILE_FETCHING_INDICATOR_VALUES);
    }

    @Test
    void shouldThrowIfWebCLientErrorForIndicator() throws Exception {
        when(mockIndicatorService.indicator("biom-42")).thenThrow(WebClientResponseException.create(500, "Server error", null, null, null));

        assertThatExceptionOfType(FunctionalException.class).isThrownBy(
            () -> controller.getValues("biom-42")
        ).matches(e -> e.getCode() == FunctionalException.Code.HTTP_ERROR_WHILE_FETCHING_INDICATOR_VALUES);
    }

    @Test
    void shouldCreateAndFetchValues() throws Exception {
        IndicatorCommandDTO command = new IndicatorCommandDTO(
            "biom_67",
            "surface-forêts",
            List.of(category.getId()),
            List.of(ecogesture.getId())
        );

        when(mockIndicatorService.indicator(command.getBiomId())).thenReturn(
            Mono.just(
                new ValuedIndicator(
                    new IndicatorData(command.getBiomId(), "i1", "r1"),
                    Map.of(OUTRE_MER, new IndicatorValue(10, "%"),
                           GUADELOUPE, new IndicatorValue(20, "%"))
                )
            )
        );

        Indicator createdIndicator = new Indicator(256L, command.getBiomId(), command.getSlug(), List.of(category), List.of(ecogesture));
        when(mockIndicatorRepository.create(any())).thenReturn(createdIndicator);

        mockMvc.perform(post("/api/indicators")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(command)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id").value(256L));

        verify(mockIndicatorRepository).create(indicatorArgumentCaptor.capture());

        Indicator indicatorToCreate = indicatorArgumentCaptor.getValue();
        assertThat(indicatorToCreate.getBiomId()).isEqualTo(command.getBiomId());
        assertThat(indicatorToCreate.getSlug()).isEqualTo(command.getSlug());
        assertThat(indicatorToCreate.getCategories()).containsExactly(category);
        assertThat(indicatorToCreate.getEcogestures()).containsExactly(ecogesture);

        verify(mockIndicatorRepository).updateValue(createdIndicator, OUTRE_MER, new IndicatorValue(10, "%"));
        verify(mockIndicatorRepository).updateValue(createdIndicator, GUADELOUPE, new IndicatorValue(20, "%"));
    }

    @Test
    void shouldThrowWhenCreatingWithAlreadyExistingBiomId() {
        IndicatorCommandDTO command = new IndicatorCommandDTO(
            indicator.getBiomId(),
            "surface-forêts",
            List.of(category.getId()),
            List.of(ecogesture.getId())
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
            List.of(category.getId()),
            List.of(ecogesture.getId())
        );
        assertThatExceptionOfType(FunctionalException.class).isThrownBy(
            () -> controller.create(command)
        ).matches(e -> e.getCode() == FunctionalException.Code.INDICATOR_SLUG_ALREADY_EXISTING);
    }

    @Test
    void shouldUpdateTheIndicatorAndItsValues() throws Exception {
        IndicatorCommandDTO command = new IndicatorCommandDTO(
            "biom_92",
            "surface-forêts",
            List.of(),
            List.of()
        );

        when(mockIndicatorService.indicator(command.getBiomId())).thenReturn(
            Mono.just(
                new ValuedIndicator(
                    new IndicatorData(command.getBiomId(), "i1", "r1"),
                    Map.of(OUTRE_MER, new IndicatorValue(10, "%"),
                           GUADELOUPE, new IndicatorValue(20, "%"))
                )
            )
        );

        mockMvc.perform(put("/api/indicators/{id}", indicator.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(command)))
               .andExpect(status().isNoContent());

        verify(mockIndicatorRepository).update(indicatorArgumentCaptor.capture());

        Indicator updatedIndicator = indicatorArgumentCaptor.getValue();
        assertThat(updatedIndicator.getId()).isEqualTo(indicator.getId());
        assertThat(updatedIndicator.getBiomId()).isEqualTo(command.getBiomId());
        assertThat(updatedIndicator.getSlug()).isEqualTo(command.getSlug());
        assertThat(updatedIndicator.getCategories()).isEmpty();

        // delete old values
        verify(mockIndicatorRepository).deleteValues(updatedIndicator, Set.of(Territory.values()));

        // create new ones
        verify(mockIndicatorRepository).updateValue(updatedIndicator, OUTRE_MER, new IndicatorValue(10, "%"));
        verify(mockIndicatorRepository).updateValue(updatedIndicator, GUADELOUPE, new IndicatorValue(20, "%"));

        // update page name
        verify(mockPageRepository).updateName("deforestation", IndicatorModel.INDICATOR_PAGE_MODEL.getName(), "surface-forêts");
    }

    @Test
    void shouldThrowWhenUpdatingWithAlreadyExistingBiomId() {
        Indicator otherIndicator = new Indicator(indicator.getBiomId(), "other", List.of(), List.of());
        when(mockIndicatorRepository.findByBiomId(otherIndicator.getBiomId())).thenReturn(Optional.of(otherIndicator));
        IndicatorCommandDTO command = new IndicatorCommandDTO(
            indicator.getBiomId(),
            "surface-forets",
            List.of(),
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
            List.of(),
            List.of()
        );
        assertThatCode(() -> controller.update(indicator.getId(), command)).doesNotThrowAnyException();

        // it should not update the values as the BIOM ID is the same
        verify(mockIndicatorRepository, never()).deleteValues(any(), anySet());
        verify(mockIndicatorService, never()).indicator(anyString());
    }

    @Test
    void shouldThrowWhenUpdatingWithAlreadyExistingSlug() {
        Indicator otherIndicator = new Indicator("other", indicator.getSlug(), List.of(), List.of());
        when(mockIndicatorRepository.findBySlug(otherIndicator.getSlug())).thenReturn(Optional.of(otherIndicator));
        IndicatorCommandDTO command = new IndicatorCommandDTO(
            "biom_92",
            indicator.getSlug(),
            List.of(),
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
            List.of(),
            List.of()
        );

        when(mockIndicatorService.indicator(command.getBiomId())).thenReturn(
            Mono.just(
                new ValuedIndicator(
                    new IndicatorData(command.getBiomId(), "i1", "r1"),
                    Map.of(OUTRE_MER, new IndicatorValue(10, "%"),
                          GUADELOUPE, new IndicatorValue(20, "%"))
                )
            )
        );

        assertThatCode(() -> controller.update(indicator.getId(), command)).doesNotThrowAnyException();
    }
}
