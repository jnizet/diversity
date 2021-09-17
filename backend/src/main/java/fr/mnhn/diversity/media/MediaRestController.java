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
 * Controller used to update media page metadata
 */
@RestController
@Transactional
@RequestMapping("/api/media")
public class MediaRestController {

    private final MediaRepository mediaRepository;

    public MediaRestController(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    @PutMapping("/{mediaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateMediaCategories(@PathVariable("mediaId") Long mediaId,
                       @Validated @RequestBody MediaCommandDTO command) {
        mediaRepository.updateCategories(mediaId, command.getCategoriesId());
    }
}
