package fr.mnhn.diversity.indicator;

import static fr.mnhn.diversity.common.exception.FunctionalException.Code.INDICATOR_CATEGORY_NAME_ALREADY_EXISTING;

import java.util.List;
import java.util.stream.Collectors;

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
 * Controller used to handle the categories of the indicators
 */
@RestController
@Transactional
@RequestMapping("/api/indicator-categories")
public class IndicatorCategoryRestController {

    private final IndicatorCategoryRepository indicatorCategoryRepository;

    public IndicatorCategoryRestController(IndicatorCategoryRepository indicatorCategoryRepository) {
        this.indicatorCategoryRepository = indicatorCategoryRepository;
    }

    @GetMapping
    public List<IndicatorCategoryDTO> list() {
        return this.indicatorCategoryRepository.list()
                                               .stream()
                                               .map(IndicatorCategoryDTO::new)
                                               .collect(Collectors.toList());
    }

    @DeleteMapping("/{indicatorCategoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("indicatorCategoryId") Long indicatorCategoryId) {
        IndicatorCategory indicatorCategory = indicatorCategoryRepository.findById(indicatorCategoryId).orElseThrow(NotFoundException::new);
        indicatorCategoryRepository.delete(indicatorCategory);
    }

    @GetMapping("/{indicatorCategoryId}")
    public IndicatorCategoryDTO get(@PathVariable("indicatorCategoryId") Long indicatorCategoryId) {
        IndicatorCategory indicatorCategory = indicatorCategoryRepository.findById(indicatorCategoryId).orElseThrow(NotFoundException::new);
        return new IndicatorCategoryDTO(indicatorCategory);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IndicatorCategoryDTO create(@Validated @RequestBody IndicatorCategoryCommandDTO command) {
        validateIndicatorCategoryName(command.getName(), null);

        IndicatorCategory indicatorCategory = new IndicatorCategory(command.getName());

        IndicatorCategory createdIndicatorCategory = indicatorCategoryRepository.create(indicatorCategory);
        return new IndicatorCategoryDTO(createdIndicatorCategory);
    }

    @PutMapping("/{indicatorCategoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable("indicatorCategoryId") Long indicatorCategoryId,
                       @Validated @RequestBody IndicatorCategoryCommandDTO command) {
        IndicatorCategory indicatorCategory = indicatorCategoryRepository.findById(indicatorCategoryId)
                                                                         .orElseThrow(NotFoundException::new);
        validateIndicatorCategoryName(command.getName(), indicatorCategory);

        IndicatorCategory updatedIndicatorCategory = new IndicatorCategory(indicatorCategory.getId(), command.getName());

        indicatorCategoryRepository.update(updatedIndicatorCategory);
    }

    private void validateIndicatorCategoryName(String name, IndicatorCategory indicatorCategory) {
        indicatorCategoryRepository.findByName(name)
                                   .filter(foundIndicatorCategory -> !foundIndicatorCategory.equals(indicatorCategory))
                                   .ifPresent(foundIndicatorCategory -> {
                                       throw new FunctionalException(INDICATOR_CATEGORY_NAME_ALREADY_EXISTING);
                                   });
    }
}
