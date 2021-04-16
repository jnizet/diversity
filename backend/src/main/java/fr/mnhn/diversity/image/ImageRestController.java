package fr.mnhn.diversity.image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import javax.imageio.ImageIO;

import fr.mnhn.diversity.common.exception.BadRequestException;
import fr.mnhn.diversity.model.ImageSize;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller used to handle the image API used by the admin pages to upload new images
 */
@RestController
@Transactional
@RequestMapping("/api/images")
public class ImageRestController {
    private final ImageRepository imageRepository;
    private final ImageStorageService imageStorageService;

    public ImageRestController(ImageRepository imageRepository, ImageStorageService imageStorageService) {
        this.imageRepository = imageRepository;
        this.imageStorageService = imageStorageService;
    }

    /**
     * Uploads an image. If <code>multisize</code> is true, then the image must be a JPEG image, and is resized in
     * all sizes before being saved on the file system. Otherwise, the image type can be JPEG, PNG, GIF, SVG, or it
     * can even be a PDF document, and it's saved as is on the file system.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ImageDTO create(@RequestParam(value = "multisize", defaultValue = "false") boolean multiSize,
                           @RequestParam(value = "document", defaultValue = "false") boolean document,
                           @RequestParam("file") MultipartFile file) {
        ImageType imageType = getAndValidateImageType(multiSize, document, file);

        Image image = new Image(null, imageType.getMediaType().toString(), file.getOriginalFilename());
        image = imageRepository.create(image);

        saveImage(multiSize, file, image);

        return new ImageDTO(image);
    }

    @GetMapping("import/{imageId}/image")
    public ImageDTO getImportImageBytes(@PathVariable("imageId") Long imageId,
                                        @RequestParam(value = "multisize", defaultValue = "false") boolean multiSize,
                                        @RequestParam(value = "document", defaultValue = "false") boolean document
    ) throws IOException {
        var file = imageStorageService.
            getImageBytesFromImportDataSource(imageId).block();
        MediaType contentType = file.getHeaders().getContentType();
        Image image = new Image(null, contentType.toString(), "copy-" + imageId);
        image = imageRepository.create(image);

        if(multiSize){
            List<ResizedImage> resizedImages = resize(file.getBody().getInputStream().readAllBytes());
            for (ResizedImage resizedImage : resizedImages) {
                imageStorageService.saveMultiSizeImage(image, resizedImage.getSize(), new ByteArrayInputStream(resizedImage.getBytes()));
            }
        }
        else {
            imageStorageService.saveImage(image, file.getBody().getInputStream());
        }
        return new ImageDTO(image);

    }

    private ImageType getAndValidateImageType(boolean multiSize, boolean document, MultipartFile file) {
        if (file.getContentType() == null) {
            throw new BadRequestException("Content type of file must be set");
        }
        if (document && multiSize) {
            throw new BadRequestException("An image may not be a document and multiSize at the same time");
        }
        MediaType mediaType = MediaType.parseMediaType(file.getContentType());

        Set<ImageType> acceptedImageTypes;
        if (multiSize) {
            acceptedImageTypes = Set.of(ImageType.JPG);
        } else if (document) {
            acceptedImageTypes = Set.of(ImageType.PDF);
        } else {
            acceptedImageTypes = EnumSet.complementOf(EnumSet.of(ImageType.PDF));
        }

        return acceptedImageTypes
            .stream()
            .filter(imageType -> imageType.getMediaType().isCompatibleWith(mediaType))
            .findAny()
            .orElseThrow(() -> new BadRequestException("The content type is not one of the accepted content types"));
    }

    private void saveImage(boolean multiSize, MultipartFile file, Image image) {
        try {
            if (multiSize) {
                saveMultiSizeImage(file, image);
            } else {
                saveSimpleImage(file, image);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void saveSimpleImage(MultipartFile file, Image image) throws IOException {
        try (InputStream imageStream = file.getInputStream()) {
            imageStorageService.saveImage(image, imageStream);
        }
    }

    private void saveMultiSizeImage(MultipartFile file, Image image) throws IOException {
        List<ResizedImage> resizedImages = resize(file.getBytes());
        for (ResizedImage resizedImage : resizedImages) {
            imageStorageService.saveMultiSizeImage(image, resizedImage.getSize(), new ByteArrayInputStream(resizedImage.getBytes()));
        }
    }

    private List<ResizedImage> resize(byte[] file) throws IOException {
        BufferedImage sourceBufferedImage = ImageIO.read(new ByteArrayInputStream(file));
        List<ResizedImage> result = new ArrayList<>();
        for (ImageSize imageSize : ImageSize.values()) {
            if (imageSize.getWidth() >= sourceBufferedImage.getWidth()) {
                result.add(new ResizedImage(file, imageSize));
            } else {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ImageIO.write(doResize(sourceBufferedImage, imageSize.getWidth()), "jpg", out);
                result.add(new ResizedImage(out.toByteArray(), imageSize));
            }
        }
        return result;
    }

    private BufferedImage doResize(BufferedImage source, int targetWidth) {
        float ratio = (float) targetWidth / source.getWidth();
        int targetHeight = Math.round(source.getHeight() * ratio);
        BufferedImage target = new BufferedImage(targetWidth, targetHeight, source.getType());
        Graphics2D bg = target.createGraphics();
        bg.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        bg.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        bg.scale(ratio, ratio);
        bg.drawImage(source, 0, 0, null);
        bg.dispose();
        return target;
    }

    private static final class ResizedImage {
        private final byte[] bytes;
        private final ImageSize size;

        public ResizedImage(byte[] bytes, ImageSize size) {
            this.bytes = bytes;
            this.size = size;
        }

        public byte[] getBytes() {
            return bytes;
        }

        public ImageSize getSize() {
            return size;
        }
    }
}
