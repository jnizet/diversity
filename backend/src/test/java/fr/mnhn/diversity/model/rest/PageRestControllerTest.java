package fr.mnhn.diversity.model.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.mnhn.diversity.model.Element;
import fr.mnhn.diversity.model.Image;
import fr.mnhn.diversity.model.Link;
import fr.mnhn.diversity.model.Page;
import fr.mnhn.diversity.model.PageRepository;
import fr.mnhn.diversity.model.Text;
import fr.mnhn.diversity.model.meta.ListElement;
import fr.mnhn.diversity.model.meta.PageModel;
import fr.mnhn.diversity.model.meta.SectionElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


/**
 * Tests for {@link PageRestController}
 */
@WebMvcTest(PageRestController.class)
@ActiveProfiles("test")
class PageRestControllerTest {
    @MockBean
    private PageRepository mockPageRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PageRestController controller;

    @Captor
    private ArgumentCaptor<Page> pageArgumentCaptor;

    private Text title;
    private Text carouselTitle;
    private Link firstCarouselLink;
    private Image firstCarouselImage;
    private Link secondCarouselLink;
    private Image secondCarouselImage;
    private Page page;

    @BeforeEach
    void prepare() {
        // override the model with a simpler one
        controller.setModelsByName(Map.of("home", PageModel.builder("home")
                                                           .text("title", "Titre")
                                                           .section(
                                                               SectionElement.builder("carousel").describedAs("Carousel")
                                                                             .text("title", "Titre du carousel")
                                                                             .list(
                                                                                 ListElement.builder("slides").describedAs("Slides du carousel")
                                                                                            .link("link", "Lien du slide")
                                                                                            .image("image", "Image du slide")
                                                                             )
                                                           )
                                                           .build()));

        title = new Text(101L, "title", "Portail de la diversité");
        carouselTitle = new Text(102L, "carousel.title", "Apprendre en s'amusant");
        firstCarouselLink = new Link(103L, "carousel.slides.0.link", "Raison 1", "https://raison1.fr");
        firstCarouselImage = new Image(104L, "carousel.slides.0.image", 1L, "Raison 1", true);
        secondCarouselLink = new Link(105L, "carousel.slides.1.link", "Raison 2", "https://raison2.fr");
        secondCarouselImage = new Image(106L, "carousel.slides.1.image", 2L, "Raison 2", true);
        page = new Page(43L, "home", "home", "Accueil", List.of(title, carouselTitle, firstCarouselLink, firstCarouselImage, secondCarouselLink, secondCarouselImage));
        when(mockPageRepository.findById(page.getId())).thenReturn(Optional.of(page));
    }

