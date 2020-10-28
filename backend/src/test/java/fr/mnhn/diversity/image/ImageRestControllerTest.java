package fr.mnhn.diversity.image;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import javax.imageio.ImageIO;

import fr.mnhn.diversity.model.ImageSize;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.StreamUtils;

/**
 * MVC tests for {@link ImageRestController}
 * @author JB Nizet
 */
@WebMvcTest(ImageRestController.class)
class ImageRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImageStorageService mockImageStorageService;

    @MockBean
    private ImageRepository mockImageRepository;

    @Test
    void shouldThrowIfInvalidContentType() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/images")
                                              .file(new MockMultipartFile("file", "foo.txt", "text/plain", "hello".getBytes())))
               .andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowIfMultiSizeAndNotJPEG() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/images")
                                              .file(new MockMultipartFile("file", "foo.gif", "image/gif", "hello".getBytes()))
                                              .param("multisize", "true"))
               .andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowIfDocumentAndNotPDF() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/images")
                                              .file(new MockMultipartFile("file", "foo.gif", "image/gif", "hello".getBytes()))
                                              .param("document", "true"))
               .andExpect(status().isBadRequest());
    }

    @Test
    void shouldSaveSimpleImage() throws Exception {
        byte[] smallOriginal = StreamUtils.copyToByteArray(ImageRestControllerTest.class.getResourceAsStream("/images/small-original.jpg"));
        Image image = new Image(null, ImageType.JPG.getMediaType().toString(), "foo.jpg");
        Image savedImage = image.withId(42L);

        when(mockImageRepository.create(image)).thenReturn(savedImage);
        AtomicReference<byte[]> savedImageBytes = new AtomicReference<>();
        doAnswer(invocation -> {
            savedImageBytes.set(StreamUtils.copyToByteArray(invocation.getArgument(1, InputStream.class)));
            return null;
        }).when(mockImageStorageService).saveImage(any(), any());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/images")
                                              .file(new MockMultipartFile("file", "foo.jpg", ImageType.JPG.getMediaType().toString(), smallOriginal)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id").value(savedImage.getId()));

        assertThat(savedImageBytes.get()).isEqualTo(smallOriginal);
    }

    @Test
    void shouldSaveSmallMultiSizeImage() throws Exception {
        byte[] smallOriginal = StreamUtils.copyToByteArray(ImageRestControllerTest.class.getResourceAsStream("/images/small-original.jpg"));
        Image image = new Image(null, ImageType.JPG.getMediaType().toString(), "foo.jpg");
        Image savedImage = image.withId(42L);

        when(mockImageRepository.create(image)).thenReturn(savedImage);
        List<byte[]> savedImageBytesList = new ArrayList<>();
        doAnswer(invocation -> {
            savedImageBytesList.add(StreamUtils.copyToByteArray(invocation.getArgument(2, InputStream.class)));
            return null;
        }).when(mockImageStorageService).saveMultiSizeImage(any(), any(), any());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/images")
                                              .file(new MockMultipartFile("file", "foo.jpg", ImageType.JPG.getMediaType().toString(), smallOriginal))
                                              .param("multisize", "true"))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id").value(savedImage.getId()));

        assertThat(savedImageBytesList).hasSize(ImageSize.values().length);
        assertThat(savedImageBytesList.get(ImageSize.XL.ordinal())).isEqualTo(smallOriginal);
        assertThat(savedImageBytesList.get(ImageSize.LG.ordinal())).isEqualTo(smallOriginal);
        assertThat(savedImageBytesList.get(ImageSize.MD.ordinal())).isNotEqualTo(smallOriginal);
        assertThat(savedImageBytesList.get(ImageSize.SM.ordinal())).isNotEqualTo(smallOriginal);

        checkReadableImage(savedImageBytesList.get(ImageSize.MD.ordinal()), ImageSize.MD.getWidth());
        checkReadableImage(savedImageBytesList.get(ImageSize.SM.ordinal()), ImageSize.SM.getWidth());
    }

    @Test
    void shouldSaveLargeMultiSizeImage() throws Exception {
        byte[] largeOriginal = StreamUtils.copyToByteArray(ImageRestControllerTest.class.getResourceAsStream("/images/large-original.jpg"));
        Image image = new Image(null, ImageType.JPG.getMediaType().toString(), "foo.jpg");
        Image savedImage = image.withId(42L);

        when(mockImageRepository.create(image)).thenReturn(savedImage);
        List<byte[]> savedImageBytesList = new ArrayList<>();
        doAnswer(invocation -> {
            savedImageBytesList.add(StreamUtils.copyToByteArray(invocation.getArgument(2, InputStream.class)));
            return null;
        }).when(mockImageStorageService).saveMultiSizeImage(any(), any(), any());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/images")
                                              .file(new MockMultipartFile("file", "foo.jpg", ImageType.JPG.getMediaType().toString(), largeOriginal))
                                              .param("multisize", "true"))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id").value(savedImage.getId()));

        assertThat(savedImageBytesList).hasSize(ImageSize.values().length);

        checkReadableImage(savedImageBytesList.get(ImageSize.XL.ordinal()), ImageSize.XL.getWidth());
        checkReadableImage(savedImageBytesList.get(ImageSize.LG.ordinal()), ImageSize.LG.getWidth());
        checkReadableImage(savedImageBytesList.get(ImageSize.MD.ordinal()), ImageSize.MD.getWidth());
        checkReadableImage(savedImageBytesList.get(ImageSize.SM.ordinal()), ImageSize.SM.getWidth());
    }

    private void checkReadableImage(byte[] bytes, int expectedWidth) throws IOException {
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes));
        assertThat(image.getWidth()).isEqualTo(expectedWidth);
    }
}
