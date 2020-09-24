package fr.mnhn.diversity.indicator;

import static fr.mnhn.diversity.common.exception.FunctionalException.Code.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import fr.mnhn.diversity.common.exception.BadRequestException;
import fr.mnhn.diversity.common.exception.FunctionalException;
import fr.mnhn.diversity.common.exception.NotFoundException;
import fr.mnhn.diversity.indicator.api.IndicatorService;
import fr.mnhn.diversity.territory.Territory;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller used to handle the indicators API
 */
@RestController
@Transactional
@RequestMapping("/api/indicators")
public class IndicatorRestController {

    private final IndicatorRepository indicatorRepository;
    private final IndicatorCategoryRepository indicatorCategoryRepository;
    private final IndicatorService indicatorService;

    public IndicatorRestController(
        IndicatorRepository indicatorRepository,
        IndicatorCategoryRepository indicatorCategoryRepository,
        IndicatorService indicatorService
    ) {
        this.indicatorRepository = indicatorRepository;
        this.indicatorCategoryRepository = indicatorCategoryRepository;
        this.indicatorService = indicatorService;
    }

    @GetMapping
    public List<IndicatorDTO> list() {
        return this.indicatorRepository.list()
                                       .stream()
                                       .map(IndicatorDTO::new)
                                       .collect(Collectors.toList());
    }

    @DeleteMapping("/{indicatorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("indicatorId") Long indicatorId) {
        Indicator indicator = indicatorRepository.findById(indicatorId).orElseThrow(NotFoundException::new);
        indicatorRepository.delete(indicator);
    }

    @GetMapping("/{indicatorId}")
    public IndicatorDTO get(@PathVariable("indicatorId") Long indicatorId) {
        Indicator indicator = indicatorRepository.findById(indicatorId).orElseThrow(NotFoundException::new);
        return new IndicatorDTO(indicator);
    }

    @GetMapping("/{biomId}/values")
    public List<IndicatorValueDTO> getValues(@PathVariable("biomId") String biomId) {
        Map<Territory, IndicatorValue> indicatorValues = getValuesForIndicatorBiom(biomId);
        return indicatorValues.entrySet()
                              .stream()
                              .map(e -> new IndicatorValueDTO(e.getKey(), e.getValue()))
                              .sorted(Comparator.comparing(IndicatorValueDTO::getTerritory))
                              .collect(Collectors.toList());
    }

    private Map<Territory, IndicatorValue> getValuesForIndicatorBiom(String biomId) {
        Map<Territory, IndicatorValue> indicatorValues;
        try {
            indicatorValues = indicatorService.indicatorData(biomId)
                                              .flatMap(indicatorData -> indicatorService.indicatorValues(indicatorData.getCalculationReference()))
                                              .block();
        } catch (Exception e) {
            throw new FunctionalException(INDICATOR_VALUES_NOT_FOUND);
        }
        return indicatorValues;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IndicatorDTO create(@Validated @RequestBody IndicatorCommandDTO command) {
        validateIndicatorBiomId(command.getBiomId(), null);
        validateIndicatorSlug(command.getSlug(), null);

        Indicator indicator = copyCommandToIndicator(null, command);

        Indicator createdIndicator = indicatorRepository.create(indicator);

        // fetch the values for the indicator
        fetchAndStoreIndicatorValues(indicator);
        return new IndicatorDTO(createdIndicator);
    }

    private Indicator copyCommandToIndicator(Long id, IndicatorCommandDTO command) {
        List<IndicatorCategory> categories = new ArrayList<>();
        command.getCategoryIds().forEach(categoryId -> {
            IndicatorCategory indicatorCategory = indicatorCategoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new BadRequestException("Pas de catégorie avec l'identifiant " + categoryId));
            categories.add(indicatorCategory);
        });

        return new Indicator(id, command.getBiomId(), command.getSlug(), categories);
    }

    @PutMapping("/{indicatorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable("indicatorId") Long indicatorId,
                       @Validated @RequestBody IndicatorCommandDTO command) {
        Indicator indicator = indicatorRepository.findById(indicatorId)
                                                 .orElseThrow(NotFoundException::new);
        validateIndicatorBiomId(command.getBiomId(), indicator);
        validateIndicatorSlug(command.getSlug(), indicator);

        Indicator updatedIndicator = copyCommandToIndicator(indicatorId, command);
        indicatorRepository.update(updatedIndicator);

        // if new biom ID
        if (!indicator.getBiomId().equals(updatedIndicator.getBiomId())) {
            indicatorRepository.deleteValues(updatedIndicator, Set.of(Territory.values()));
            fetchAndStoreIndicatorValues(updatedIndicator);
        }
    }

    private void fetchAndStoreIndicatorValues(Indicator indicator) {
        // fetch the new values for the indicator
        Map<Territory, IndicatorValue> indicatorValues = getValuesForIndicatorBiom(indicator.getBiomId());
        indicatorValues.forEach((territory, indicatorValue) -> {
            boolean updated = indicatorRepository.updateValue(indicator, territory, indicatorValue);
            if (!updated) {
                indicatorRepository.insertValue(indicator, territory, indicatorValue);
            }
        });
    }

    private void validateIndicatorBiomId(String biomId, Indicator indicator) {
        indicatorRepository.findByBiomId(biomId)
                           .filter(foundIndicator -> !foundIndicator.equals(indicator))
                           .ifPresent(foundIndicator -> {
                               throw new FunctionalException(INDICATOR_BIOM_ID_ALREADY_EXISTING);
                           });
    }

    private void validateIndicatorSlug(String slug, Indicator indicator) {
        indicatorRepository.findBySlug(slug)
                           .filter(foundIndicator -> !foundIndicator.equals(indicator))
                           .ifPresent(foundIndicator -> {
                               throw new FunctionalException(INDICATOR_SLUG_ALREADY_EXISTING);
                           });
    }
}
