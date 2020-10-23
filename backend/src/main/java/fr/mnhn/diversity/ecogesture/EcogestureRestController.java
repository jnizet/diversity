package fr.mnhn.diversity.ecogesture;

import static fr.mnhn.diversity.common.exception.FunctionalException.Code.ECOGESTURE_SLUG_ALREADY_EXISTING;

import java.util.List;
import java.util.stream.Collectors;

import fr.mnhn.diversity.common.exception.FunctionalException;
import fr.mnhn.diversity.common.exception.NotFoundException;
import fr.mnhn.diversity.model.PageRepository;
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
 * Controller used to handle the ecogestures
 */
@RestController
@Transactional
@RequestMapping("/api/ecogestures")
public class EcogestureRestController {

    private final EcogestureRepository ecogestureRepository;
    private final PageRepository pageRepository;

    public EcogestureRestController(EcogestureRepository ecogestureRepository,
                                    PageRepository pageRepository) {
        this.ecogestureRepository = ecogestureRepository;
        this.pageRepository = pageRepository;
    }

    @GetMapping
    public List<EcogestureDTO> list() {
        return this.ecogestureRepository.list()
                                        .stream()
                                        .map(EcogestureDTO::new)
                                        .collect(Collectors.toList());
    }

    @DeleteMapping("/{ecogestureId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("ecogestureId") Long ecogestureId) {
        // TODO delete the related page if there is one, or prevent the deletion?
        Ecogesture ecogesture = ecogestureRepository.findById(ecogestureId).orElseThrow(NotFoundException::new);
        pageRepository.deleteByNameAndModel(ecogesture.getSlug(), EcogestureModel.ECO_GESTURE_PAGE_MODEL.getName());
        ecogestureRepository.delete(ecogesture);
    }

    @GetMapping("/{ecogestureId}")
    public EcogestureDTO get(@PathVariable("ecogestureId") Long ecogestureId) {
        Ecogesture ecogesture = ecogestureRepository.findById(ecogestureId).orElseThrow(NotFoundException::new);
        return new EcogestureDTO(ecogesture);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EcogestureDTO create(@Validated @RequestBody EcogestureCommandDTO command) {
        validateEcogestureSlug(command.getSlug(), null);

        Ecogesture ecogesture = new Ecogesture(command.getSlug());

        Ecogesture createdEcogesture = ecogestureRepository.create(ecogesture);
        return new EcogestureDTO(createdEcogesture);
    }

    @PutMapping("/{ecogestureId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable("ecogestureId") Long ecogestureId,
                       @Validated @RequestBody EcogestureCommandDTO command) {
        Ecogesture ecogesture = ecogestureRepository.findById(ecogestureId)
                                                    .orElseThrow(NotFoundException::new);
        validateEcogestureSlug(command.getSlug(), ecogesture);

        Ecogesture updatedEcogesture = ecogesture.withSlug(command.getSlug());

        ecogestureRepository.update(updatedEcogesture);

        if (!ecogesture.getSlug().equals(updatedEcogesture.getSlug())) {
            pageRepository.updateName(ecogesture.getSlug(), EcogestureModel.ECO_GESTURE_PAGE_MODEL.getName(), updatedEcogesture.getSlug());
        }
    }

    private void validateEcogestureSlug(String slug, Ecogesture ecogesture) {
        ecogestureRepository.findBySlug(slug)
                            .filter(foundEcogesture -> !foundEcogesture.equals(ecogesture))
                            .ifPresent(foundEcogesture -> {
                                throw new FunctionalException(ECOGESTURE_SLUG_ALREADY_EXISTING);
                            });
    }
}
