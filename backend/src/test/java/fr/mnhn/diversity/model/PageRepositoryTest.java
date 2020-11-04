package fr.mnhn.diversity.model;

import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static fr.mnhn.diversity.common.testing.RepositoryTests.DELETE_ALL;
import static fr.mnhn.diversity.common.testing.RepositoryTests.TRACKER;
import static fr.mnhn.diversity.territory.Territory.*;
import static fr.mnhn.diversity.territory.Territory.SAINT_PIERRE_ET_MIQUELON;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;
import javax.sql.DataSource;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.generator.ValueGenerators;
import fr.mnhn.diversity.common.testing.RepositoryTest;
import fr.mnhn.diversity.home.HomeModel;
import fr.mnhn.diversity.indicator.IndicatorModel;
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
                    insertInto("indicator")
                        .columns("id", "biom_id", "slug")
                        .values(1L, "indicator1", "slug1")
                        .values(2L, "indicator2", "slug2")
                        .values(3L, "indicator3", "slug3")
                        .values(4L, "indicator4", "slug4")
                        .build(),
                    insertInto("indicator_value")
                        .withGeneratedValue("id", ValueGenerators.sequence())
                        .columns("indicator_id", "territory", "value", "unit")
                        .values(1L, REUNION, 11, "%")
                        .values(2L, REUNION, 12, "%")
                        .values(3L, REUNION, 13, "%")
                        .build(),
                    insertInto("page")
                        .columns("id", "name", "model_name", "title")
                        .values(1L, "Home", "home", "Accueil")
                        .values(2L, "gesture1", "gesture", "Ecogeste 1")
                        .values(3L, "gesture2", "gesture", "Ecogeste 2")
                        .values(4L, "slug1", IndicatorModel.INDICATOR_PAGE_MODEL.getName(), "Indicator 1")
                        .values(5L, "slug2", IndicatorModel.INDICATOR_PAGE_MODEL.getName(), "Indicator 2")
                        .values(6L, "slug3", IndicatorModel.INDICATOR_PAGE_MODEL.getName(), "Indicator 3")
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
        assertThat(page.getModelName()).isEqualTo(HomeModel.HOME_PAGE_MODEL.getName());
        assertThat(page.getTitle()).isEqualTo("Accueil");
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
    void shouldFindBasicByNameAndModel() {
        TRACKER.skipNextLaunch();
        BasicPage page = repository.findBasicByNameAndModel("Home", "home").get();

        assertThat(page.getId()).isEqualTo(1L);
        assertThat(page.getName()).isEqualTo("Home");
        assertThat(page.getModelName()).isEqualTo(HomeModel.HOME_PAGE_MODEL.getName());
        assertThat(page.getTitle()).isEqualTo("Accueil");
    }

    @Test
    void shouldFindBasicByModel() {
        TRACKER.skipNextLaunch();
        List<BasicPage> pages = repository.findBasicByModel("unknown");
        assertThat(pages.isEmpty());

        pages = repository.findBasicByModel("gesture");
        assertThat(pages).hasSize(2);

        assertThat(pages.get(0).getId()).isEqualTo(2L);
        assertThat(pages.get(1).getId()).isEqualTo(3L);
    }

    @Test
    void shouldFindAllBasic() {
        TRACKER.skipNextLaunch();
        List<BasicPage> pages = repository.findAllBasic();
        assertThat(pages.stream().map(BasicPage::getId)).containsExactly(1L, 2L, 3L, 4L, 5L, 6L);
    }

    @Test
    void shouldFindNextOfFirstByModel() {
        TRACKER.skipNextLaunch();
        assertThat(repository.findNextOrFirstByModel("unknown", 42L)).isEmpty();

        assertThat(repository.findNextOrFirstByModel("gesture", 2L)
                             .get()
                             .getId()).isEqualTo(3L);
        assertThat(repository.findNextOrFirstByModel("gesture", 3L)
                             .get()
                             .getId()).isEqualTo(2L);
        assertThat(repository.findNextOrFirstByModel("home", 1L)
                             .get()
                             .getId()).isEqualTo(1L);
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

    @Test
    void shouldUpdateName() {
        repository.updateName("gesture1", "gesture", "newgesture1");
        assertThat(repository.findByNameAndModel("newgesture1", "gesture")).isNotEmpty();
    }

    @Test
    void shouldDeleteByNameAndModelName() {
        repository.deleteByNameAndModel("gesture1", "gesture");
        assertThat(repository.findByNameAndModel("gesture1", "gesture")).isEmpty();
    }

    @Test
    void shouldCreateAPage() {
        Text title = new Text(null, "title", "Bienvenu sur le portail");
        Link link = new Link(null, "tourism", "Office du tourisme", "https://office-tourisme.fr");
        Image image = new Image(null, "landscape", 2L, "Beau paysage", true);
        Page pageToCreate = new Page(null, "new-home", "home", "Nouvelle page d'accueil", List.of(title, link, image));

        Page createdPage = repository.create(pageToCreate);
        assertThat(createdPage.getId()).isNotNull();

        Page page = repository.findByNameAndModel("new-home", "home").get();
        assertThat(page.getId()).isEqualTo(createdPage.getId());
        assertThat(page.getTitle()).isEqualTo("Nouvelle page d'accueil");
        assertThat(page.getElements()).hasSize(3);
        assertThat(((Text) page.getElements().get("title")).getText()).isEqualTo(title.getText());
        assertThat(((Link) page.getElements().get("tourism")).getText()).isEqualTo(link.getText());
        assertThat(((Link) page.getElements().get("tourism")).getHref()).isEqualTo(link.getHref());
        assertThat(((Image) page.getElements().get("landscape")).getImageId()).isEqualTo(image.getImageId());
        assertThat(((Image) page.getElements().get("landscape")).getAlt()).isEqualTo(image.getAlt());
    }

    @Test
    void shouldFindRandomIndicatorPagesForTerritory() {
        TRACKER.skipNextLaunch();
        assertThat(repository.findRandomIndicatorPagesForTerritory(2, GUADELOUPE)).isEmpty();
        List<Page> pages = repository.findRandomIndicatorPagesForTerritory(2, REUNION);
        assertThat(pages).hasSize(2);
        pages.forEach(page -> assertThat(page.getId()).isIn(4L, 5L, 6L));
    }
}
