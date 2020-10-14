package fr.mnhn.diversity.image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;

import fr.mnhn.diversity.common.exception.BadRequestException;
import fr.mnhn.diversity.model.ImageSize;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
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
                           @RequestParam("file") MultipartFile file) {
        ImageType imageType = getAndValidateImageType(multiSize, file);

        Image image = new Image(null, imageType.getMediaType().toString(), file.getOriginalFilename());
        image = imageRepository.create(image);

        saveImage(multiSize, file, image);

        return new ImageDTO(image);
    }

    private ImageType getAndValidateImageType(boolean multiSize, MultipartFile file) {
        if (file.getContentType() == null) {
            throw new BadRequestException("Content type of file must be set");
        }
        MediaType mediaType = MediaType.parseMediaType(file.getContentType());
        if (multiSize) {
            if (!mediaType.isCompatibleWith(ImageType.JPG.getMediaType())) {
                throw new BadRequestException("The content type must be " + ImageType.JPG.getMediaType() + " for multi-size images");
            }
            return ImageType.JPG;
        } else {
            return Arrays.stream(ImageType.values())
                         .filter(imageType -> imageType.getMediaType().isCompatibleWith(mediaType))
                         .findAny()
                         .orElseThrow(() -> new BadRequestException("The content type is not one of the accepted content types"));
        }
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
        List<ResizedImage> resizedImages = resize(file);
        for (ResizedImage resizedImage : resizedImages) {
            imageStorageService.saveMultiSizeImage(image, resizedImage.getSize(), new ByteArrayInputStream(resizedImage.getBytes()));
        }
    }

    private List<ResizedImage> resize(MultipartFile file) throws IOException {
        byte[] originalImageBytes = file.getBytes();
        BufferedImage sourceBufferedImage = ImageIO.read(new ByteArrayInputStream(originalImageBytes));
        List<ResizedImage> result = new ArrayList<>();
        for (ImageSize imageSize : ImageSize.values()) {
            if (imageSize.getWidth() >= sourceBufferedImage.getWidth()) {
                result.add(new ResizedImage(originalImageBytes, imageSize));
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
