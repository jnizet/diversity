package fr.mnhn.diversity.image;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

import fr.mnhn.diversity.common.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller used to download images.
 * The image metadata (ID, content type, original file name) are stored in database, but the image itself is stored in
 * a directory.
 * In this directory, the images are named by their ID (to avoid name clashes) with an extension (.jpg, etc.) deduced
 * from the content type of the image.
 * @author JB Nizet
 */
@RestController
@RequestMapping("/images")
@Transactional
public class ImageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageController.class);

    private final ImageRepository imageRepository;
    private final ImageProperties imageProperties;

    public ImageController(ImageRepository imageRepository, ImageProperties imageProperties) {
        this.imageRepository = imageRepository;
        this.imageProperties = imageProperties;
    }

    @GetMapping("/{imageId}/image")
    public ResponseEntity<Resource> getImageBytes(@PathVariable("imageId") Long imageId) throws IOException {
        Image image = imageRepository.findById(imageId).orElseThrow(NotFoundException::new);
        ImageType imageType = ImageType.fromContentType(image.getContentType());
        String imageFileName = image.getId() + "." + imageType.getExtension();
        Path imagePath = imageProperties.getDirectory().resolve(imageFileName);

        if (!Files.exists(imagePath)) {
            LOGGER.error("Image with ID " + imageId +
                             " exists in the database but not in the directory " + imageProperties.getDirectory() +
                             " under the name " + imageFileName);
            throw new NotFoundException();
        }

        // we set the content disposition so that if the user wants to save the image, the suggested file name
        // will be the original file name rather than `<imageId>.<extension>`.
        String contentDisposition =
            ContentDisposition.builder("inline")
                              .filename(image.getOriginalFileName())
                              .build()
                              .toString();

        // we tell the browser to cache the image for 30 days, because images are supposed to be immutable:
        // if we change the image, then we should change its ID
        return ResponseEntity.ok()
                             .contentType(imageType.getMediaType())
                             .contentLength(Files.size(imagePath))
                             .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                             .cacheControl(CacheControl.maxAge(Duration.ofDays(30)))
                             .body(new FileSystemResource(imagePath));
    }
}