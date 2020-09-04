package fr.mnhn.diversity.repository;

import static com.ninja_squad.dbsetup.Operations.*;
import static fr.mnhn.diversity.repository.Tracker.TRACKER;
import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

/**
 * Tests for {@link PageRepository}
 * @author JB Nizet
 */
@RepositoryTest
@Import(PageRepository.class)
class PageRepositoryTest {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private PageRepository repository;

    @BeforeEach
    void prepare() {
        DbSetup dbSetup =
            new DbSetup(
                new DataSourceDestination(dataSource),
                sequenceOf(
                    deleteAllFrom("page_element", "page"),
                    insertInto("page")
                        .columns("id", "name", "model_name")
                        .values(1L, "Home", "home")
                        .build(),
                    insertInto("page_element")
                        .columns("id", "page_id", "type", "key", "text", "image_id", "alt", "href")
                        .values(11L, 1L, "TEXT", "title", "Welcome to MNHN", null, null, null)
                        .values(12L, 1L, "LINK", "tourism", "Tourism office", null, null, "https://tourism.fr")
                        .values(13L, 1L, "IMAGE", "landscape", null, "image1", "Beautiful landscape", null)
                        .build()
                    )
            );

        TRACKER.launchIfNecessary(dbSetup);
    }

    @Test
    void shouldGetEmptyIfWrongId() {
        TRACKER.skipNextLaunch();
        assertThat(repository.get(123456789L)).isEmpty();
    }

    @Test
    void shouldGetPage() {
        TRACKER.skipNextLaunch();
        Page page = repository.get(1L).get();

        assertThat(page.getId()).isEqualTo(1L);
        assertThat(page.getName()).isEqualTo("Home");
        assertThat(page.getModelName()).isEqualTo("home");
        assertThat(page.getElements()).hasSize(3);

        Image landscape = (Image) page.getElements().get("landscape");
        assertThat(landscape.getType()).isEqualTo(ElementType.IMAGE);
        assertThat(landscape.getId()).isEqualTo(13L);
        assertThat(landscape.getKey()).isEqualTo("landscape");
        assertThat(landscape.getImageId()).isEqualTo("image1");
        assertThat(landscape.getAlt()).isEqualTo("Beautiful landscape");

        Text title = (Text) page.getElements().get("title");
        assertThat(title.getType()).isEqualTo(ElementType.TEXT);
        assertThat(title.getId()).isEqualTo(11L);
        assertThat(title.getKey()).isEqualTo("title");
        assertThat(title.getText()).isEqualTo("Welcome to MNHN");

        Link tourism = (Link) page.getElements().get("tourism");
        assertThat(tourism.getType()).isEqualTo(ElementType.LINK);
        assertThat(tourism.getId()).isEqualTo(12L);
        assertThat(tourism.getKey()).isEqualTo("tourism");
        assertThat(tourism.getHref()).isEqualTo("https://tourism.fr");
        assertThat(tourism.getText()).isEqualTo("Tourism office");
    }

    @Test
    void shouldFindByName() {
        TRACKER.skipNextLaunch();
        Page page = repository.findByName("Home").get();

        assertThat(page.getId()).isEqualTo(1L);
        assertThat(page.getName()).isEqualTo("Home");
        assertThat(page.getElements()).hasSize(3);
    }
}
