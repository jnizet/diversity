package fr.mnhn.diversity.image;

import static org.assertj.core.api.Assertions.assertThat;

import fr.mnhn.diversity.common.api.ImportDataSource;
import fr.mnhn.diversity.common.api.ImportDataSourceConfig;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Path;

import fr.mnhn.diversity.model.ImageSize;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * MVC tests for {@link ImageStorageService}
 * @author JB Nizet
 */

@RestClientTest
@Import(ImportDataSourceConfig.class)
class ImageStorageServiceTest {

    private ImageStorageService service;
    private Image image = new Image(42L, ImageType.JPG.getMediaType().toString(), "foo.jpg");
    private MockWebServer server;

    @Autowired
    @ImportDataSource
    private WebClient webClient;

    @TempDir
    Path tempDirectory;

    @BeforeEach
    void prepare() throws IOException {
        server = new MockWebServer();
        server.start();

        WebClient mockWebClient = webClient.mutate().baseUrl(server.url("").toString()).build();
        ImageProperties imageProperties = new ImageProperties(tempDirectory);
        service = new ImageStorageService(imageProperties, mockWebClient);
    }

    @Test
    void shouldHandleImage() throws IOException {
        assertThat(service.imageExists(image)).isFalse();
        byte[] bytes = "hello".getBytes();
        service.saveImage(image, new ByteArrayInputStream(bytes));
        assertThat(service.imageExists(image)).isTrue();
        assertThat(service.imageResource(image).getInputStream()).hasBinaryContent(bytes);
        assertThat(tempDirectory.resolve("42.jpg")).exists();
    }

    @Test
    void shouldHandleMultiSizeImage() throws IOException {
        assertThat(service.multiSizeImageExists(image, ImageSize.MD)).isFalse();
        byte[] bytes = "hello".getBytes();
        service.saveMultiSizeImage(image, ImageSize.MD, new ByteArrayInputStream(bytes));
        assertThat(service.multiSizeImageExists(image, ImageSize.MD)).isTrue();
        assertThat(service.multiSizeImageResource(image, ImageSize.MD).getInputStream()).hasBinaryContent(bytes);
        assertThat(tempDirectory.resolve("42-md.jpg")).exists();

        // XL should save without suffix, so that if we ask for the image without size, we get the large version
        service.saveMultiSizeImage(image, ImageSize.XL, new ByteArrayInputStream(bytes));
        assertThat(service.multiSizeImageExists(image, ImageSize.XL)).isTrue();
        assertThat(service.imageExists(image)).isTrue();
        assertThat(service.imageResource(image).getInputStream()).hasBinaryContent(bytes);
        assertThat(tempDirectory.resolve("42.jpg")).exists();
    }
}
