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
 * Tests for {@link IndicatorCategoryController}
 */
@WebMvcTest(IndicatorCategoryController.class)
@ActiveProfiles("test")
class IndicatorCategoryControllerTest {
    @MockBean
    private IndicatorCategoryRepository mockIndicatorCategoryRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IndicatorCategoryController controller;

    @Captor
    private ArgumentCaptor<IndicatorCategory> categoryArgumentCaptor;

    private IndicatorCategory category;

    @BeforeEach
    void prepare() {
        category = new IndicatorCategory(54L, "Végétation");
        when(mockIndicatorCategoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(mockIndicatorCategoryRepository.findByName(category.getName())).thenReturn(Optional.of(category));
    }

    @Test
    void shouldList() throws Exception {
        when(mockIndicatorCategoryRepository.list()).thenReturn(List.of(category));

        mockMvc.perform(get("/api/indicator-categories"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].id").value(category.getId()))
               .andExpect(jsonPath("$[0].name").value(category.getName()));
    }

    @Test
    void shouldDelete() throws Exception {
        when(mockIndicatorCategoryRepository.findById(42L)).thenReturn(Optional.of(category));

        mockMvc.perform(delete("/api/indicator-categories/42"))
               .andExpect(status().isNoContent());

        verify(mockIndicatorCategoryRepository).delete(category);
    }

    @Test
    void shouldThrowIfNotExistWhenDelete() throws Exception {
        when(mockIndicatorCategoryRepository.findById(42L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/indicator-categories/42"))
               .andExpect(status().isNotFound());
    }

    @Test
    void shouldGet() throws Exception {
        when(mockIndicatorCategoryRepository.findById(42L)).thenReturn(Optional.of(category));

        mockMvc.perform(get("/api/indicator-categories/42"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(category.getId()))
               .andExpect(jsonPath("$.name").value(category.getName()));
    }

    @Test
    void shouldCreate() throws Exception {
        IndicatorCategoryCommandDTO command = new IndicatorCategoryCommandDTO(
            "Espèces menacées"
        );

        when(mockIndicatorCategoryRepository.create(any())).thenReturn(new IndicatorCategory(256L, command.getName()));

        mockMvc.perform(post("/api/indicator-categories")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(command)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id").value(256L));

        verify(mockIndicatorCategoryRepository).create(categoryArgumentCaptor.capture());

        IndicatorCategory createdIndicatorCategory = categoryArgumentCaptor.getValue();
        assertThat(createdIndicatorCategory.getName()).isEqualTo(command.getName());
    }

    @Test
    void shouldThrowWhenCreatingWithAlreadyExistingName() {
        IndicatorCategoryCommandDTO command = new IndicatorCategoryCommandDTO(
            category.getName()
        );
        assertThatExceptionOfType(FunctionalException.class).isThrownBy(
            () -> controller.create(command)
        ).matches(e -> e.getCode() == FunctionalException.Code.INDICATOR_CATEGORY_NAME_ALREADY_EXISTING);
    }

    @Test
    void shouldUpdate() throws Exception {
        IndicatorCategoryCommandDTO command = new IndicatorCategoryCommandDTO(
            "Espèces menacées"
        );

        mockMvc.perform(put("/api/indicator-categories/{id}", category.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(command)))
               .andExpect(status().isNoContent());

        verify(mockIndicatorCategoryRepository).update(categoryArgumentCaptor.capture());

        IndicatorCategory updatedIndicatorCategory = categoryArgumentCaptor.getValue();
        assertThat(updatedIndicatorCategory.getName()).isEqualTo(command.getName());
    }

    @Test
    void shouldThrowWhenUpdatingWithAlreadyExistingName() {
        IndicatorCategory otherIndicatorCategory = new IndicatorCategory(category.getName());
        when(mockIndicatorCategoryRepository.findByName(otherIndicatorCategory.getName())).thenReturn(Optional.of(otherIndicatorCategory));
        IndicatorCategoryCommandDTO command = new IndicatorCategoryCommandDTO(
            category.getName()
        );
        assertThatExceptionOfType(FunctionalException.class).isThrownBy(
            () -> controller.update(category.getId(), command)
        ).matches(e -> e.getCode() == FunctionalException.Code.INDICATOR_CATEGORY_NAME_ALREADY_EXISTING);
    }

    @Test
    void shouldNotThrowWhenUpdatingWithTheSameName() {
        IndicatorCategoryCommandDTO command = new IndicatorCategoryCommandDTO(
            category.getName()
        );
        assertThatCode(() -> controller.update(category.getId(), command)).doesNotThrowAnyException();
    }
}
