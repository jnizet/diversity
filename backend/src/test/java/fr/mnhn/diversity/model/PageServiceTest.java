package fr.mnhn.diversity.model;

import static org.assertj.core.api.Assertions.*;

import fr.mnhn.diversity.common.api.ImportDataSource;
import fr.mnhn.diversity.common.api.ImportDataSourceConfig;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import fr.mnhn.diversity.common.exception.BadRequestException;
import fr.mnhn.diversity.model.meta.ListElement;
import fr.mnhn.diversity.model.meta.MultiListElement;
import fr.mnhn.diversity.model.meta.PageModel;
import fr.mnhn.diversity.model.meta.SectionElement;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Unit tests for {@link PageService}
 *
 * @author JB Nizet
 */


@RestClientTest
@Import(ImportDataSourceConfig.class)
class PageServiceTest {

    @Autowired
    @ImportDataSource
    private WebClient webClient;


    private PageService service;
    private MockWebServer server;

    @BeforeEach
    void prepare() throws IOException {
        server = new MockWebServer();
        server.start();

        WebClient mockWebClient = webClient.mutate().baseUrl(server.url("").toString()).build();

        service = new PageService(mockWebClient, "token");
    }

    @AfterEach
    void cleanup() throws IOException {
        server.shutdown();
    }

