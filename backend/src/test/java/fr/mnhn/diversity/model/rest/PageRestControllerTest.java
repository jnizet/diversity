package fr.mnhn.diversity.model.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import fr.mnhn.diversity.media.MediaRepository;
import fr.mnhn.diversity.model.Select;
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
import fr.mnhn.diversity.model.PageService;
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
import reactor.core.publisher.Mono;


/**
 * Tests for {@link PageRestController}
 */
@WebMvcTest(PageRestController.class)
@ActiveProfiles("test")
class PageRestControllerTest {
    @MockBean
    private PageRepository mockPageRepository;

    @MockBean
    private PageService mockPageService;

    @MockBean
    private MediaRepository mockMediaRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PageRestController controller;

    @Captor
    private ArgumentCaptor<Page> pageArgumentCaptor;

    private Text title;
    private Select select;
    private Text carouselTitle;
    private Link firstCarouselLink;
    private Image firstCarouselImage;
    private Link secondCarouselLink;
    private Image secondCarouselImage;
    private Page page;
    private PageModel homePageModel;

    @BeforeEach
    void prepare() {
        // override the model with a simpler one
        homePageModel = PageModel.builder("home")
                                 .withPath("/")
                                 .text("title", "Titre")
                                 .section(
                                     SectionElement.builder("carousel").describedAs("Carousel")
                                                   .text("title", "Titre du carousel")
                                                   .list(
                                                       ListElement.builder("slides").describedAs("Slides du carousel")
                                                                  .link("link", "Lien du slide")
                                                                  .image("image", "Image du slide")
                                                   )
                                                   .select("select", "un select", Map.of("option1", "option1 label", "option2", "option2 label"))
                                 )
                                 .build();
        controller.setModelsByName(Map.of(homePageModel.getName(), homePageModel));

        title = new Text(101L, "title", "Portail de la diversit??");
        select = new Select(107L, "carousel.select", "options1");
        carouselTitle = new Text(102L, "carousel.title", "Apprendre en s'amusant");
        firstCarouselLink = new Link(103L, "carousel.slides.0.link", "Raison 1", "https://raison1.fr");
        firstCarouselImage = new Image(104L, "carousel.slides.0.image", 1L, "Raison 1", true);
        secondCarouselLink = new Link(105L, "carousel.slides.1.link", "Raison 2", "https://raison2.fr");
        secondCarouselImage = new Image(106L, "carousel.slides.1.image", 2L, "Raison 2", true);
        page = new Page(43L, "home", "home", "Accueil", List.of(title, carouselTitle, firstCarouselLink, firstCarouselImage, secondCarouselLink, secondCarouselImage, select));
        when(mockPageRepository.findById(page.getId())).thenReturn(Optional.of(page));
        when(mockPageRepository.findByNameAndModel(page.getName(),page.getModelName())).thenReturn(Optional.of(page));
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
               .andExpect(jsonPath("$.elements[1].elements.length()").value(3))
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
               .andExpect(jsonPath("$.elements[1].elements[1].elements[1].elements[1].imageId").value(secondCarouselImage.getImageId()))
               // select
               .andExpect(jsonPath("$.elements[1].elements[2].type").value(PageElementType.SELECT.name()))
               .andExpect(jsonPath("$.elements[1].elements[2].description").value("un select"))
               .andExpect(jsonPath("$.elements[1].elements[2].value").value(select.getValue()));
    }

    @Test
    void shouldGetByNameAndModel() throws Exception {
        mockMvc.perform(get("/api/pages/home/home"))
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
            .andExpect(jsonPath("$.elements[1].elements.length()").value(3))
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
            .andExpect(jsonPath("$.elements[1].elements[1].elements[1].elements[1].imageId").value(secondCarouselImage.getImageId()))
            // select
            .andExpect(jsonPath("$.elements[1].elements[2].type").value(PageElementType.SELECT.name()))
            .andExpect(jsonPath("$.elements[1].elements[2].description").value("un select"))
            .andExpect(jsonPath("$.elements[1].elements[2].value").value(select.getValue()));
    }

