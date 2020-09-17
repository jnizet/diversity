package fr.mnhn.diversity.indicator;

import static fr.mnhn.diversity.territory.Territory.*;
import static org.mockito.Mockito.*;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

/**
 * Unit tests for {@link RefreshIndicatorValuesJob}
 * @author JB Nizet
 */
class RefreshIndicatorValuesJobTest {
    private IndicatorRepository mockIndicatorRepository;
    private IndicatorService mockIndicatorService;
    private RefreshIndicatorValuesJob job;

    private Indicator indicator1;
    private Indicator indicator2;

    @BeforeEach
    void prepare() {
        mockIndicatorRepository = mock(IndicatorRepository.class);
        mockIndicatorService = mock(IndicatorService.class);
        job = new RefreshIndicatorValuesJob(mockIndicatorRepository, mockIndicatorService);

        indicator1 = new Indicator(1L, "indicator1");
        indicator2 = new Indicator(2L, "indicator2");

        when(mockIndicatorRepository.list()).thenReturn(List.of(indicator1, indicator2));

        when(mockIndicatorService.indicatorData(indicator1.getBiomId())).thenReturn(Mono.just(new IndicatorData(indicator1.getBiomId(), "i1", "r1")));
        when(mockIndicatorService.indicatorData(indicator2.getBiomId())).thenReturn(Mono.just(new IndicatorData(indicator2.getBiomId(), "i2", "r2")));

        when(mockIndicatorService.indicatorValues("r1")).thenReturn(
            Mono.just(Map.of(OUTRE_MER, new IndicatorValue(10, "%"),
                             GUADELOUPE, new IndicatorValue(20, "%")))
        );
        when(mockIndicatorService.indicatorValues("r2")).thenReturn(
            Mono.just(Map.of(OUTRE_MER, new IndicatorValue(30, "%"),
                             REUNION, new IndicatorValue(40, "%")))
        );
    }

    @Test
    void shouldRefreshIndicatorValues() {
        when(mockIndicatorRepository.updateValue(any(), eq(OUTRE_MER), any())).thenReturn(true);

        job.refreshIndicatorValues();

        verify(mockIndicatorRepository).deleteValues(indicator1, EnumSet.complementOf(EnumSet.of(OUTRE_MER, GUADELOUPE)));
        verify(mockIndicatorRepository).deleteValues(indicator2, EnumSet.complementOf(EnumSet.of(OUTRE_MER, REUNION)));

        verify(mockIndicatorRepository).updateValue(indicator1, OUTRE_MER, new IndicatorValue(10, "%"));
        verify(mockIndicatorRepository).updateValue(indicator1, GUADELOUPE, new IndicatorValue(20, "%"));
        verify(mockIndicatorRepository).updateValue(indicator2, OUTRE_MER, new IndicatorValue(30, "%"));
        verify(mockIndicatorRepository).updateValue(indicator2, REUNION, new IndicatorValue(40, "%"));

        verify(mockIndicatorRepository).insertValue(indicator1, GUADELOUPE, new IndicatorValue(20, "%"));
        verify(mockIndicatorRepository).insertValue(indicator2, REUNION, new IndicatorValue(40, "%"));
    }
}
