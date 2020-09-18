package fr.mnhn.diversity.indicator;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.mnhn.diversity.indicator.api.IndicatorService;
import fr.mnhn.diversity.territory.Territory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Scheduled job used to refresh the indicator values every night
 * @author JB Nizet
 */
@Component
public class RefreshIndicatorValuesJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(RefreshIndicatorValuesJob.class);

    private final IndicatorRepository indicatorRepository;
    private final IndicatorService indicatorService;

    public RefreshIndicatorValuesJob(IndicatorRepository indicatorRepository,
                                     IndicatorService indicatorService) {
        this.indicatorRepository = indicatorRepository;
        this.indicatorService = indicatorService;
    }

    @Scheduled(cron = "0 0 2 * * *", zone = "Europe/Paris")
    @Transactional
    public void refreshIndicatorValues() {
        List<Indicator> indicators = indicatorRepository.list();
        for (Indicator indicator : indicators) {
            refreshIndicator(indicator);
        }
    }

    private void refreshIndicator(Indicator indicator) {
        LOGGER.info("Refreshing values for indicator " + indicator.getBiomId());
        try {
            Map<Territory, IndicatorValue> indicatorValues =
                indicatorService.indicatorData(indicator.getBiomId())
                                .flatMap(indicatorData -> indicatorService.indicatorValues(indicatorData.getCalculationReference()))
                                .block();

            Set<Territory> territoriesWithoutValue = EnumSet.complementOf(EnumSet.copyOf(indicatorValues.keySet()));
            indicatorRepository.deleteValues(indicator, territoriesWithoutValue);

            indicatorValues.forEach((territory, indicatorValue) -> {
                boolean updated = indicatorRepository.updateValue(indicator, territory, indicatorValue);
                if (!updated) {
                    indicatorRepository.insertValue(indicator, territory, indicatorValue);
                }
            });
        } catch (Exception e) {
            LOGGER.error("Failure to refresh values for indicator " + indicator.getBiomId());
        }
    }
}
