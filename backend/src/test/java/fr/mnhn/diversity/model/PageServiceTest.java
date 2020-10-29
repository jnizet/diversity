package fr.mnhn.diversity.model;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Map;

import fr.mnhn.diversity.common.exception.BadRequestException;
import fr.mnhn.diversity.model.meta.ListElement;
import fr.mnhn.diversity.model.meta.PageModel;
import fr.mnhn.diversity.model.meta.SectionElement;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link PageService}
 * @author JB Nizet
 */
class PageServiceTest {
    @Test
    public void shouldPopulatePage() {
        PageModel pageModel =
            PageModel.builder("Home")
                     .withPath("/")
                     .text("title", "Title")
                     .section(SectionElement.builder("welcome")
                                            .describedAs("Welcome section")
                                            .multiSizeImage("welcomeImage", "Welcome")
                                            .text("welcomeLegend", "Welcome legend")
                                            .link("tourismLink", "Tourism link"))
                     .list(ListElement.builder("carousel")
                                      .describedAs("Carousel")
                                      .image("image", "Image")
                                      .text("legend", "Legend")
                     )
                     .build();

        Page page = new Page(1L, "Home", "home", "Welcome", List.of(
            Element.text(1L, "title", "Diversity"),
            Element.image(2L, "welcome.welcomeImage", 1L, "Welcome image", true),
            Element.text(3L, "welcome.welcomeLegend", "Welcome here"),
            Element.link(4L, "welcome.tourismLink", "Click here", "https://tourism.fr"),
            Element.image(5L, "carousel.0.image", 2L, "0"),
            Element.text(6L, "carousel.0.legend", "Image 0"),
            Element.image(7L, "carousel.1.image", 3L, "1"),
            Element.text(8L, "carousel.1.legend", "Image 1")
        ));

        PageService service = new PageService();
        PageContent result = service.buildPageContent(pageModel, page);

        assertThat(result.getId()).isEqualTo(page.getId());
        assertThat(result.getName()).isEqualTo(page.getName());
        assertThat(result.getModelName()).isEqualTo(page.getModelName());
        assertThat(result.getTitle()).isEqualTo(page.getTitle());

        assertThat(result.getContent()).isEqualTo(
            Map.of(
                "title", Element.text(1L, "title", "Diversity"),
                "welcome", Map.of(
                    "welcomeImage", Element.image(2L, "welcome.welcomeImage", 1L, "Welcome image", true),
                    "welcomeLegend", Element.text(3L, "welcome.welcomeLegend", "Welcome here"),
                    "tourismLink", Element.link(4L, "welcome.tourismLink", "Click here", "https://tourism.fr")
                ),
                "carousel", List.of(
                    Map.of(
                        "image", Element.image(5L, "carousel.0.image", 2L, "0", false),
                        "legend", Element.text(6L, "carousel.0.legend", "Image 0")
                    ),
                    Map.of(
                        "image", Element.image(7L, "carousel.1.image", 3L, "1", false),
                        "legend", Element.text(8L, "carousel.1.legend", "Image 1")
                    )
                )
            )
        );
    }

    @Test
    public void shouldPopulatePageWhenListDoesNotContainLeafElement() {
        PageModel pageModel =
            PageModel.builder("Home")
                     .withPath("/")
                     .list(ListElement.builder("carousel")
                                      .describedAs("Carousel")
                                      .section(SectionElement.builder("section")
                                                             .describedAs("Some section")
                                                             .image("image", "Image")
                                                             .text("legend", "Legend")
                                      )
                     )
                     .build();

        Page page = new Page(1L, "Home", "home", "Welcome", List.of(
            Element.image(5L, "carousel.0.section.image", 1L, "0"),
            Element.text(6L, "carousel.0.section.legend", "Image 0"),
            Element.image(7L, "carousel.1.section.image", 2L, "1"),
            Element.text(8L, "carousel.1.section.legend", "Image 1")
        ));

        PageService service = new PageService();
        PageContent result = service.buildPageContent(pageModel, page);

        assertThat(result.getContent()).isEqualTo(
            Map.of(
                "carousel", List.of(
                    Map.of(
                        "section",
                        Map.of(
                           "image", Element.image(5L, "carousel.0.section.image", 1L, "0"),
                           "legend", Element.text(6L, "carousel.0.section.legend", "Image 0")
                        )
                    ),
                    Map.of(
                        "section",
                        Map.of(
                            "image", Element.image(7L, "carousel.1.section.image", 2L, "1"),
                            "legend", Element.text(8L, "carousel.1.section.legend", "Image 1")
                        )
                    )
                )
            )
        );
    }

