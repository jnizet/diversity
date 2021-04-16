package fr.mnhn.diversity.image;

import fr.mnhn.diversity.common.api.ImportDataSource;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import fr.mnhn.diversity.model.ImageSize;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Service used to handle stored images
 * @author JB Nizet
 */
@Component
public class ImageStorageService {
    private final Path imageDirectory;
    private final WebClient webClient;

    public ImageStorageService(ImageProperties imageProperties, @ImportDataSource WebClient webClient
    ) {
        this.imageDirectory = imageProperties.getDirectory();
        this.webClient = webClient;
    }

    public Mono<ResponseEntity<Resource>> getImageBytesFromImportDataSource(Long imageId){
        return webClient
            .get()
            .uri("/images/{pageModel}/image", imageId)
            .retrieve()
            .toEntity(Resource.class);
    }

    public boolean imageExists(Image image) {
        Path imagePath = imagePath(image);
        return Files.exists(imagePath);
    }

    public boolean multiSizeImageExists(Image image, ImageSize imageSize) {
        Path imagePath = multiSizeImagePath(image, imageSize);
        return Files.exists(imagePath);
    }

    public Resource imageResource(Image image) {
        return new FileSystemResource(imagePath(image));
    }

    public Resource multiSizeImageResource(Image image, ImageSize imageSize) {
        return new FileSystemResource(multiSizeImagePath(image, imageSize));
    }

    public Path imagePath(Image image) {
        ImageType imageType = ImageType.fromContentType(image.getContentType());
        String imageFileName = image.getId() + "." + imageType.getExtension();
        return imageDirectory.resolve(imageFileName);
    }

    public Path multiSizeImagePath(Image image, ImageSize imageSize) {
        ImageType imageType = ImageType.fromContentType(image.getContentType());
        String imageFileName = image.getId() + imageSize.getFileNameSuffix() + "." + imageType.getExtension();
        return imageDirectory.resolve(imageFileName);
    }

    public void saveImage(Image image, InputStream imageStream) throws IOException {
        Files.copy(imageStream, imagePath(image));
    }

    public void saveMultiSizeImage(Image image, ImageSize imageSize, InputStream imageStream) throws IOException {
        Files.copy(imageStream, multiSizeImagePath(image, imageSize));
    }
}
