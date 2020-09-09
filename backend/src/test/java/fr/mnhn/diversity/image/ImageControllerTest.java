package fr.mnhn.diversity.image;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * MVC tests for {@link ImageController}
 * @author JB Nizet
 */
@WebMvcTest(ImageController.class)
@Import(ImageConfig.class)
class ImageControllerTest {
    @MockBean
    private ImageRepository mockImageRepository;

    @SpyBean
    private ImageProperties mockImageProperties;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ImageController imageController;

    @BeforeEach
    void prepare(@TempDir Path tempDirectory) {
        doReturn(tempDirectory).when(mockImageProperties).getDirectory();
    }

    @Test
    void shouldReturnImage() throws Exception {
        Long id = 42L;
        when(mockImageRepository.findById(id)).thenReturn(
            Optional.of(new Image(id, ImageType.JPG.getMediaType().toString(), "test.jpg"))
        );

        byte[] bytes = "jpegImage".getBytes(StandardCharsets.UTF_8);
        Files.write(mockImageProperties.getDirectory().resolve("42.jpg"), bytes);

        mockMvc.perform(get("/images/{id}/image", id))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.IMAGE_JPEG))
               .andExpect(header().longValue(HttpHeaders.CONTENT_LENGTH, bytes.length))
               .andExpect(content().bytes(bytes));
    }

    @Test
    void shouldReturnNotFoundIfImageDoesNotExistInDatabase() throws Exception {
        mockMvc.perform(get("/images/{id}/image", 345678L))
               .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnNotFoundIfImageDoesNotExistInFileSystem() throws Exception {
        Long id = 42L;
        when(mockImageRepository.findById(id)).thenReturn(
            Optional.of(new Image(id, ImageType.JPG.getMediaType().toString(), "test.jpg"))
        );

        mockMvc.perform(get("/images/{id}/image", 345678L))
               .andExpect(status().isNotFound());
    }
}