    @Test
    void shouldGetPageDataFromImportSources() throws Exception {
        when(mockPageService.getPageDataFromImportDataSource(page.getModelName(), page.getName())).thenReturn(
            Mono.just("{\"id\":1030,\"title\":\"Mentions l??gales\",\"name\":\"mentions-legales\",\"modelName\":\"legal-terms\",\"description\":null,\"elements\":[{\"type\":\"TEXT\",\"name\":\"title\",\"description\":\"Titre\",\"id\":14288,\"text\":\"Mentions l??gales\",\"multiLine\":false,\"optional\":false},{\"type\":\"LIST\",\"name\":\"paragraphs\",\"description\":\"Paragraphes\",\"elements\":[{\"type\":\"LIST_UNIT\",\"name\":\"paragraphs\",\"description\":\"Paragraphes\",\"elements\":[{\"type\":\"TEXT\",\"name\":\"title\",\"description\":\"Titre du paragraphe\",\"id\":14284,\"text\":\"Conception et ??dition\",\"multiLine\":false,\"optional\":false},{\"type\":\"TEXT\",\"name\":\"text\",\"description\":\"Texte du paragraphe\",\"id\":14290,\"text\":\"**Editeur**\\nMus??um national d'Histoire naturelle\\nUMS Patrimoine Naturel \\nCP41, 36 rue Geoffroy saint-Hilaire\\n75005 Paris\\n\\n**Conception et h??bergement**\\nAgence Ninja Squad\\nwww.ninja-squad.fr\\n\\nJean-Baptiste Giffard\\nwww.digitaldesigner.cool\\n\\nSite h??berg?? par le Mus??um national d???Histoire naturelle\\n\\n**Cr??ation graphique**\\nDesign graphique r??alis?? par : \\nAm??lie Bracq\\nEdouard Sastre\\n\\n**Cr??ation et mise ?? jour du contenu**\\nLe contenu ??ditorial du site est produit et mis ?? jour par l???Unit?? Mixte de Service Patrimoine Naturel (UMS PatriNat) du MNHN.\",\"multiLine\":true,\"optional\":false}]},{\"type\":\"LIST_UNIT\",\"name\":\"paragraphs\",\"description\":\"Paragraphes\",\"elements\":[{\"type\":\"TEXT\",\"name\":\"title\",\"description\":\"Titre du paragraphe\",\"id\":14287,\"text\":\"Conditions g??n??rales d???utilisation\",\"multiLine\":false,\"optional\":false},{\"type\":\"TEXT\",\"name\":\"text\",\"description\":\"Texte du paragraphe\",\"id\":14285,\"text\":\"**Informations contenues dans le site**\\nLe portail Compteur de biodiversit?? outre-mer est un site informatif. \\nIl a pour objectif de pr??senter la biodiversit?? des territoires ultra-marins fran??ais et des actions et initiatives men??es pour la protection de ces ??cosyst??mes.\\nAucune information contenue sur le site ne saurait poss??der ou ??tre interpr??t??e comme ayant une quelconque valeur contractuelle. Les textes ?? caract??re scientifique publi??s dans le site sont valables ?? la date de leur publication et ne sauraient engager leurs auteurs dans le futur.\\nToute information contenue sur le site peut ??tre modifi??e, ?? tout moment sans pr??avis, par le Mus??um national d???histoire naturelle. Les informations contenues sur le site ne constituent pas une offre de services ou de produits ni une sollicitation commerciale de quelque nature que ce soit, ni une recommandation ou un conseil. Le Mus??um national d???histoire naturelle ne sera pas tenu responsable pour toute d??cision prise ou non sur la base d???une information contenue sur le site, ni pour l???utilisation qui pourrait en ??tre faite par un tiers. Ces informations ne sauraient engager la responsabilit?? du Mus??um national d???histoire naturelle.\\n\\n**Propri??t?? intellectuelle**\\nL???ensemble du site ?? Compteur de biodiversit?? outre-mer ?? rel??ve de la l??gislation fran??aise et internationale sur le droit d???auteur et la propri??t?? intellectuelle.\\nTous les ??l??ments pr??sent??s sur ce site (textes, photographies, vid??os, cr??ations graphiques, illustrations) sont soumis ?? la l??gislation relative au droit des informations publiques et sont couverts par le droit d'auteur. Ces ??l??ments restent la propri??t?? exclusive du Mus??um national d???Histoire naturelle et/ou de ses auteurs.\\nLa reproduction de tout ou partie de ce site sur un support ??lectronique quel qu???il soit est formellement interdite sauf autorisation expresse du directeur de la publication. La reproduction de ce site sur un support papier est autoris??e pour des fins personnelles, associatives ou professionnelles et sous r??serve du respect des trois conditions suivantes :\\n??? gratuit?? de la diffusion ;\\n??? respect de l???int??grit?? des documents reproduits : pas de modification ni alt??ration d???aucune sorte ;\\n??? citation claire et lisible de la source indiquant le nom \\\"Mus??um national d???Histoire naturelle\\\", l???adresse internet du site web du MNHN : www.mnhn.fr et la mention : \\\"?? sur www.mnhn.fr - Reproduction interdite ??? Tous droits r??serv??s\\\".\\nToutes diffusions ou utilisations ?? des fins commerciales ou publicitaires des informations sont strictement interdites. Pour d???autres utilisations envisag??es, veuillez nous consulter.\\n\\n**Cr??dits photographiques**\\nToutes les photographies pr??sentes sur le site sont cr??dit??es de leurs auteurs et de la licence associ??e ?? leur utilisation si elle est mentionn??e.\\nLes contenus sous licence Creative Commons ont une mention sp??cifique avec le type de licence qui permet ?? l???utilisateur d???aller consulter les conditions d???utilisation de l???image concern??e. \\nExemple :\\n(CC BY-NC-SA 3.0)\",\"multiLine\":true,\"optional\":false}]},{\"type\":\"LIST_UNIT\",\"name\":\"paragraphs\",\"description\":\"Paragraphes\",\"elements\":[{\"type\":\"TEXT\",\"name\":\"title\",\"description\":\"Titre du paragraphe\",\"id\":14289,\"text\":\"Mentions relatives ?? l???utilisation de cookies\",\"multiLine\":false,\"optional\":false},{\"type\":\"TEXT\",\"name\":\"text\",\"description\":\"Texte du paragraphe\",\"id\":14286,\"text\":\"**Outils de mesure d???audience**\\nLe site utilise l???outil d???analyse Matomo. Les traceurs de mesures d???audience issus de Matomo ne n??cessitent pas de recueil pr??alable du consentement de l???utilisateur du fait qu???ils servent uniquement ?? produire des donn??es statistiques anonymes.\\nPour en savoir plus :\\nhttps://fr.matomo.org/privacy/\\n\\n**Politique de confidentialit?? et RGPD**\\nLe Mus??um national d???Histoire naturelle porte la plus grande attention ?? vos donn??es ?? caract??re personnel et s???engage ?? les prot??ger.\\nIl s'engage ?? ce que la collecte et le traitement de donn??es ?? caract??re personnel, effectu??s ?? partir du pr??sent site, soient conformes ?? la loi n??78-17 du 6 janvier 1978 modifi??e relative ?? l'informatique, aux fichiers et aux libert??s ainsi qu???au R??glement (UE) 2016/679 du Parlement europ??en et du Conseil du 27 avril 2016 applicable le 25 mai 2018. \\n\\n**Donn??es personnelles**\\nSauf stipulation contraire directement mentionn??e, aucune donn??e personnelle n???est collect??e ni trait??e sur le portail ?? Compteur de biodiversit?? outre-mer ??.\",\"multiLine\":true,\"optional\":false}]}]}]}"));
        mockMvc.perform(get("/api/pages/import/home/home"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value("1030"))
            .andExpect(jsonPath("$.title").value("Mentions l??gales"))
            .andExpect(jsonPath("$.elements.length()").value(2))
            // titre
            .andExpect(jsonPath("$.elements[0].type").value(PageElementType.TEXT.name()))
            .andExpect(jsonPath("$.elements[0].description").value("Titre"))
            .andExpect(jsonPath("$.elements[0].name").value("title"))
            .andExpect(jsonPath("$.elements[0].text").value("Mentions l??gales"))
            .andExpect(jsonPath("$.elements[0].multiLine").value(false))
            // carousel
            .andExpect(jsonPath("$.elements[1].type").value(PageElementType.LIST.name()))
            .andExpect(jsonPath("$.elements[1].elements.length()").value(3));
       }

    @Test
    void shouldGetModel() throws Exception {
        mockMvc.perform(get("/api/pages/models/home"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").doesNotExist())
               .andExpect(jsonPath("$.title").value(""))
               .andExpect(jsonPath("$.elements.length()").value(2))
               // titre
               .andExpect(jsonPath("$.elements[0].type").value(PageElementType.TEXT.name()))
               .andExpect(jsonPath("$.elements[0].description").value("Titre"))
               .andExpect(jsonPath("$.elements[0].name").value(title.getKey()))
               .andExpect(jsonPath("$.elements[0].text").value(""))
               .andExpect(jsonPath("$.elements[0].multiLine").value(false))
               // carousel
               .andExpect(jsonPath("$.elements[1].type").value(PageElementType.SECTION.name()))
               .andExpect(jsonPath("$.elements[1].elements.length()").value(3))
               // carousel title
               .andExpect(jsonPath("$.elements[1].elements[0].type").value(PageElementType.TEXT.name()))
               .andExpect(jsonPath("$.elements[1].elements[0].description").value("Titre du carousel"))
               .andExpect(jsonPath("$.elements[1].elements[0].name").value("title"))
               .andExpect(jsonPath("$.elements[1].elements[0].text").value(""))
               .andExpect(jsonPath("$.elements[1].elements[0].type").value(PageElementType.TEXT.name()))
               // only one slide here, as this is the model
               .andExpect(jsonPath("$.elements[1].elements[1].type").value(PageElementType.LIST.name()))
               .andExpect(jsonPath("$.elements[1].elements[1].description").value("Slides du carousel"))
               .andExpect(jsonPath("$.elements[1].elements[1].name").value("slides"))
               .andExpect(jsonPath("$.elements[1].elements[1].elements.length()").value(1))
               // slide
               .andExpect(jsonPath("$.elements[1].elements[1].elements[0].type").value(PageElementType.LIST_UNIT.name()))
               .andExpect(jsonPath("$.elements[1].elements[1].elements[0].elements.length()").value(2))
               .andExpect(jsonPath("$.elements[1].elements[1].elements[0].elements[0].type").value(PageElementType.LINK.name()))
               .andExpect(jsonPath("$.elements[1].elements[1].elements[0].elements[0].href").value(""))
               .andExpect(jsonPath("$.elements[1].elements[1].elements[0].elements[0].text").value(""))
               .andExpect(jsonPath("$.elements[1].elements[1].elements[0].elements[1].type").value(PageElementType.IMAGE.name()))
               .andExpect(jsonPath("$.elements[1].elements[1].elements[0].elements[1].alt").value(""))
               .andExpect(jsonPath("$.elements[1].elements[1].elements[0].elements[1].imageId").doesNotExist())
                // select
               .andExpect(jsonPath("$.elements[1].elements[2].type").value(PageElementType.SELECT.name()))
               .andExpect(jsonPath("$.elements[1].elements[2].description").value("un select"))
               .andExpect(jsonPath("$.elements[1].elements[2].name").value("select"))
               .andExpect(jsonPath("$.elements[1].elements[2].options").isMap())
               .andExpect(jsonPath("$.elements[1].elements[2].value").value(""));
    }

    @Test
    void shouldUpdate() throws Exception {
        TextCommandDTO carouselTitleCommand = new TextCommandDTO("carousel.title", "Vive la bio-diversit??");
        ImageCommandDTO firstCarouselImageCommand = new ImageCommandDTO("carousel.slides.0.image", 3L, "Image 1", true);
        LinkCommandDTO firstCarouselLinkCommand = new LinkCommandDTO("carousel.slides.0.link", "Lien 1", "https://lien1.fr");
        List<ElementCommandDTO> elements = List.of(carouselTitleCommand, firstCarouselImageCommand, firstCarouselLinkCommand);
        PageCommandDTO command = new PageCommandDTO("Portail de la diversit?? !", "home", elements);

        mockMvc.perform(put("/api/pages/{id}", page.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(command)))
               .andExpect(status().isNoContent());

        verify(mockPageRepository).update(pageArgumentCaptor.capture());
        Page pageToUpdate = pageArgumentCaptor.getValue();
        assertThat(pageToUpdate.getTitle()).isEqualTo("Portail de la diversit?? !");
        List<Element> elementsToUpdate = pageToUpdate.getElements().values().stream().sorted(Comparator.comparing(Element::getKey)).collect(Collectors.toList());
        assertThat(elementsToUpdate).containsExactly(
            new Image(null, "carousel.slides.0.image", 3L, "Image 1", true),
            new Link(null, "carousel.slides.0.link", "Lien 1", "https://lien1.fr"),
            new Text(null, "carousel.title", "Vive la bio-diversit??")
        );
        verify(mockPageService).validatePageContent(homePageModel, pageToUpdate);
    }

    @Test
    void shouldCreate() throws Exception {
        TextCommandDTO titleCommand = new TextCommandDTO("title", "Bienvenu");
        TextCommandDTO carouselTitleCommand = new TextCommandDTO("carousel.title", "Vive la bio-diversit??");
        SelectCommandDTO carouselSelectCommand = new SelectCommandDTO("carousel.select", "option2");
        ImageCommandDTO firstCarouselImageCommand = new ImageCommandDTO("carousel.slides.0.image", 3L, "Image 1", true);
        LinkCommandDTO firstCarouselLinkCommand = new LinkCommandDTO("carousel.slides.0.link", "Lien 1", "https://lien1.fr");
        List<ElementCommandDTO> elements = List.of(titleCommand, carouselTitleCommand, firstCarouselImageCommand, firstCarouselLinkCommand, carouselSelectCommand);
        PageCommandDTO command = new PageCommandDTO("Portail de la diversit?? !", "Home", elements);

        Text title = new Text(257L, titleCommand.getKey(), titleCommand.getText());
        Text carouselTitle = new Text(258L, carouselTitleCommand.getKey(), carouselTitleCommand.getText());
        Select carouselSelect = new Select(261L, carouselSelectCommand.getKey(), carouselSelectCommand.getValue());
        Image firstCarouselImage = new Image(259L, firstCarouselImageCommand.getKey(), firstCarouselImageCommand.getImageId(), firstCarouselImageCommand.getAlt(), firstCarouselImageCommand.isMultiSize());
        Link firstCarouselLink = new Link(260L, firstCarouselLinkCommand.getKey(), firstCarouselLinkCommand.getText(), firstCarouselLinkCommand.getHref());
        Page createdPage = new Page(256L, command.getName(), "home", command.getTitle(), List.of(title, carouselTitle, firstCarouselImage, firstCarouselLink, carouselSelect));
        when(mockPageRepository.create(any())).thenReturn(createdPage);

        mockMvc.perform(post("/api/pages/models/home")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(command)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id").value(256L));

        verify(mockPageRepository).create(pageArgumentCaptor.capture());
        Page pageToCreate = pageArgumentCaptor.getValue();
        assertThat(pageToCreate.getTitle()).isEqualTo("Portail de la diversit?? !");
        List<Element> elementsToCreate = pageToCreate.getElements().values().stream().sorted(Comparator.comparing(Element::getKey)).collect(Collectors.toList());
        assertThat(elementsToCreate).containsExactly(
            new Select(null, "carousel.select", "option2"),
            new Image(null, "carousel.slides.0.image", 3L, "Image 1", true),
            new Link(null, "carousel.slides.0.link", "Lien 1", "https://lien1.fr"),
            new Text(null, "carousel.title", "Vive la bio-diversit??"),
            new Text(null, "title", "Bienvenu")
        );

        verify(mockPageService).validatePageContent(homePageModel, pageToCreate);
    }
}
