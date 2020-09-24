package fr.mnhn.diversity.indicator;

import static fr.mnhn.diversity.common.exception.FunctionalException.Code.INDICATOR_BIOM_ID_ALREADY_EXISTING;
import static fr.mnhn.diversity.common.exception.FunctionalException.Code.INDICATOR_SLUG_ALREADY_EXISTING;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fr.mnhn.diversity.common.exception.BadRequestException;
import fr.mnhn.diversity.common.exception.FunctionalException;
import fr.mnhn.diversity.common.exception.NotFoundException;
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

    public IndicatorRestController(
        IndicatorRepository indicatorRepository,
        IndicatorCategoryRepository indicatorCategoryRepository
    ) {
        this.indicatorRepository = indicatorRepository;
        this.indicatorCategoryRepository = indicatorCategoryRepository;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IndicatorDTO create(@Validated @RequestBody IndicatorCommandDTO command) {
        validateIndicatorBiomId(command.getBiomId(), null);
        validateIndicatorSlug(command.getSlug(), null);

        Indicator indicator = copyCommandToIndicator(command);

        Indicator createdIndicator = indicatorRepository.create(indicator);
        return new IndicatorDTO(createdIndicator);
    }

    private Indicator copyCommandToIndicator(IndicatorCommandDTO command) {
        List<IndicatorCategory> categories = new ArrayList<>();
        command.getCategoryIds().forEach(categoryId -> {
            IndicatorCategory indicatorCategory = indicatorCategoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new BadRequestException("Pas de catégorie avec l'identifiant " + categoryId));
            categories.add(indicatorCategory);
        });

        return new Indicator(command.getBiomId(), command.getSlug(), categories);
    }

    @PutMapping("/{indicatorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable("indicatorId") Long indicatorId,
                       @Validated @RequestBody IndicatorCommandDTO command) {
        Indicator indicator = indicatorRepository.findById(indicatorId)
                                                 .orElseThrow(NotFoundException::new);
        validateIndicatorBiomId(command.getBiomId(), indicator);
        validateIndicatorSlug(command.getSlug(), indicator);

        Indicator updatedIndicator = copyCommandToIndicator(command);

        indicatorRepository.update(updatedIndicator);
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