    @Nested
    class Validation {
        private PageModel pageModel =
            PageModel.builder("Home")
                     .withPath("/")
                     .text("title", "Title")
                     .list(ListElement.builder("carousel")
                                      .describedAs("Carousel")
                                      .multiSizeImage("image", "Image")
                                      .text("legend", "Legend")
                     )
                     .section(SectionElement.builder("presentation")
                                            .describedAs("Presentation")
                                            .text("text", "Text")
                     )
                     .build();
        private PageService service = new PageService();

        @Test
        void shouldValidateCorrectPage() {
            Page correctPage = new Page(1L, "Home", "home", "Welcome", List.of(
                Element.text(1L, "title", "Title"),
                Element.text(2L, "presentation.text", "Text"),
                Element.image(5L, "carousel.0.image", 2L, "0"),
                Element.text(6L, "carousel.0.legend", "Image 0")
            ));
            assertThatCode(() -> service.validatePageContent(pageModel, correctPage)).doesNotThrowAnyException();
        }

        @Test
        void shouldValidatePageWithMissingTitle() {
            Page missingTitlePage = new Page(1L, "Home", "home", "Welcome", List.of(
                Element.text(2L, "presentation.text", "Text"),
                Element.image(5L, "carousel.0.image", 2L, "0"),
                Element.text(6L, "carousel.0.legend", "Image 0")
            ));
            assertThatExceptionOfType(BadRequestException.class).isThrownBy(
                () -> service.validatePageContent(pageModel, missingTitlePage)
            );
        }

        @Test
        void shouldValidatePageWithMissingPresentationText() {
            Page missingTitlePage = new Page(1L, "Home", "home", "Welcome", List.of(
                Element.text(1L, "title", "Title"),
                Element.image(5L, "carousel.0.image", 2L, "0"),
                Element.text(6L, "carousel.0.legend", "Image 0")
            ));
            assertThatExceptionOfType(BadRequestException.class).isThrownBy(
                () -> service.validatePageContent(pageModel, missingTitlePage)
            );
        }

        @Test
        void shouldValidatePageWithMissingLegend() {
            Page missingLegendPage = new Page(1L, "Home", "home", "Welcome", List.of(
                Element.text(2L, "presentation.text", "Text"),
                Element.text(1L, "title", "Title"),
                Element.image(5L, "carousel.0.image", 2L, "0")
            ));
            assertThatExceptionOfType(BadRequestException.class).isThrownBy(
                () -> service.validatePageContent(pageModel, missingLegendPage)
            );
        }

        @Test
        void shouldValidatePageWithAdditionalElementInList() {
            Page additionalElement = new Page(1L, "Home", "home", "Welcome", List.of(
                Element.text(1L, "title", "Title"),
                Element.text(2L, "presentation.text", "Text"),
                Element.image(5L, "carousel.0.image", 2L, "0"),
                Element.text(6L, "carousel.0.legend", "Image 0"),
                Element.text(10L, "carousel.0.waaaat", "Waaaat")
            ));
            assertThatExceptionOfType(BadRequestException.class).isThrownBy(
                () -> service.validatePageContent(pageModel, additionalElement)
            );
        }

        @Test
        void shouldValidatePageWithAdditionalElementInSection() {
            Page additionalElement = new Page(1L, "Home", "home", "Welcome", List.of(
                Element.text(1L, "title", "Title"),
                Element.text(2L, "presentation.waaat", "Waaat"),
                Element.image(5L, "carousel.0.image", 2L, "0"),
                Element.text(6L, "carousel.0.legend", "Image 0")
            ));
            assertThatExceptionOfType(BadRequestException.class).isThrownBy(
                () -> service.validatePageContent(pageModel, additionalElement)
            );
        }

        @Test
        void shouldValidatePageWithHoleInList() {
            Page holeInListPage = new Page(1L, "Home", "home", "Welcome", List.of(
                Element.text(1L, "title", "Title"),
                Element.image(5L, "carousel.0.image", 2L, "0"),
                Element.text(6L, "carousel.0.legend", "Image 0"),
                Element.image(7L, "carousel.2.image", 2L, "2"),
                Element.text(8L, "carousel.2.legend", "Image 2")
            ));
            assertThatExceptionOfType(BadRequestException.class).isThrownBy(
                () -> service.validatePageContent(pageModel, holeInListPage)
            );
        }
    }
}
