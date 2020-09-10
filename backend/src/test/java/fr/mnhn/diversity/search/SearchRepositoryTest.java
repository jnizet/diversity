package fr.mnhn.diversity.search;

import static com.ninja_squad.dbsetup.Operations.*;
import static fr.mnhn.diversity.common.testing.Tracker.TRACKER;
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

/**
 * Tests for {@link SearchRepository}
 * @author JB Nizet
 */
@RepositoryTest
@Import(SearchRepository.class)
class SearchRepositoryTest {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private SearchRepository repository;

    @BeforeEach
    void prepare() {
        DbSetup dbSetup =
            new DbSetup(
                new DataSourceDestination(dataSource),
                sequenceOf(
                    deleteAllFrom("page_element", "page"),
                    insertInto("page")
                        .columns("id", "name", "model_name", "title")
                        .values(1L, "Home", "home", "Accueil")
                        .values(2L, "About", "about", "A propos")
                        .build(),
                    insertInto("page_element")
                        .columns("id", "page_id", "key", "text", "title")
                        .withDefaultValue("type", "TEXT")
                        .values(11L, 1L, "title", "Compteurs de biodiversité", true)
                        .values(12L, 1L, "intro", "Pour en savoir plus sur les compteurs, voir à propos", false)
                        .values(13L, 1L, "end", "voilà, on vous a parlé des compteurs", false)
                        .values(21L, 2L, "title", "A propos des compteurs", true)
                        .values(22L, 2L, "intro", "c'est bien des compteurs de biodiversité", false)
                        .build()
                )
            );

        TRACKER.launchIfNecessary(dbSetup);
    }

    @Test
    void shouldNotFindUnexistingText() {
        TRACKER.skipNextLaunch();
        assertThat(repository.search("blabla")).isEmpty();
    }

    @Test
    void shouldFindRightPages() {
        TRACKER.skipNextLaunch();
        List<PageSearchResult> results = repository.search("voilà");
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getId()).isEqualTo(1L);
    }

    @Test
    void shouldFindExistingTextIgnoringPluralAndStopWordsAndDisplayBestScoreFirst() {
        TRACKER.skipNextLaunch();
        List<PageSearchResult> results = repository.search("compteur");
        assertThat(results).hasSize(2);
        assertThat(results.get(0).getId()).isEqualTo(1L);
        assertThat(results.get(0).getName()).isEqualTo("Home");
        assertThat(results.get(0).getModelName()).isEqualTo("home");
        assertThat(results.get(0).getTitle()).isEqualTo("Accueil");
        assertThat(results.get(0).getHighlight()).isEqualTo("<b>Compteurs</b> de biodiversité");
        assertThat(results.get(0).getUrl()).isNull();

        assertThat(results.get(1).getId()).isEqualTo(2L);
    }

    @Test
    void shouldPromoteTitles() {
        TRACKER.skipNextLaunch();
        List<PageSearchResult> results = repository.search("biodiversité");
        assertThat(results).hasSize(2);
        assertThat(results.get(0).getId()).isEqualTo(1L);
        assertThat(results.get(1).getId()).isEqualTo(2L);

        results = repository.search("propos");
        assertThat(results).hasSize(2);
        assertThat(results.get(0).getId()).isEqualTo(2L);
        assertThat(results.get(1).getId()).isEqualTo(1L);
    }
}
