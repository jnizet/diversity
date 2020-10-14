package fr.mnhn.diversity.model;

import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static fr.mnhn.diversity.common.testing.RepositoryTests.DELETE_ALL;
import static fr.mnhn.diversity.common.testing.RepositoryTests.TRACKER;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import javax.sql.DataSource;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import fr.mnhn.diversity.common.testing.RepositoryTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;

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
                    DELETE_ALL,
                    insertInto("page")
                        .columns("id", "name", "model_name", "title")
                        .values(1L, "Home", "home", "Accueil")
                        .values(2L, "gesture1", "gesture", "Ecogeste")
                        .values(3L, "gesture2", "gesture", "Ecogestes")
                        .build(),
                    insertInto("image")
                        .columns("id", "content_type", "original_file_name")
                        .values(1L, MediaType.IMAGE_JPEG_VALUE, "beautiful-landscape.jpg")
                        .values(2L, MediaType.IMAGE_JPEG_VALUE, "beau-paysage.jpg")
                        .build(),
                    insertInto("page_element")
                        .columns("id", "page_id", "type", "key", "text", "image_id", "alt", "href")
                        .values(11L, 1L, "TEXT", "title", "Welcome to MNHN", null, null, null)
                        .values(12L, 1L, "LINK", "tourism", "Tourism office", null, null, "https://tourism.fr")
                        .values(13L, 1L, "IMAGE", "landscape", null, 1L, "Beautiful landscape", null)
                        .values(21L, 2L, "TEXT", "title", "Gesture 1", null, null, null)
                        .values(31L, 3L, "TEXT", "title", "Gesture 2", null, null, null)
                        .build()
                    )
            );

        TRACKER.launchIfNecessary(dbSetup);
    }

    @Test
    void shouldGetEmptyIfWrongId() {
        TRACKER.skipNextLaunch();
        assertThat(repository.findById(123456789L)).isEmpty();
    }

    @Test
    void shouldFindById() {
        TRACKER.skipNextLaunch();
        Page page = repository.findById(1L).get();

        assertThat(page.getId()).isEqualTo(1L);
        assertThat(page.getName()).isEqualTo("Home");
        assertThat(page.getModelName()).isEqualTo("home");
        assertThat(page.getTitle()).isEqualTo("Accueil");
        assertThat(page.getElements()).hasSize(3);

        Image landscape = (Image) page.getElements().get("landscape");
        assertThat(landscape.getType()).isEqualTo(ElementType.IMAGE);
        assertThat(landscape.getId()).isEqualTo(13L);
        assertThat(landscape.getKey()).isEqualTo("landscape");
        assertThat(landscape.getImageId()).isEqualTo(1L);
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
    void shouldFindByNameAndModel() {
        TRACKER.skipNextLaunch();
        Page page = repository.findByNameAndModel("Home", "home").get();

        assertThat(page.getId()).isEqualTo(1L);
        assertThat(page.getName()).isEqualTo("Home");
        assertThat(page.getElements()).hasSize(3);
    }

    @Test
    void shouldFindByModel() {
        TRACKER.skipNextLaunch();
        List<Page> pages = repository.findByModel("unknown");
        assertThat(pages.isEmpty());

        pages = repository.findByModel("gesture");
        assertThat(pages).hasSize(2);

        assertThat(pages.get(0).getId()).isEqualTo(2L);
        assertThat(pages.get(0).getElements()).hasSize(1);
        assertThat(pages.get(1).getId()).isEqualTo(3L);
        assertThat(pages.get(1).getElements()).hasSize(1);
    }

    @Test
    void shouldUpdateAPage() {
        Page page = repository.findByNameAndModel("Home", "home").get();

        // update the text
        Text title = (Text) page.getElements().get("title");
        Text updatedTitle = new Text(title.getId(), title.getKey(), "Bienvenu sur le portail");

        // update the link
        Link link = (Link) page.getElements().get("tourism");
        Link updatedLink = new Link(link.getId(), link.getKey(), "Office du tourisme", "https://office-tourisme.fr");

        // update the image
        Image image = (Image) page.getElements().get("landscape");
        Image updatedImage = new Image(image.getId(), image.getKey(), 2L, "Beau paysage", true);

        // update the page title
        Page updatedPage = new Page(page.getId(), page.getName(), page.getModelName(), "Portail diversité", List.of(updatedImage, updatedTitle, updatedLink));
        repository.update(updatedPage);

        page = repository.findByNameAndModel("Home", "home").get();
        assertThat(page.getId()).isEqualTo(1L);
        assertThat(page.getTitle()).isEqualTo("Portail diversité");
        assertThat(page.getElements()).hasSize(3);
        assertThat(((Text) page.getElements().get("title")).getText()).isEqualTo(updatedTitle.getText());
        assertThat(((Link) page.getElements().get("tourism")).getText()).isEqualTo(updatedLink.getText());
        assertThat(((Link) page.getElements().get("tourism")).getHref()).isEqualTo(updatedLink.getHref());
        assertThat(((Image) page.getElements().get("landscape")).getImageId()).isEqualTo(updatedImage.getImageId());
        assertThat(((Image) page.getElements().get("landscape")).getAlt()).isEqualTo(updatedImage.getAlt());

    }
}
