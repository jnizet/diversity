package fr.mnhn.diversity.service.page;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.mnhn.diversity.repository.Element;
import fr.mnhn.diversity.repository.Page;
import fr.mnhn.diversity.web.home.model.ListElement;
import fr.mnhn.diversity.web.home.model.PageModel;
import fr.mnhn.diversity.web.home.model.SectionElement;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link PageService}
 * @author JB Nizet
 */
class PageServiceTest {
    @Test
    public void shouldPopulatePage() throws JsonProcessingException {
        PageModel pageModel =
            PageModel.builder("Home")
                     .text("title")
                     .section(SectionElement.builder("welcome")
                                            .image("welcomeImage")
                                            .text("welcomeLegend")
                                            .link("tourismLink"))
                     .list(ListElement.builder("carousel")
                                      .image("image")
                                      .text("legend")
                     )
                     .build();

        Page page = new Page(1L, "Home", "home", List.of(
            Element.text(1L, "title", "Diversity"),
            Element.image(2L, "welcome.welcomeImage", "welcome.jpg", "Welcome image"),
            Element.text(3L, "welcome.welcomeLegend", "Welcome here"),
            Element.link(4L, "welcome.tourismLink", "Click here", "https://tourism.fr"),
            Element.image(5L, "carousel.0.image", "carousel0.jpg","0"),
            Element.text(6L, "carousel.0.legend", "Image 0"),
            Element.image(7L, "carousel.1.image", "carousel1.jpg","1"),
            Element.text(8L, "carousel.1.legend", "Image 1")
        ));

        PageService service = new PageService();
        Map<String, Object> result = service.buildPage(pageModel, page);

        assertThat(result).isEqualTo(
            Map.of(
                "title", Element.text(1L, "title", "Diversity"),
                "welcome", Map.of(
                    "welcomeImage", Element.image(2L, "welcome.welcomeImage", "welcome.jpg", "Welcome image"),
                    "welcomeLegend", Element.text(3L, "welcome.welcomeLegend", "Welcome here"),
                    "tourismLink", Element.link(4L, "welcome.tourismLink", "Click here", "https://tourism.fr")
                ),
                "carousel", List.of(
                    Map.of(
                        "image", Element.image(5L, "carousel.0.image", "carousel0.jpg","0"),
                        "legend", Element.text(6L, "carousel.0.legend", "Image 0")
                    ),
                    Map.of(
                        "image", Element.image(7L, "carousel.1.image", "carousel1.jpg","1"),
                        "legend", Element.text(8L, "carousel.1.legend", "Image 1")
                    )
                )
            )
        );
    }
}
