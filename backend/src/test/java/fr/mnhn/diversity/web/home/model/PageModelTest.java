package fr.mnhn.diversity.web.home.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link PageModel} class, its builder, and its elements builders
 * @author JB Nizet
 */
class PageModelTest {
    @Test
    void shouldBuildPageModel() {
        PageModel home =
            PageModel.builder("Home")
                .describedAs("The home page")
                .text("mainTitle")
                .section(SectionElement.builder("welcomeSection")
                    .text(TextElement.builder("paragraph").multiLine())
                )
                .list(ListElement.builder("list")
                    .image("landscape")
                    .link("tourismLink")
                )
                .build();

        assertThat(home.getName()).isEqualTo("Home");
        assertThat(home.getDescription()).isEqualTo("The home page");
        assertThat(home.getElements()).hasSize(3);

        TextElement mainTitle = (TextElement) home.getElements().get(0);
        assertThat(mainTitle.getName()).isEqualTo("mainTitle");
        assertThat(mainTitle.isMultiLine()).isFalse();

        SectionElement section = (SectionElement) home.getElements().get(1);
        assertThat(section.getName()).isEqualTo("welcomeSection");
        assertThat(section.getElements()).hasSize(1);

        TextElement paragraph = (TextElement) section.getElements().get(0);
        assertThat(paragraph.getName()).isEqualTo("paragraph");
        assertThat(paragraph.isMultiLine()).isTrue();

        ListElement list = (ListElement) home.getElements().get(2);
        assertThat(list.getName()).isEqualTo("list");
        assertThat(list.getElements()).hasSize(2);

        ImageElement landscape = (ImageElement) list.getElements().get(0);
        assertThat(landscape.getName()).isEqualTo("landscape");

        LinkElement tourismLink = (LinkElement) list.getElements().get(1);
        assertThat(tourismLink.getName()).isEqualTo("tourismLink");
    }
}
