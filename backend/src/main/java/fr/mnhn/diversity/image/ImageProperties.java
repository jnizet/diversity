package fr.mnhn.diversity.image;

import java.nio.file.Path;
import java.util.Objects;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Configuration properties to configure the way the application gets images
 * @author JB Nizet
 */
@ConfigurationProperties("diversity.images")
@Validated
public class ImageProperties {

    /**
     * The directory where uploaded images are stored
     */
    @NotNull
    private Path directory;

    public ImageProperties() {
    }

    public ImageProperties(Path directory) {
        this.directory = directory;
    }

    public Path getDirectory() {
        return directory;
    }

    public void setDirectory(Path directory) {
        this.directory = directory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ImageProperties)) {
            return false;
        }
        ImageProperties that = (ImageProperties) o;
        return Objects.equals(directory, that.directory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(directory);
    }

    @Override
    public String toString() {
        return "ImageProperties{" +
            "directory=" + directory +
            '}';
    }
}
