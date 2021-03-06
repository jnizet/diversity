package fr.mnhn.diversity.image;

import static com.ninja_squad.dbsetup.Operations.*;
import static com.ninja_squad.dbsetup.Operations.insertInto;
import static fr.mnhn.diversity.common.testing.RepositoryTests.DELETE_ALL;
import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import fr.mnhn.diversity.common.testing.RepositoryTest;
import fr.mnhn.diversity.common.testing.RepositoryTests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

/**
 * Tests for {@link ImageRepository}
 * @author JB Nizet
 */
@RepositoryTest
@Import(ImageRepository.class)
class ImageRepositoryTest {
    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void prepare() {
        DbSetup dbSetup =
            new DbSetup(
                new DataSourceDestination(dataSource),
                sequenceOf(
                    DELETE_ALL,
                    insertInto("image")
                        .columns("id", "content_type", "original_file_name")
                        .values(1L, "image/jpeg", "test.jpg")
                        .build()
                )
            );

        RepositoryTests.TRACKER.launchIfNecessary(dbSetup);
    }

    @Test
    void shouldFindById() {
        assertThat(imageRepository.findById(3456789L)).isEmpty();
        assertThat(imageRepository.findById(1L)).contains(
            new Image(1L, "image/jpeg", "test.jpg")
        );
    }
}
