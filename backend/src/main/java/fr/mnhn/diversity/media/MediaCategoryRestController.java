package fr.mnhn.diversity.media;

import static fr.mnhn.diversity.common.exception.FunctionalException.Code.INDICATOR_CATEGORY_NAME_ALREADY_EXISTING;

import fr.mnhn.diversity.common.exception.FunctionalException;
import fr.mnhn.diversity.common.exception.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;
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
 * Controller used to handle the categories of the medias
 */
@RestController
@Transactional
@RequestMapping("/api/media-categories")
public class MediaCategoryRestController {

    private final MediaCategoryRepository mediaCategoryRepository;

    public MediaCategoryRestController(MediaCategoryRepository mediaCategoryRepository) {
        this.mediaCategoryRepository = mediaCategoryRepository;
    }

    @GetMapping
    public List<MediaCategoryDTO> list() {
        return this.mediaCategoryRepository.list()
                                               .stream()
                                               .map(
                                                   MediaCategoryDTO::new)
                                               .collect(Collectors.toList());
    }

    @DeleteMapping("/{mediaCategoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("mediaCategoryId") Long mediaCategoryId) {
        MediaCategory mediaCategory = mediaCategoryRepository.findById(mediaCategoryId).orElseThrow(NotFoundException::new);
        mediaCategoryRepository.delete(mediaCategory);
    }

    @GetMapping("/{mediaCategoryId}")
    public MediaCategoryDTO get(@PathVariable("mediaCategoryId") Long mediaCategoryId) {
        MediaCategory mediaCategory = mediaCategoryRepository.findById(mediaCategoryId).orElseThrow(NotFoundException::new);
        return new MediaCategoryDTO(mediaCategory);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MediaCategoryDTO create(@Validated @RequestBody MediaCategoryCommandDTO command) {
        validateMediaCategoryName(command.getName(), null);

        MediaCategory mediaCategory = new MediaCategory(command.getName());

        MediaCategory createdMediaCategory = mediaCategoryRepository.create(mediaCategory);
        return new MediaCategoryDTO(createdMediaCategory);
    }

    @PutMapping("/{mediaCategoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable("mediaCategoryId") Long mediaCategoryId,
                       @Validated @RequestBody MediaCategoryCommandDTO command) {
        MediaCategory mediaCategory = mediaCategoryRepository.findById(mediaCategoryId)
                                                                         .orElseThrow(NotFoundException::new);
        validateMediaCategoryName(command.getName(), mediaCategory);

        MediaCategory updatedMediaCategory = new MediaCategory(mediaCategory.getId(), command.getName());

        mediaCategoryRepository.update(updatedMediaCategory);
    }

    private void validateMediaCategoryName(String name, MediaCategory mediaCategory) {
        mediaCategoryRepository.findByName(name)
                                   .filter(foundMediaCategory -> !foundMediaCategory
                                       .equals(mediaCategory))
                                   .ifPresent(foundMediaCategory -> {
                                       throw new FunctionalException(INDICATOR_CATEGORY_NAME_ALREADY_EXISTING);
                                   });
    }
}
