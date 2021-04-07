package fr.mnhn.diversity.image;

import java.io.IOException;
import java.time.Duration;
import java.util.Locale;

import fr.mnhn.diversity.common.exception.NotFoundException;
import fr.mnhn.diversity.model.ImageSize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.format.Formatter;
import org.springframework.http.CacheControl;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
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

@CrossOrigin
@RestController
@RequestMapping("/images")
@Transactional
public class ImageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageController.class);

    private final ImageRepository imageRepository;
    private final ImageStorageService imageStorageService;

    public ImageController(ImageRepository imageRepository, ImageStorageService imageStorageService) {
        this.imageRepository = imageRepository;
        this.imageStorageService = imageStorageService;
    }

    @GetMapping("/{imageId}/image")
    public ResponseEntity<Resource> getImageBytes(@PathVariable("imageId") Long imageId) throws IOException {
        Image image = imageRepository.findById(imageId).orElseThrow(NotFoundException::new);
        if (!imageStorageService.imageExists(image)) {
            LOGGER.error("Image with ID " + imageId +
                             " exists in the database but not in the file " + imageStorageService.imagePath(image));
            throw new NotFoundException();
        }
        return createInlineResponse(image, imageStorageService.imageResource(image));
    }

    @GetMapping("/{imageId}/image/{imageSize}")
    public ResponseEntity<Resource> getImageBytes(@PathVariable("imageId") Long imageId,
                                                  @PathVariable("imageSize") ImageSize requestedImageSize) throws IOException {
        Image image = imageRepository.findById(imageId).orElseThrow(NotFoundException::new);

        Resource foundImageResource = null;
        // we try every image size from the requested one until the largest one, so that if a variant is missing for
        // some reason, we fallback to the largest one. In development, that allows using only the large images.
        for (ImageSize attemptedSize : ImageSize.values()) {
            if (attemptedSize.compareTo(requestedImageSize) >= 0) {
                if (!imageStorageService.multiSizeImageExists(image, attemptedSize)) {
                    LOGGER.error("Image with ID " + imageId +
                                     " exists in the database but not in the file " + imageStorageService.multiSizeImagePath(image, attemptedSize));
                } else {
                    foundImageResource = imageStorageService.multiSizeImageResource(image, attemptedSize);
                    break;
                }
            }
        }

        if (foundImageResource == null) {
            throw new NotFoundException();
        }

        return createInlineResponse(image, foundImageResource);
    }

    @GetMapping("/{imageId}/document")
    public ResponseEntity<Resource> getDocumentBytes(@PathVariable("imageId") Long imageId) throws IOException {
        Image image = imageRepository.findById(imageId).orElseThrow(NotFoundException::new);
        if (!imageStorageService.imageExists(image)) {
            LOGGER.error("Document with ID " + imageId +
                             " exists in the database but not in the file " + imageStorageService.imagePath(image));
            throw new NotFoundException();
        }
        return createAttachmentResponse(image, imageStorageService.imageResource(image));
    }

    private ResponseEntity<Resource> createInlineResponse(Image image, Resource imageResource) throws IOException {
        // we set the content disposition so that if the user wants to save the image, the suggested file name
        // will be the original file name rather than `<imageId>.<extension>`.
        String contentDisposition =
            ContentDisposition.builder("inline")
                              .filename(image.getOriginalFileName())
                              .build()
                              .toString();
        return createResponse(image, imageResource, contentDisposition);
    }

    private ResponseEntity<Resource> createAttachmentResponse(Image image, Resource imageResource) throws IOException {
        String contentDisposition =
            ContentDisposition.builder("attachment")
                              .filename(image.getOriginalFileName())
                              .build()
                              .toString();
        return createResponse(image, imageResource, contentDisposition);
    }

    private ResponseEntity<Resource> createResponse(Image image, Resource imageResource, String contentDisposition) throws IOException {
        // we tell the browser to cache the image for 365 days, because images are supposed to be immutable:
        // if we change the image, then we should change its ID
        return ResponseEntity.ok()
                             .contentType(MediaType.parseMediaType(image.getContentType()))
                             .contentLength(imageResource.contentLength())
                             .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                             .cacheControl(CacheControl.maxAge(Duration.ofDays(365)))
                             .body(imageResource);
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addCustomFormatter(new Formatter<ImageSize>() {
            @Override
            public ImageSize parse(String text, Locale locale) {
                return ImageSize.valueOf(text.toUpperCase());
            }

            @Override
            public String print(ImageSize object, Locale locale) {
                return object.name().toLowerCase();
            }
        });
    }
}
