package fr.mnhn.diversity.image;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.mnhn.diversity.ControllerTest;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import fr.mnhn.diversity.model.ImageSize;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * MVC tests for {@link ImageController}
 * @author JB Nizet
 */
@WebMvcTest(ImageController.class)
@Import(ImageConfig.class)
class ImageControllerTest extends ControllerTest {
    @MockBean
    private ImageRepository mockImageRepository;

    @MockBean
    private ImageStorageService mockImageStorageService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnImage() throws Exception {
        Long id = 42L;
        Image image = new Image(id, ImageType.JPG.getMediaType().toString(), "test.jpg");
        when(mockImageRepository.findById(id)).thenReturn(Optional.of(image));

        byte[] bytes = "jpegImage".getBytes(StandardCharsets.UTF_8);
        when(mockImageStorageService.imageExists(image)).thenReturn(true);
        when(mockImageStorageService.imageResource(image)).thenReturn(new ByteArrayResource(bytes));

        mockMvc.perform(get("/images/{id}/image", id))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.IMAGE_JPEG))
               .andExpect(header().longValue(HttpHeaders.CONTENT_LENGTH, bytes.length))
               .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"test.jpg\""))
               .andExpect(header().exists(HttpHeaders.CACHE_CONTROL))
               .andExpect(content().bytes(bytes));
    }

    @Test
    void shouldReturnDocument() throws Exception {
        Long id = 42L;
        Image image = new Image(id, ImageType.PDF.getMediaType().toString(), "test.pdf");
        when(mockImageRepository.findById(id)).thenReturn(Optional.of(image));

        byte[] bytes = "pdfDocument".getBytes(StandardCharsets.UTF_8);
        when(mockImageStorageService.imageExists(image)).thenReturn(true);
        when(mockImageStorageService.imageResource(image)).thenReturn(new ByteArrayResource(bytes));

        mockMvc.perform(get("/images/{id}/document", id))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_PDF))
               .andExpect(header().longValue(HttpHeaders.CONTENT_LENGTH, bytes.length))
               .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"test.pdf\""))
               .andExpect(header().exists(HttpHeaders.CACHE_CONTROL))
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
        when(mockImageStorageService.imageExists(any())).thenReturn(false);

        mockMvc.perform(get("/images/{id}/image", id))
               .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnImageOfRequestedDimensionIfFound() throws Exception {
        Long id = 42L;
        Image image = new Image(id, ImageType.JPG.getMediaType().toString(), "test.jpg");
        when(mockImageRepository.findById(id)).thenReturn(Optional.of(image));

        byte[] bytes = "jpegImage".getBytes(StandardCharsets.UTF_8);
        when(mockImageStorageService.multiSizeImageExists(image, ImageSize.SM)).thenReturn(true);
        when(mockImageStorageService.multiSizeImageResource(image, ImageSize.SM)).thenReturn(new ByteArrayResource(bytes));

        mockMvc.perform(get("/images/{id}/image/sm", id))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.IMAGE_JPEG))
               .andExpect(header().longValue(HttpHeaders.CONTENT_LENGTH, bytes.length))
               .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"test.jpg\""))
               .andExpect(header().exists(HttpHeaders.CACHE_CONTROL))
               .andExpect(content().bytes(bytes));
    }

    @Test
    void shouldReturnNotFoundIfImageWithDimensionDoesNotExistInDatabase() throws Exception {
        mockMvc.perform(get("/images/{id}/image/sm", 345678L))
               .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnImageOfLargerDimensionIfFound() throws Exception {
        Long id = 42L;
        Image image = new Image(id, ImageType.JPG.getMediaType().toString(), "test.jpg");
        when(mockImageRepository.findById(id)).thenReturn(Optional.of(image));

        byte[] bytes = "jpegImage".getBytes(StandardCharsets.UTF_8);
        when(mockImageStorageService.multiSizeImageExists(image, ImageSize.SM)).thenReturn(false);
        when(mockImageStorageService.multiSizeImageExists(image, ImageSize.MD)).thenReturn(false);
        when(mockImageStorageService.multiSizeImageExists(image, ImageSize.LG)).thenReturn(false);
        when(mockImageStorageService.multiSizeImageExists(image, ImageSize.XL)).thenReturn(true);
        when(mockImageStorageService.multiSizeImageResource(image, ImageSize.XL)).thenReturn(new ByteArrayResource(bytes));

        mockMvc.perform(get("/images/{id}/image/sm", id))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.IMAGE_JPEG))
               .andExpect(header().longValue(HttpHeaders.CONTENT_LENGTH, bytes.length))
               .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"test.jpg\""))
               .andExpect(header().exists(HttpHeaders.CACHE_CONTROL))
               .andExpect(content().bytes(bytes));
    }

    @Test
    void shouldReturnNotFoundIfNoDimensionOfImageExistInFileSystem() throws Exception {
        Long id = 42L;
        when(mockImageRepository.findById(id)).thenReturn(
            Optional.of(new Image(id, ImageType.JPG.getMediaType().toString(), "test.jpg"))
        );
        when(mockImageStorageService.multiSizeImageExists(any(), any())).thenReturn(false);

        mockMvc.perform(get("/images/{id}/image/sm", id))
               .andExpect(status().isNotFound());
    }
}