    @Test
    public void shouldGetPageDataFromImportDataSource() throws Exception {
        String body = "{\"id\":1030,\"title\":\"Mentions légales\",\"name\":\"mentions-legales\",\"modelName\":\"legal-terms\",\"description\":null,\"elements\":[{\"type\":\"TEXT\",\"name\":\"title\",\"description\":\"Titre\",\"id\":14288,\"text\":\"Mentions légales\",\"multiLine\":false,\"optional\":false},{\"type\":\"LIST\",\"name\":\"paragraphs\",\"description\":\"Paragraphes\",\"elements\":[{\"type\":\"LIST_UNIT\",\"name\":\"paragraphs\",\"description\":\"Paragraphes\",\"elements\":[{\"type\":\"TEXT\",\"name\":\"title\",\"description\":\"Titre du paragraphe\",\"id\":14284,\"text\":\"Conception et édition\",\"multiLine\":false,\"optional\":false},{\"type\":\"TEXT\",\"name\":\"text\",\"description\":\"Texte du paragraphe\",\"id\":14290,\"text\":\"**Editeur**\\nMuséum national d'Histoire naturelle\\nUMS Patrimoine Naturel \\nCP41, 36 rue Geoffroy saint-Hilaire\\n75005 Paris\\n\\n**Conception et hébergement**\\nAgence Ninja Squad\\nwww.ninja-squad.fr\\n\\nJean-Baptiste Giffard\\nwww.digitaldesigner.cool\\n\\nSite hébergé par le Muséum national d’Histoire naturelle\\n\\n**Création graphique**\\nDesign graphique réalisé par : \\nAmélie Bracq\\nEdouard Sastre\\n\\n**Création et mise à jour du contenu**\\nLe contenu éditorial du site est produit et mis à jour par l’Unité Mixte de Service Patrimoine Naturel (UMS PatriNat) du MNHN.\",\"multiLine\":true,\"optional\":false}]},{\"type\":\"LIST_UNIT\",\"name\":\"paragraphs\",\"description\":\"Paragraphes\",\"elements\":[{\"type\":\"TEXT\",\"name\":\"title\",\"description\":\"Titre du paragraphe\",\"id\":14287,\"text\":\"Conditions générales d’utilisation\",\"multiLine\":false,\"optional\":false},{\"type\":\"TEXT\",\"name\":\"text\",\"description\":\"Texte du paragraphe\",\"id\":14285,\"text\":\"**Informations contenues dans le site**\\nLe portail Compteur de biodiversité outre-mer est un site informatif. \\nIl a pour objectif de présenter la biodiversité des territoires ultra-marins français et des actions et initiatives menées pour la protection de ces écosystèmes.\\nAucune information contenue sur le site ne saurait posséder ou être interprétée comme ayant une quelconque valeur contractuelle. Les textes à caractère scientifique publiés dans le site sont valables à la date de leur publication et ne sauraient engager leurs auteurs dans le futur.\\nToute information contenue sur le site peut être modifiée, à tout moment sans préavis, par le Muséum national d’histoire naturelle. Les informations contenues sur le site ne constituent pas une offre de services ou de produits ni une sollicitation commerciale de quelque nature que ce soit, ni une recommandation ou un conseil. Le Muséum national d’histoire naturelle ne sera pas tenu responsable pour toute décision prise ou non sur la base d’une information contenue sur le site, ni pour l’utilisation qui pourrait en être faite par un tiers. Ces informations ne sauraient engager la responsabilité du Muséum national d’histoire naturelle.\\n\\n**Propriété intellectuelle**\\nL’ensemble du site « Compteur de biodiversité outre-mer » relève de la législation française et internationale sur le droit d’auteur et la propriété intellectuelle.\\nTous les éléments présentés sur ce site (textes, photographies, vidéos, créations graphiques, illustrations) sont soumis à la législation relative au droit des informations publiques et sont couverts par le droit d'auteur. Ces éléments restent la propriété exclusive du Muséum national d’Histoire naturelle et/ou de ses auteurs.\\nLa reproduction de tout ou partie de ce site sur un support électronique quel qu’il soit est formellement interdite sauf autorisation expresse du directeur de la publication. La reproduction de ce site sur un support papier est autorisée pour des fins personnelles, associatives ou professionnelles et sous réserve du respect des trois conditions suivantes :\\n• gratuité de la diffusion ;\\n• respect de l’intégrité des documents reproduits : pas de modification ni altération d’aucune sorte ;\\n• citation claire et lisible de la source indiquant le nom \\\"Muséum national d’Histoire naturelle\\\", l’adresse internet du site web du MNHN : www.mnhn.fr et la mention : \\\"© sur www.mnhn.fr - Reproduction interdite – Tous droits réservés\\\".\\nToutes diffusions ou utilisations à des fins commerciales ou publicitaires des informations sont strictement interdites. Pour d’autres utilisations envisagées, veuillez nous consulter.\\n\\n**Crédits photographiques**\\nToutes les photographies présentes sur le site sont créditées de leurs auteurs et de la licence associée à leur utilisation si elle est mentionnée.\\nLes contenus sous licence Creative Commons ont une mention spécifique avec le type de licence qui permet à l’utilisateur d’aller consulter les conditions d’utilisation de l’image concernée. \\nExemple :\\n(CC BY-NC-SA 3.0)\",\"multiLine\":true,\"optional\":false}]},{\"type\":\"LIST_UNIT\",\"name\":\"paragraphs\",\"description\":\"Paragraphes\",\"elements\":[{\"type\":\"TEXT\",\"name\":\"title\",\"description\":\"Titre du paragraphe\",\"id\":14289,\"text\":\"Mentions relatives à l’utilisation de cookies\",\"multiLine\":false,\"optional\":false},{\"type\":\"TEXT\",\"name\":\"text\",\"description\":\"Texte du paragraphe\",\"id\":14286,\"text\":\"**Outils de mesure d’audience**\\nLe site utilise l’outil d’analyse Matomo. Les traceurs de mesures d’audience issus de Matomo ne nécessitent pas de recueil préalable du consentement de l’utilisateur du fait qu’ils servent uniquement à produire des données statistiques anonymes.\\nPour en savoir plus :\\nhttps://fr.matomo.org/privacy/\\n\\n**Politique de confidentialité et RGPD**\\nLe Muséum national d’Histoire naturelle porte la plus grande attention à vos données à caractère personnel et s’engage à les protéger.\\nIl s'engage à ce que la collecte et le traitement de données à caractère personnel, effectués à partir du présent site, soient conformes à la loi n°78-17 du 6 janvier 1978 modifiée relative à l'informatique, aux fichiers et aux libertés ainsi qu’au Règlement (UE) 2016/679 du Parlement européen et du Conseil du 27 avril 2016 applicable le 25 mai 2018. \\n\\n**Données personnelles**\\nSauf stipulation contraire directement mentionnée, aucune donnée personnelle n’est collectée ni traitée sur le portail « Compteur de biodiversité outre-mer ».\",\"multiLine\":true,\"optional\":false}]}]}]}";
        server.enqueue(
            new MockResponse()
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(body)
        );
        var response = service.getPageDataFromImportDataSource("legal-terms", "mentions-legales").block();
        RecordedRequest recordedRequest = server.takeRequest();
        assertThat(response).isEqualTo(body);
        assertThat(recordedRequest.getHeader("Authorization")).isEqualTo("Key token");
        assertThat(recordedRequest.getMethod()).isEqualTo("GET");
        assertThat(recordedRequest.getPath()).isEqualTo("/api/pages/legal-terms/mentions-legales");
    }

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
                .list(MultiListElement.builder("articleElements")
                    .describedAs("Article elements")
                    .template(
                        SectionElement.builder("imageSection")
                                      .describedAs("Image section")
                                      .multiSizeImage("image", "Image")
                    )
                    .template(
                        SectionElement.builder("paragraphSection")
                                    .describedAs("Paragraph section")
                                    .text("paragraph", "Paragraph")
                    )
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
            Element.text(8L, "carousel.1.legend", "Image 1"),
            Element.text(9L, "articleElements.0.paragraphSection.paragraph", "Paragraph 1"),
            Element.image(10L, "articleElements.1.imageSection.image", 4L, "Image 2", true)
        ));

        PageContent result = service.buildPageContent(pageModel, page);

        assertThat(result.getId()).isEqualTo(page.getId());
        assertThat(result.getName()).isEqualTo(page.getName());
        assertThat(result.getModelName()).isEqualTo(page.getModelName());
        assertThat(result.getTitle()).isEqualTo(page.getTitle());

        assertThat(result.getContent()).isEqualTo(
            Map.of(
                "title", Element.text(1L, "title", "Diversity"),
                "welcome", Map.of(
                    "welcomeImage",
                    Element.image(2L, "welcome.welcomeImage", 1L, "Welcome image", true),
                    "welcomeLegend", Element.text(3L, "welcome.welcomeLegend", "Welcome here"),
                    "tourismLink",
                    Element.link(4L, "welcome.tourismLink", "Click here", "https://tourism.fr")
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
                ),
                "articleElements", List.of(
                    Map.of(
                        "template", "paragraphSection",
                        "content", Map.of(
                            "paragraph", Element.text(9L, "articleElements.0.paragraphSection.paragraph", "Paragraph 1")
                        )
                    ),
                    Map.of(
                        "template", "imageSection",
                        "content", Map.of(
                            "image", Element.image(10L, "articleElements.1.imageSection.image", 4L, "Image 2", true)
                        )
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

        @Test
        void shouldValidateCorrectPage() {
            Page correctPage = new Page(1L, "Home", "home", "Welcome", List.of(
                Element.text(1L, "title", "Title"),
                Element.text(2L, "presentation.text", "Text"),
                Element.image(5L, "carousel.0.image", 2L, "0"),
                Element.text(6L, "carousel.0.legend", "Image 0")
            ));
            assertThatCode(() -> service.validatePageContent(pageModel, correctPage))
                .doesNotThrowAnyException();
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