    @Test
    void shouldGet() throws Exception {
        mockMvc.perform(get("/api/pages/43"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(page.getId()))
               .andExpect(jsonPath("$.title").value(page.getTitle()))
               .andExpect(jsonPath("$.elements.length()").value(2))
               // titre
               .andExpect(jsonPath("$.elements[0].type").value(PageElementType.TEXT.name()))
               .andExpect(jsonPath("$.elements[0].description").value("Titre"))
               .andExpect(jsonPath("$.elements[0].name").value(title.getKey()))
               .andExpect(jsonPath("$.elements[0].text").value(title.getText()))
               .andExpect(jsonPath("$.elements[0].multiLine").value(false))
               // carousel
               .andExpect(jsonPath("$.elements[1].type").value(PageElementType.SECTION.name()))
               .andExpect(jsonPath("$.elements[1].elements.length()").value(2))
               // carousel title
               .andExpect(jsonPath("$.elements[1].elements[0].type").value(PageElementType.TEXT.name()))
               .andExpect(jsonPath("$.elements[1].elements[0].description").value("Titre du carousel"))
               .andExpect(jsonPath("$.elements[1].elements[0].name").value("title"))
               .andExpect(jsonPath("$.elements[1].elements[0].text").value(carouselTitle.getText()))
               .andExpect(jsonPath("$.elements[1].elements[0].type").value(PageElementType.TEXT.name()))
               // 2 slides
               .andExpect(jsonPath("$.elements[1].elements[1].type").value(PageElementType.LIST.name()))
               .andExpect(jsonPath("$.elements[1].elements[1].description").value("Slides du carousel"))
               .andExpect(jsonPath("$.elements[1].elements[1].name").value("slides"))
               .andExpect(jsonPath("$.elements[1].elements[1].elements.length()").value(2))
               // each slide has a link and an image
               // first slide
               .andExpect(jsonPath("$.elements[1].elements[1].elements[0].type").value(PageElementType.LIST_UNIT.name()))
               .andExpect(jsonPath("$.elements[1].elements[1].elements[0].elements.length()").value(2))
               .andExpect(jsonPath("$.elements[1].elements[1].elements[0].elements[0].type").value(PageElementType.LINK.name()))
               .andExpect(jsonPath("$.elements[1].elements[1].elements[0].elements[0].href").value(firstCarouselLink.getHref()))
               .andExpect(jsonPath("$.elements[1].elements[1].elements[0].elements[0].text").value(firstCarouselLink.getText()))
               .andExpect(jsonPath("$.elements[1].elements[1].elements[0].elements[1].type").value(PageElementType.IMAGE.name()))
               .andExpect(jsonPath("$.elements[1].elements[1].elements[0].elements[1].alt").value(firstCarouselImage.getAlt()))
               .andExpect(jsonPath("$.elements[1].elements[1].elements[0].elements[1].imageId").value(firstCarouselImage.getImageId()))
               // second slide
               .andExpect(jsonPath("$.elements[1].elements[1].elements[1].type").value(PageElementType.LIST_UNIT.name()))
               .andExpect(jsonPath("$.elements[1].elements[1].elements[1].elements.length()").value(2))
               .andExpect(jsonPath("$.elements[1].elements[1].elements[1].elements[0].type").value(PageElementType.LINK.name()))
               .andExpect(jsonPath("$.elements[1].elements[1].elements[1].elements[0].href").value(secondCarouselLink.getHref()))
               .andExpect(jsonPath("$.elements[1].elements[1].elements[1].elements[0].text").value(secondCarouselLink.getText()))
               .andExpect(jsonPath("$.elements[1].elements[1].elements[1].elements[1].type").value(PageElementType.IMAGE.name()))
               .andExpect(jsonPath("$.elements[1].elements[1].elements[1].elements[1].alt").value(secondCarouselImage.getAlt()))
               .andExpect(jsonPath("$.elements[1].elements[1].elements[1].elements[1].imageId").value(secondCarouselImage.getImageId()));
    }

    @Test
    void shouldUpdate() throws Exception {
        TextCommandDTO carouselTitleCommand = new TextCommandDTO("carousel.title", "Vive la bio-diversité");
        ImageCommandDTO firstCarouselImageCommand = new ImageCommandDTO("carousel.slides.0.image", 3L, "Image 1", true);
        LinkCommandDTO firstCarouselLinkCommand = new LinkCommandDTO("carousel.slides.0.link", "Lien 1", "https://lien1.fr");
        List<ElementCommandDTO> elements = List.of(carouselTitleCommand, firstCarouselImageCommand, firstCarouselLinkCommand);
        PageCommandDTO command = new PageCommandDTO("Portail de la diversité !", elements);

        mockMvc.perform(put("/api/pages/{id}", page.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(command)))
               .andExpect(status().isNoContent());

        verify(mockPageRepository).update(pageArgumentCaptor.capture());
        Page pageToUpdate = pageArgumentCaptor.getValue();
        assertThat(pageToUpdate.getTitle()).isEqualTo("Portail de la diversité !");
        List<Element> elementsToUpdate = pageToUpdate.getElements().values().stream().sorted(Comparator.comparing(Element::getKey)).collect(Collectors.toList());
        assertThat(elementsToUpdate).containsExactly(
            new Image(null, "carousel.slides.0.image", 3L, "Image 1", true),
            new Link(null, "carousel.slides.0.link", "Lien 1", "https://lien1.fr"),
            new Text(null, "carousel.title", "Vive la bio-diversité")
        );
    }
}
