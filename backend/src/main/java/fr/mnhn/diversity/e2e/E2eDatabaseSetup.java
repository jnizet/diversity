package fr.mnhn.diversity.e2e;

import static com.ninja_squad.dbsetup.Operations.*;
import static fr.mnhn.diversity.model.ElementType.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.sql.DataSource;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.generator.SequenceValueGenerator;
import com.ninja_squad.dbsetup.generator.ValueGenerator;
import com.ninja_squad.dbsetup.generator.ValueGenerators;
import com.ninja_squad.dbsetup.operation.Operation;
import fr.mnhn.diversity.about.AboutModel;
import fr.mnhn.diversity.act.ActModel;
import fr.mnhn.diversity.admin.security.PasswordHasher;
import fr.mnhn.diversity.ecogesture.EcogestureActSectionModel;
import fr.mnhn.diversity.ecogesture.EcogestureModel;
import fr.mnhn.diversity.home.HomeModel;
import fr.mnhn.diversity.image.ImageType;
import fr.mnhn.diversity.indicator.IndicatorModel;
import fr.mnhn.diversity.legal.LegalTermsModel;
import fr.mnhn.diversity.territory.Territory;
import fr.mnhn.diversity.territory.TerritoryModel;
import fr.mnhn.diversity.territory.Zone;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * A component only used when the app is started with the e2e profile, and which populates the e2e database
 * with known data for e2e tests
 * @author JB Nizet
 */
@Component
@Profile("e2e")
public class E2eDatabaseSetup implements CommandLineRunner {

    private final DataSourceDestination destination;
    private final PasswordHasher passwordHasher;

    public E2eDatabaseSetup(DataSource dataSource, PasswordHasher passwordHasher) {
        this.destination = new DataSourceDestination(dataSource);
        this.passwordHasher = passwordHasher;
    }

    /**
     * Generates 13 ecogestures
     */
    public List<Operation> generateEcogestures(Long firstEcogestureId, ValueGenerator<?> elementIdGenerator) {
        return IntStream.range(0, 13)
                 .mapToObj(
                     (index) ->
                         insertInto("page_element")
                             .withDefaultValue("page_id", firstEcogestureId + index)
                             .withGeneratedValue("id", elementIdGenerator)
                             .columns("type", "key", "text", "image_id", "alt", "href", "title")
                             .values(TEXT, "presentation.name", "Prot??geons les r??cifs coralliens", null, null, null, true)
                             .values(TEXT, "presentation.category", "Loisirs", null, null, null, false)
                             .values(TEXT, "presentation.description", "Sinon ils vont mourir", null, null, null, false)
                             .values(TEXT, "presentation.descriptionTerritories", "Sinon ils vont mourir", null, null, null, false)
                             .values(IMAGE, "presentation.image", null, 30L, "Jolis coraux", null, false)
                             .values(CHECKBOX, "presentation.shadowed", true, null, null, null, false)
                             .values(IMAGE, "presentation.file", null, 31L, "Fiche technique", null, false)
                             .values(TEXT, "understand.title", "Comprendre : un ??cosyst??me tr??s riche", null, null, null, false)
                             .values(TEXT, "understand.text", "Les r??cifs coralliens affichent plus d'un tiers des esp??ces marines connues...", null, null, null, false)
                             .values(TEXT, "understand.quote", "Ces actions conjugu??es entra??nent un accroissement de la biodiversit??...", null, null, null, false)
                             .values(TEXT, "action.title", "Les bons gestes pour prot??ger les r??cifs", null, null, null, false)
                             .values(IMAGE, "action.cards.0.icon", null, 33L, "Cr??me solaire", null, false)
                             .values(TEXT, "action.cards.0.description", "Je choisis une cr??me solaire non nocive pour l'environnement", null, null, null, false)
                             .values(IMAGE, "action.cards.1.icon", null, 34L, "Bateau", null, false)
                             .values(TEXT, "action.cards.1.description", "En bateau, je ne jette pas l'ancre ?? proximit?? de r??cifs", null, null, null, false)
                             .build()
                 ).collect(Collectors.toList());
    }

    @Override
    public void run(String... args) {
        Operation deleteAll =
            deleteAllFrom(
                "indicator_value",
                "indicator_category",
                "indicator_ecogesture",
                "indicator",
                "category",
                "ecogesture",
                "page_element",
                "page",
                "image",
                "app_user"
            );

        Operation categories =
            insertInto("category")
                .columns("id", "name")
                .values(1, "??cosyst??mes")
                .values(2, "Esp??ces menac??es")
                .values(3, "V??g??tation")
                .build();

        Operation indicators =
            insertInto("indicator")
                .columns("id", "biom_id", "slug", "is_rounded", "rank")
                .values(1, "i1", "especes-envahissantes", false, 1)
                .values(2, "i2", "deforestation", false, 2)
                .build();

        Operation indicatorCategories =
            insertInto("indicator_category")
                .columns("indicator_id", "category_id")
                .values(1, 1)
                .values(2, 1)
                .values(2, 3)
                .build();

        Operation indicatorValues =
            insertInto("indicator_value")
                .columns("id", "indicator_id", "territory", "value", "unit")
                .values(11, 1, "OUTRE_MER", 65, null)
                .values(12, 1, "REUNION", 40, null)
                .values(13, 1, "GUADELOUPE", 14, null)
                .values(21, 2, "OUTRE_MER", 5, "%")
                .values(22, 2, "REUNION", 7, "%")
                .values(23, 2, "SAINT_PIERRE_ET_MIQUELON", 3, "%")
                .build();

        Operation ecogestures =
            insertInto("ecogesture")
                .columns("id", "slug")
                .values(1, "recifs")
                .values(2, "recifs1")
                .build();

        Operation indicatorEcogestures =
            insertInto("indicator_ecogesture")
                .columns("indicator_id", "ecogesture_id")
                .values(1, 1)
                .values(2, 1)
                .build();

        Long home = 1L;
        Long about = 2L;
        Long ecogesture1 = 100L;
        Long ecogestureHome = 4L;
        Long act = 5L;
        Long science = 7L;
        Long territoryHome = 8L;
        Long legalTerms = 9L;
        Long reunion = 10L;
        Long stPierreEtMiquelon = 11L;
        Long indicatorHome = 29L;
        Long especesEnvahissantes = 30L;
        Long deforestation = 31L;
        Long ecogestureAct = 32L;
        Long antilles = 41L;
        Operation pages =
            insertInto("page")
                .columns("id", "name", "model_name", "title")
                .values(home, HomeModel.HOME_PAGE_NAME, HomeModel.HOME_PAGE_MODEL.getName(), "Accueil")
                .values(about, AboutModel.ABOUT_PAGE_NAME, AboutModel.ABOUT_PAGE_MODEL.getName(), "?? propos")
                .values(act, ActModel.ACT_PAGE_NAME, ActModel.ACT_PAGE_MODEL.getName(), "Agir ensemble")
                .values(science, ActModel.SCIENCE_PAGE_NAME, ActModel.SCIENCE_PAGE_MODEL.getName(), "Sciences participatives")
                .values(ecogesture1, "recifs", EcogestureModel.ECO_GESTURE_PAGE_MODEL.getName(), "??cogeste: prot??ger les r??cifs")
                .values(101L, "recifs1", EcogestureModel.ECO_GESTURE_PAGE_MODEL.getName(), "??cogeste: prot??ger les r??cifs")
                .values(102L, "recifs2", EcogestureModel.ECO_GESTURE_PAGE_MODEL.getName(), "??cogeste: prot??ger les r??cifs")
                .values(103L, "recifs3", EcogestureModel.ECO_GESTURE_PAGE_MODEL.getName(), "??cogeste: prot??ger les r??cifs")
                .values(104L, "recifs4", EcogestureModel.ECO_GESTURE_PAGE_MODEL.getName(), "??cogeste: prot??ger les r??cifs")
                .values(105L, "recifs5", EcogestureModel.ECO_GESTURE_PAGE_MODEL.getName(), "??cogeste: prot??ger les r??cifs")
                .values(106L, "recifs6", EcogestureModel.ECO_GESTURE_PAGE_MODEL.getName(), "??cogeste: prot??ger les r??cifs")
                .values(107L, "recifs7", EcogestureModel.ECO_GESTURE_PAGE_MODEL.getName(), "??cogeste: prot??ger les r??cifs")
                .values(108L, "recifs8", EcogestureModel.ECO_GESTURE_PAGE_MODEL.getName(), "??cogeste: prot??ger les r??cifs")
                .values(109L, "recifs9", EcogestureModel.ECO_GESTURE_PAGE_MODEL.getName(), "??cogeste: prot??ger les r??cifs")
                .values(110L, "recifs10", EcogestureModel.ECO_GESTURE_PAGE_MODEL.getName(), "??cogeste: prot??ger les r??cifs")
                .values(111L, "recifs11", EcogestureModel.ECO_GESTURE_PAGE_MODEL.getName(), "??cogeste: prot??ger les r??cifs")
                .values(112L, "recifs12", EcogestureModel.ECO_GESTURE_PAGE_MODEL.getName(), "??cogeste: prot??ger les r??cifs")
                .values(ecogestureHome, EcogestureModel.ECO_GESTURE_HOME_PAGE_NAME, EcogestureModel.ECO_GESTURE_HOME_PAGE_MODEL.getName(), "??cogestes")
                .values(territoryHome, TerritoryModel.TERRITORY_HOME_PAGE_NAME, TerritoryModel.TERRITORY_HOME_PAGE_MODEL.getName(), "Territoires")
                .values(reunion, Territory.REUNION.getSlug(), TerritoryModel.TERRITORY_PAGE_MODEL.getName(), "La R??union")
                .values(stPierreEtMiquelon,
                        Territory.SAINT_PIERRE_ET_MIQUELON.getSlug(), TerritoryModel.TERRITORY_PAGE_MODEL.getName(), "Saint Pierre et Miquelon")
                .values(indicatorHome, IndicatorModel.INDICATOR_HOME_PAGE_NAME, IndicatorModel.INDICATOR_HOME_PAGE_MODEL.getName(), "Indicateurs")
                .values(especesEnvahissantes, "especes-envahissantes", IndicatorModel.INDICATOR_PAGE_MODEL.getName(), "Esp??ces envahissantes")
                .values(deforestation, "deforestation", IndicatorModel.INDICATOR_PAGE_MODEL.getName(), "D??forestation")
                .values(legalTerms, LegalTermsModel.LEGAL_TERMS_PAGE_NAME, LegalTermsModel.LEGAL_TERMS_PAGE_MODEL.getName(), "Mentions l??gales")
                .values(ecogestureAct, EcogestureActSectionModel.ECOGESTURE_ACT_SECTION_NAME, EcogestureActSectionModel.ECOGESTURE_ACT_SECTION_MODEL.getName(), "Section Agir pour la science des ecogestes")
                .values(antilles, Zone.ANTILLES.getSlug(), TerritoryModel.ZONE_PAGE_MODEL.getName(), "Le bassin Antillais")
                .build();

        String png = ImageType.PNG.getMediaType().toString();
        String jpg = ImageType.JPG.getMediaType().toString();
        String svg = ImageType.SVG.getMediaType().toString();
        String pdf = ImageType.PDF.getMediaType().toString();
        Operation images =
            insertInto("image")
                .columns("id", "content_type", "original_file_name")
                .values(1L, jpg, "carousel1.jpg")
                .values(2L, jpg, "carousel2.jpg")
                .values(3L, jpg, "carousel3.jpg")
                .values(300L, jpg, "carousel3.jpg")
                .values(4L, png, "apropos.png")
                .values(5L, jpg, "temoignage.png")
                .values(6L, jpg, "science-participative.png")
                .values(7L, jpg, "fond.png")
                .values(8L, svg, "outre-mer.svg")
                .values(9L, jpg, "onb.jpg")
                .values(10L, png, "mnhn.png")
                .values(11L, jpg, "apropos1.jpg")
                .values(12L, jpg, "apropos2.jpg")
                .values(13L, jpg, "apropos3.jpg")
                .values(14L, jpg, "apropos-quote.jpg")
                .values(30L, jpg, "ecogeste_corail.jpg")
                .values(31L, pdf, "fiche-technique.pdf")
                .values(32L, png, "comprendre.png")
                .values(33L, png, "vignette1.png")
                .values(34L, png, "vignette2.png")
                .values(40L, jpg, "indicateurs.jpg")
                .values(91L, jpg, "ecogeste_intro_all.jpg")
                .values(92L, png, "tous_vivants.png")
                .values(101L, jpg, "interest1.jpg")
                .values(102L, jpg, "interest2.jpg")
                .values(103L, jpg, "tortue.jpg")
                .values(104L, jpg, "papangue.jpg")
                .values(105L, png, "other.png")
                .values(106L, png, "reunion.png")
                .values(107L, jpg, "st-pierre-et-miquelon.jpg")
                .values(108L, png, "tourbieres.png")
                .values(109L, png, "ressources-naturelles.png")
                .values(110L, jpg, "comprendre.jpg")
                .values(201L, jpg, "act-background.jpg")
                .values(202L, jpg, "science-project.jpg")
                .values(401, jpg, "science.jpg")
                .values(402, jpg, "r1.jpg")
                .values(403, jpg, "r2.jpg")
                .values(404, jpg, "oiseaux.jpg")
                .values(405, jpg, "oiseau.jpg")
                .build();

        SequenceValueGenerator elementIdGenerator = ValueGenerators.sequence();

        Operation homeElements =
            insertInto("page_element")
                .withDefaultValue("page_id", home)
                .withGeneratedValue("id", elementIdGenerator)
                .columns("type", "key", "text", "image_id", "alt", "href", "title")
                .values(TEXT, "carousel.carouselElements.0.title", "D??couvrez la biodiversit?? des Outre-Mer", null, null, null, false)
                .values(TEXT, "carousel.carouselElements.1.title", "D??couvrez la biodiversit?? des Outre-Mer", null, null, null, false)
                .values(TEXT, "carousel.carouselElements.2.title", "D??couvrez la biodiversit?? des Outre-Mer", null, null, null, false)
                .values(TEXT, "carousel.carouselElements.3.title", "D??couvrez la biodiversit?? des Outre-Mer", null, null, null, false)
                .values(TEXT, "carousel.carouselElements.0.text", "Partez ?? la rencontre des esp??ces et des ??cosyst??mes des territoires fran??ais d'outre-mer", null, null, null, false)
                .values(TEXT, "carousel.carouselElements.1.text", "Partez ?? la rencontre des esp??ces et des ??cosyst??mes des territoires fran??ais d'outre-mer", null, null, null, false)
                .values(TEXT, "carousel.carouselElements.2.text", "Partez ?? la rencontre des esp??ces et des ??cosyst??mes des territoires fran??ais d'outre-mer", null, null, null, false)
                .values(TEXT, "carousel.carouselElements.3.text", "Partez ?? la rencontre des esp??ces et des ??cosyst??mes des territoires fran??ais d'outre-mer", null, null, null, false)
                .values(SELECT, "carousel.select", "option1", null, null, null, false)
                .values(IMAGE, "carousel.carouselElements.0.image", null, 1L, "Chelonia mydas ??  B. Guichard", null, false)
                .values(CHECKBOX, "carousel.carouselElements.0.shadowed", true, null, null, null, false)
                .values(IMAGE, "carousel.carouselElements.1.image", null, 2L, "L??gende n??2", null, false)
                .values(CHECKBOX, "carousel.carouselElements.1.shadowed", true, null, null, null, false)
                .values(IMAGE, "carousel.carouselElements.2.image", null, 3L, "L??gende n??3", null, false)
                .values(CHECKBOX, "carousel.carouselElements.2.shadowed", true, null, null, null, false)
                .values(IMAGE, "carousel.carouselElements.3.image", null, 300L, "L??gende n??4", null, false)
                .values(CHECKBOX, "carousel.carouselElements.3.shadowed", true, null, null, null, false)
                .values(LINK, "carousel.carouselElements.0.button", "D??couvrir les territoires", null, null, null, false)
                .values(LINK, "carousel.carouselElements.1.button", "D??couvrir les territoires", null, null, null, false)
                .values(LINK, "carousel.carouselElements.2.button", "D??couvrir les territoires", null, null, null, false)
                .values(LINK, "carousel.carouselElements.3.button", "D??couvrir les territoires", null, null, null, false)
                .values(TEXT, "presentation.title", "Le compteur de biodiversit?? Outre-Mer", null, null, null, false)
                .values(TEXT, "presentation.text", "Les territoires d'outre-mer pr??sentent une biodiversit?? particuli??rement riche et vari??e, mais fragilis??e par les activit??s humaines. La mission du compteur est de donner une vision d'ensemble des enjeux li??s ?? la biodiversit?? en outre-mer afin que chacun, r??sident ou voyageur de passage, puisse s'informer et s'impliquer ?? son ??chelle pour la pr??servation de cette biodiversit?? exceptionnelle.", null, null, null, false)
                .values(TEXT, "presentation.indicators", "Des chiffres cl??s pour comprendre", null, null, null, false)
                .values(TEXT, "presentation.ecogestures", "Des gestes simples ?? mettre en place au quotidien", null, null, null, false)
                .values(TEXT, "presentation.science", "Participez aux programmes pour faire progresser la connaissance", null, null, null, false)
                .values(TEXT, "presentation.territories", "D??couvrez l'identit?? de chaque territoire d'outre-mer", null, null, null, false)
                .values(TEXT, "presentation.quote", "Les outre-mer regroupent 80 % de la biodiversit?? fran??aise, r??inventons, adaptons notre fa??on de vivre et de voyager", null, null, null, false)
                .values(TEXT, "testimony.title", "Une biodiversit?? unique et fragile, prot??geons-la !", null, null, null, false)
                .values(TEXT, "testimony.text", "La conservation de la biodiversit?? d??pend de sa connaissance et de la compr??hension des ph??nom??nes qui l'impactent. Les indicateurs sont des outils qui permettent de suivre diff??rents param??tres de la biodiversit??, pour renseigner de son ??volution.", null, null, null, false)
                .values(TEXT, "testimony.quote", "Ces actions conjugu??es entra??nent un accroissement de la biodiversit??, une am??lioration de la qualit?? de l'eau, de l'air mais aussi de la qualit?? de vie.", null, null, null, false)
                .values(IMAGE, "testimony.image", null, 5L, "T??moignage", null, false)
                .values(TEXT, "science.title", "La photo-identification de serpents de mer par un groupe de plongeuses amatrices", null, null, null, false)
                .values(LINK, "science.more", "Lire aussi", null, null, "/science-participative", false)
                .values(TEXT, "science.text", "Surnomm??es les ?? grand-m??res fantastiques ??, un groupe de 7 plongeuses entre 60 et 75 ans ont collabor?? avec les chercheurs de l'Universit?? de Nouvelle-Cal??donie pour ??tudier un serpent de mer m??connu, l'Hydrophis major, dans la baie des Citrons, au sud de Noum??a. Leurs photographies tr??s r??guli??res ont permis de recenser 250 individus diff??rents de ce serpent dont on pensait la pr??sence anecdotique.", null, null, null, false)
                .values(IMAGE, "science.image", null, 6L, "Science participative", null, false)
                .build();

        Operation aboutElements =
            insertInto("page_element")
                .withDefaultValue("page_id", about)
                .withGeneratedValue("id", elementIdGenerator)
                .columns("type", "key", "text", "image_id", "alt", "href", "title")
                .values(TEXT, "header.title", "Pourquoi un compteur de la biodiversit?? en outre-mer ?", null, null, null, true)
                .values(TEXT, "header.subtitle", "La mission du compteur est de donner une vision d???ensemble des enjeux li??s ?? la biodiversit?? en outre-mer, et s???articule autour de trois objectifs : ", null, null, null, false)
                .values(IMAGE, "header.background", null, 7, "L??gende", null, false)
                .values(CHECKBOX, "header.shadowed", true, null, null, null, false)
                .values(TEXT, "goal1.title", "Partager la connaissance scientifique", null, null, null, true)
                .values(TEXT, "goal1.description", "Quelles sont les esp??ces pr??sentes dans ce territoire ? Comment ??voluent leurs populations ? Quel est l?????tat de sant?? des r??cifs coralliens ? Pour r??pondre ?? ces questions et bien d???autres, diff??rents param??tres, appel??s ?? indicateurs ??, sont mesur??s par les scientifiques pour suivre l?????volution de l?????tat de la biodiversit??. Le compteur a pour mission de rendre compte de l?????tat de ces connaissances dans tous les territoires.", null, null, null, false)
                .values(IMAGE, "goal1.image", null, 11, "L??gende", null, false)
                .values(TEXT, "goal2.title", "Valoriser les actions des territoires", null, null, null, true)
                .values(TEXT, "goal2.description", "De nombreuses actions locales sont mises en place afin de tenter de pr??server les ??cosyst??mes et les esp??ces. Donner de la visibilit?? aux acteurs locaux, partager les exp??riences et les r??ussites, au b??n??fice de tous, c???est ??galement l???un des objectifs du compteur.", null, null, null, false)
                .values(IMAGE, "goal2.image", null, 12, "L??gende", null, false)
                .values(TEXT, "goal2.quote", "Les territoires d???outre-mer pr??sentent une biodiversit?? particuli??rement riche et vari??e, mais fragilis??e par les activit??s humaines.", null, null, null, false)
                .values(IMAGE, "goal2.quoteImage", null, 14, "L??gende", null, false)
                .values(TEXT, "goal3.title", "Encourager chacun ?? agir", null, null, null, true)
                .values(TEXT, "goal3.description", "Chacun, ?? son ??chelle, peut agir pour pr??server la biodiversit??. Connaitre et appliquer des gestes simples, modifier ses habitudes et son comportement ou encore s???investir dans desprogrammes de sciences participatives, tout le monde est concern?? par la pr??servation de la biodiversit??. Le r??le du compteur est d???accompagner les citoyens en partageant desinformations claires et adapt??es ?? tous les territoires, et en encourageant ?? se mobiliser.", null, null, null, false)
                .values(IMAGE, "goal3.image", null, 13, "L??gende", null, false)
                .values(TEXT, "partners.title", "Ils contribuent au compteur de la biodiversit??", null, null, null, false)
                .values(IMAGE, "partners.partners.0.logo", null, 8, "Logo1", null, false)
                .values(TEXT, "partners.partners.0.url", "http://google.com", null, null, null, false)
                .values(IMAGE, "partners.partners.1.logo", null, 9, "Logo2", null, false)
                .values(TEXT, "partners.partners.1.url", "http://google.com", null, null, null, false)
                .values(IMAGE, "partners.partners.2.logo", null, 10, "Logo3", null, false)
                .values(TEXT, "partners.partners.2.url", "http://google.com", null, null, null, false)
                .build();

        Operation actElements =
            insertInto("page_element")
                .withDefaultValue("page_id", act)
                .withGeneratedValue("id", elementIdGenerator)
                .columns("type", "key", "text", "image_id", "alt", "href", "title")
                .values(TEXT, "header.title", "Agir ensemble pour une biodiversit?? unique mais fragile", null, null, null, true)
                .values(TEXT, "header.subtitle", "Les outre-mer abritent une biodiversit?? unique mais fragile : prot??geons-l?? !", null, null, null, false)
                .values(IMAGE, "header.background", null, 201, "L??gende", null, false)
                .values(CHECKBOX, "header.shadowed", true, null, null, null, false)
                .values(TEXT, "ecogestures.title", "D??couvrez les ??cogestes", null, null, null, true)
                .values(TEXT, "ecogestures.subtitle", "D??couvrez les bons gestes recommand??s par le Minist??re de l???outre-mer pour une exp??rience responsable", null, null, null, false)
                .values(TEXT, "science.title", "Les sciences participatives", null, null, null, true)
                .values(TEXT, "science.subtitle", "Prot??gez la biodiversit?? ultra-marine ?? travers les ciences participatives", null, null, null, false)
                .values(TEXT, "science.project.title", "Un projet original en Nouvelle-Cal??donie", null, null, null, false)
                .values(TEXT, "science.project.description", "Un groupe de 7 plongeuses entre 60 et 75 ans ont collabor?? avec les chercheurs de l???Universit?? de Nouvelle-Cal??donie pour ??tudier un serpent de mer m??connu, l???Hydrophis major, dans la baie des Citrons, au sud de Noum??a.", null, null, null, false)
                .values(IMAGE, "science.project.image", null, 202, "L??gende", null, false)
                .build();

        Operation scienceElements =
            insertInto("page_element")
                .withDefaultValue("page_id", science)
                .withGeneratedValue("id", elementIdGenerator)
                .columns("type", "key", "text", "image_id", "alt", "href", "title")
                .values(TEXT, "header.title", "Que sont les sciences participatives ?", null, null, null, true)
                .values(TEXT, "header.subtitle", "Les sciences participatives permettent ?? tous les curieux de la nature, du d??butant ?? l???exp??riment??, de contribuer ?? la recherche sur la biodiversit?? en fournissant aux scientifiques un grand nombre de donn??es de terrain.", null, null, null, false)
                .values(IMAGE, "header.background", null, 401, "L??gende", null, false)
                .values(CHECKBOX, "header.shadowed", true, null, null, null, false)
                .values(TEXT, "presentation.title", "Sciences faites par des non-professionnels", null, null, null, false)
                .values(TEXT, "presentation.description", "Au-del?? de la simple d??finition ?? sciences faites par des non-professionnels ??, l???Institut de formation et de recherche en ??ducation ?? l???environnement (Ifr??e) distingue 3 cat??gories de programmes participatifs selon leurs objectifs, permettant de mieux comprendre ce que sont r??ellement les Sciences Participatives.", null, null, null, false)
                .values(TEXT, "paragraphs.0.title", "Les bases de donn??es collaboratives", null, null, null, false)
                .values(TEXT, "paragraphs.0.text", "Il s???agit de projet d???inventaires et de signalements destin??s aux amateurs sachant identifier les taxons observ??s. Les d??butants y sont les bienvenus. L???objet principal est de partager de la connaissance, ??ventuellement mobilisable a posteriori dans unprojet de recherche ou dans un objectif de conservation.", null, null, null, false)
                .values(TEXT, "paragraphs.1.title", "Les projets adoss??s ?? des programmes de recherche", null, null, null, false)
                .values(TEXT, "paragraphs.1.text", "L???objet et les conditions de collecte des donn??es sont alors tr??s pr??cis??ment d??finis, garantissant ainsi l???exploitabilit?? de ces derni??res. La nature du protocole va contraindre le type de public participant, mais cela peut souvent inclure des d??butants. Les donn??es collect??es peuvent ??tre ensuite vers??es dans lesprojets de bases de donn??es collaboratives.", null, null, null, false)
                .values(TEXT, "paragraphs.2.title", "Les programmes ?? vis??e ??ducative ou de gestion/conservation", null, null, null, false)
                .values(TEXT, "paragraphs.2.text", "On trouve l?? divers programmes de sensibilisation ?? la d??marche scientifique ou aux enjeux de conservation. Certains d???entre eux n???impliquent pas de collecte d???information centralis??e, leur objectif ??tant d???abord de permettre aux participants d???acqu??rir des connaissances.", null, null, null, false)
                .values(IMAGE, "images.0.image", null, 402, "L??gende", null, false)
                .values(IMAGE, "images.1.image", null, 403, "L??gende", null, false)
                .values(TEXT, "examples.title", "Deux exemples de programmes participatifs en Outre-mer", null, null, null, false)
                .values(TEXT, "examples.projects.0.title", "Un projet original en Nouvelle-Cal??donie", null, null, null, false)
                .values(TEXT, "examples.projects.0.description", "Un groupe de 7 plongeuses entre 60 et 75 ans ont collabor?? avec les chercheurs de l???Universit?? de Nouvelle-Cal??donie pour ??tudier un serpent de mer m??connu, l???Hydrophis major, dans la baie des Citrons, au sud de Noum??a.", null, null, null, false)
                .values(LINK, "examples.projects.0.more", "En savoir plus", null, null, "https://google.com", false)
                .values(TEXT, "examples.projects.0.subject", "Suivi des populations d???oiseaux communs par ??chantillonnages ponctuels simples le long de parcours pr??d??finis", null, null, null, false)
                .values(TEXT, "examples.projects.0.actor", "Groupe d???Etude et de Protection des Oiseaux de Guyane (GEPOG)", null, null, null, false)
                .values(TEXT, "examples.projects.0.target", "Ornithologues ?? titre b??n??vole ou dans un cadre professionnels (Agents de zones prot??g??es)", null, null, null, false)
                .values(IMAGE, "examples.projects.0.image", null, 404, "L??gende", null, false)
                .values(TEXT, "examples.projects.1.title", "Autre titre", null, null, null, false)
                .values(TEXT, "examples.projects.1.description", "Un groupe de 7 plongeuses entre 60 et 75 ans ont collabor?? avec les chercheurs de l???Universit?? de Nouvelle-Cal??donie pour ??tudier un serpent de mer m??connu, l???Hydrophis major, dans la baie des Citrons, au sud de Noum??a.", null, null, null, false)
                .values(LINK, "examples.projects.1.more", "En savoir plus", null, null, "https://google.com", false)
                .values(TEXT, "examples.projects.1.subject", "Suivi des populations d???oiseaux communs par ??chantillonnages ponctuels simples le long de parcours pr??d??finis", null, null, null, false)
                .values(TEXT, "examples.projects.1.actor", "Groupe d???Etude et de Protection des Oiseaux de Guyane (GEPOG)", null, null, null, false)
                .values(TEXT, "examples.projects.1.target", "Ornithologues ?? titre b??n??vole ou dans un cadre professionnels (Agents de zones prot??g??es)", null, null, null, false)
                .values(IMAGE, "examples.projects.1.image", null, 405, "L??gende", null, false)
                .values(TEXT, "application.title", "Agir pour la science depuis son smartphone, c'est possible !", null, null, null, false)
                .values(TEXT, "application.subtitle", "J???agis en ligne", null, null, null, false)
                .values(LINK, "application.downloadLink", "Je t??l??charge l'application INPN Espaces", null, null, "https://google.com", false)
                .build();

        Operation ecogestureHomeElements =
            insertInto("page_element")
                .withDefaultValue("page_id", ecogestureHome)
                .withGeneratedValue("id", elementIdGenerator)
                .columns("type", "key", "text", "image_id", "alt", "href", "title")
                .values(TEXT, "title", "R??inventons notre fa??on de vivre et de voyager gr??ce aux ??cogestes", null, null, null, true)
                .values(TEXT, "presentation", "Lorem ipsum dolor", null, null, null, false)
                .values(IMAGE, "image", null, 91L, "Ecogestes", null, false)
                .values(CHECKBOX, "shadowed", true, null, null, null, false)
                .values(TEXT, "question", "Qu'est-ce qu'un ??cogeste ?", null, null, null, false)
                .values(TEXT, "answer", "Les territoires d???outre-mer pr??sentent une biodiversit?? particuli??rement riche et vari??e, mais fragilis??e par les activit??s humaines. Les territoires d???outre-mer pr??sentent une biodiversit?? particuli??rement riche et vari??e, mais fragilis??e par les activit??s humaines. Les territoires d'outre-mer pr??sentent une biodiversit?? particuli??rement riche.", null, null, null, false)
                .values(TEXT, "quote", "Des actions concr??tes pour agir d??s maintenant et nous donner la possibilit?? de pr??server cette biodiversit?? riche et fragile", null, null, null, false)
                .values(IMAGE, "other.image", null, 92L, "Biodiversit??. Tous vivants !", null, false)
                .values(TEXT, "other.title", "Retrouvez d???autres ??cogestes sur", null, null, null, false)
                .values(TEXT, "other.text", "Biodiversit??. Tous vivants !", null, null, null, false)
                .build();

        Operation ecogestureActSectionElements =
            insertInto("page_element")
                .withDefaultValue("page_id", ecogestureAct)
                .withGeneratedValue("id", elementIdGenerator)
                .columns("type", "key", "text", "image_id", "alt", "href", "title")
                .values(TEXT, "title", "Agir pour la science", null, null, null, true)
                .values(TEXT, "description", "La majorit?? des territoires d???outre-mer fran??ais sont situ??s dans des r??gions particuli??rement riches en esp??ces, notamment en esp??ces end??miques, ce qu???on appelle des points chauds de la biodiversit??", null, null, null, false)
                .values(IMAGE, "image", null, 91L, "Ecogestes", null, false)
                .values(CHECKBOX, "shadowed", true, null, null, null, false)
                .values(TEXT, "firstActionName", "J???agis en ligne", null, null, null, false)
                .values(LINK, "firstActionLink", "Je t??l??charge l???application INPN Espaces", null, null, "https://google.com", false)
                .values(TEXT, "secondActionName", "Je donne de mon temps", null, null, null, false)
                .values(LINK, "secondActionLink", "Tous les programmes de sciences participatives", null, null, "/sciences-participatives", false)
                .build();

        Operation territoryHomeElements =
            insertInto("page_element")
                .withDefaultValue("page_id", territoryHome)
                .withGeneratedValue("id", elementIdGenerator)
                .columns("type", "key", "text", "image_id", "alt", "href", "title")
                .values(TEXT, "header.title", "A la d??couverte des Outre-mer", null, null, null, true)
                .values(TEXT, "header.text", "Les territoires d???outre-mer pr??sentent une biodiversit?? particuli??rement riche et vari??e, mais fragilis??e par les activit??s humaines. Naviguez parmi les portraits biodiversit?? des diff??rents territoires et d??couvrez l???histoire des esp??ces et des ??cosyst??mes qui les peuplent, les enjeux de chaque territoire et les initiatives des acteurs locaux pour les pr??server.", null, null, null, false)
                .values(TEXT, "header.population", "6 274", null, null, null, false)
                .values(TEXT, "header.species", "85 117", null, null, null, false)
                .build();

        Operation reunionElements =
            insertInto("page_element")
                .withDefaultValue("page_id", reunion)
                .withGeneratedValue("id", elementIdGenerator)
                .columns("type", "key", "text", "image_id", "alt", "href", "title")
                .values(TEXT, "zoom.title", "La R??union, entre mer et montagnes", null, null, null, true)
                .values(TEXT, "zoom.callToAction", "La R??union, entre mer et montagnes", null, null, null, true)
                .values(IMAGE, "zoom.map", null, 106, "Infographie R??union", null, false)
                .values(TEXT, "identity.title", "La R??union, entre mer et montagnes", null, null, null, true)
                .values(TEXT, "identity.subtitle", "Un climat tropical", null, null, null, true)
                .values(TEXT, "identity.presentation", "?? l'ouest de l'Oc??an Indien...", null, null, null, false)
                .values(IMAGE, "identity.image", null, 106, "Infographie R??union", null, false)
                .values(CHECKBOX, "identity.shadowed", true, null, null, null, false)
                .values(TEXT, "identity.area", "2???512", null, null, null, false)
                .values(TEXT, "identity.marineArea", "???", null, null, null, false)
                .values(TEXT, "identity.population", "859???959", null, null, null, false)
                .values(TEXT, "identity.populationYear", "2017", null, null, null, false)
                .values(TEXT, "identity.species", "???", null, null, null, false)
                .values(TEXT, "identity.highestPoint", "???", null, null, null, false)
                .values(TEXT, "identity.highestPointName", "???", null, null, null, false)
                .values(TEXT, "statistics.0.number", "89???%", null, null, null, false)
                .values(TEXT, "statistics.0.text", "du territoire class?? zone naturelle d'int??r??t ??cologique faunistique et floristique", null, null, null, false)
                .values(TEXT, "statistics.1.number", "90???%", null, null, null, false)
                .values(TEXT, "statistics.1.text", "du territoire class?? pittoresque", null, null, null, false)
                .values(TEXT, "interests.title", "?? la d??couverte d''endroits embl??matiques", null, null, null, false)
                .values(IMAGE, "interests.locations.0.image", null, 101, "1", null, false)
                .values(TEXT, "interests.locations.0.name", "Le grand barachois", null, null, null, false)
                .values(TEXT, "interests.locations.0.description", "Cette lagune de 1000 ha est bord??e de marais et communique avec la mer par un goulet au sud-est", null, null, null, false)
                .values(IMAGE, "interests.locations.1.image", null, 102, "2", null, false)
                .values(TEXT, "interests.locations.1.name", "Le petit barachois", null, null, null, false)
                .values(TEXT, "interests.locations.1.description", "Le plan d'eau et les marais qui le bordent sont d'une importante richesse tant faunistique que floristique", null, null, null, false)
                .values(TEXT, "species.0.name", "Tortue", null, null, null, false)
                .values(TEXT, "species.0.description", "Oh la belle tortue! Comme elle est gracieuse!", null, null, null, false)
                .values(IMAGE, "species.0.image", null, 103, "Tortue", null, false)
                .values(TEXT, "species.1.name", "Papangue", null, null, null, false)
                .values(TEXT, "species.1.description", "Dernier rapace de la R??union", null, null, null, false)
                .values(IMAGE, "species.1.image", null, 104, "Papangue", null, false)
                .values(TEXT, "events.0.date", "1535", null, null, null, false)
                .values(TEXT, "events.0.description", "Prise de possession fran??aise des ??les par Jacques Cartier", null, null, null, false)
                .values(TEXT, "events.1.date", "17??me si??cle", null, null, null, false)
                .values(TEXT, "events.1.description", "Premi??re installation permanente des p??cheurs bretons, normands et basques", null, null, null, false)
                .values(TEXT, "events.2.date", "18??me si??cle", null, null, null, false)
                .values(TEXT, "events.2.description", "Fermeture naturelle de l???isthme", null, null, null, false)
                .values(TEXT, "events.3.date", "1866", null, null, null, false)
                .values(TEXT, "events.3.description", "Premi??re ??tude publi??e sur la flore de Saint-Pierre et Miquelon", null, null, null, false)
                .values(TEXT, "events.4.date", "1889", null, null, null, false)
                .values(TEXT, "events.4.description", "Introduction volontaire du Li??vre d???Am??rique pour la chasse", null, null, null, false)
                .values(TEXT, "events.5.date", "1985", null, null, null, false)
                .values(TEXT, "events.5.description", "disparition de l???archipel du Lagop??de des saules, oiseau caract??ristique des for??ts bor??ales", null, null, null, false)
                .values(TEXT, "events.6.date", "2007", null, null, null, false)
                .values(TEXT, "events.6.description", "Cr??ation du Conseil Scientifique Territorial du Patrimoine Naturel (CSTPN) et projet de r??serve naturelle nationale duGrand Colombier", null, null, null, false)
                .values(TEXT, "ecosystems.ecosystems.0.name", "Les marais tourbeux et les tourbi??res", null, null, null, false)
                .values(TEXT, "ecosystems.ecosystems.0.description", "Ces zones humides occupent des superficies importantes. Caract??ris??es par la formation et l???accumulation de tourbe, parfois sur plusieurs m??tres d?????paisseur, elles pr??sentent des groupements v??g??taux remarquables et vari??s, parmi lesquelles on trouve des sphaignes, des mousses, ainsi que des orchid??es.", null, null, null, false)
                .values(IMAGE, "ecosystems.ecosystems.0.image", null, 12, "Tourbi??res", null, false)
                .values(TEXT, "ecosystems.ecosystems.1.name", "Second ??cosyst??me", null, null, null, false)
                .values(TEXT, "ecosystems.ecosystems.1.description", "Bla bla bla...", null, null, null, false)
                .values(IMAGE, "ecosystems.ecosystems.1.image", null, 104, "Bla bla", null, false)
                .values(IMAGE, "ecosystems.image", null, 3, "Bla bla", null, false)
                .values(TEXT, "risks.title", "Enjeux et menaces", null, null, null, false)
                .values(TEXT, "risks.risks.0.name", "Les ressources naturelles : un enjeu important", null, null, null, false)
                .values(TEXT, "risks.risks.0.description", "Le territoire est pour le moment le seul d???outre-mer fran??ais ?? ne disposer d???aucun dispositif de protection r??glementaire. Les esp??ces exotiques envahissantes, en particulier les mammif??res terrestres qui d??gradent la for??t mais ??galement des esp??ces v??g??tales telles que les Renou??es ou des esp??ces marines comme le Crabe vert, ont des impacts potentiellement pr??occupants.  La chasse, bien que permettant la r??gulation des mammif??res introduits, est une question d??licate, et freine l???augmentation des populations de plusieurs esp??ces d???oiseaux dont l???Eider ?? duvet. L?????conomie de l?????le a ??t?? victime de la surp??che et de l???effondrement des populations de morue de Terre Neuve. Un projet de cr??ation de la r??serve naturelle des Grand et Petit Colombier a ??t?? lanc?? en 2007, permettant de pr??server ce site de nidification remarquable. Malgr?? un avis favorable du Conseil national de protection de la nature, ce projet a ??t?? mal per??u par les populations locales craignant des contraintes sur leurs activit??s traditionnelles, c???est pourquoi il n???a pour le moment pas abouti. ", null, null, null, false)
                .build();

        Operation stPierreEtMiquelonElements =
            insertInto("page_element")
                .withDefaultValue("page_id", stPierreEtMiquelon)
                .withGeneratedValue("id", elementIdGenerator)
                .columns("type", "key", "text", "image_id", "alt", "href", "title")
                .values(TEXT, "zoom.title", "La R??union, entre mer et montagnes", null, null, null, true)
                .values(TEXT, "zoom.callToAction", "La R??union, entre mer et montagnes", null, null, null, true)
                .values(IMAGE, "zoom.map", null, 106, "Infographie R??union", null, false)
                .values(TEXT, "identity.title", "St-Pierre-et-Miquelon, des eaux tr??s riches en biodiversit??", null, null, null, true)
                .values(TEXT, "identity.subtitle", "Un climat subarctique, froid et humide", null, null, null, true)
                .values(TEXT, "identity.presentation", "Au sud de Terre Neuve (Canada) cet archipel est constitu?? de 3 ??les principales : Saint-Pierre, Langlade, Miquelon. Il se situe ?? la confluence du courant froid du Labrador et des eaux plus chaudes du Gulf Stream, offrant des eaux riches en biodiversit??. La proximit?? de l???oc??an influence les ??cosyst??mes terrestres : dunes, prairies c??ti??res, lagunes, marais. On y trouve ??galement la seule for??t bor??ale fran??aise.", null, null, null, false)
                .values(IMAGE, "identity.image", null, 107, "Infographie Saint-Pierre-et-Miquelon", null, false)
                .values(CHECKBOX, "identity.shadowed", true, null, null, null, false)
                .values(TEXT, "identity.area", "242", null, null, null, false)
                .values(TEXT, "identity.marineArea", "12???300", null, null, null, false)
                .values(TEXT, "identity.population", "6???274", null, null, null, false)
                .values(TEXT, "identity.populationYear", "2017", null, null, null, false)
                .values(TEXT, "identity.species", "85???117", null, null, null, false)
                .values(TEXT, "identity.highestPoint", "240", null, null, null, false)
                .values(TEXT, "identity.highestPointName", "Morne de la Grande Montagne", null, null, null, false)
                .values(TEXT, "statistics.0.number", "89???%", null, null, null, false)
                .values(TEXT, "statistics.0.text", "du territoire class?? zone naturelle d'int??r??t ??cologique faunistique et floristique", null, null, null, false)
                .values(TEXT, "statistics.1.number", "90???%", null, null, null, false)
                .values(TEXT, "statistics.1.text", "du territoire class?? pittoresque", null, null, null, false)
                .values(TEXT, "interests.title", "?? la d??couverte d''endroits embl??matiques", null, null, null, false)
                .values(IMAGE, "interests.locations.0.image", null, 101, "1", null, false)
                .values(TEXT, "interests.locations.0.name", "Le grand barachois", null, null, null, false)
                .values(TEXT, "interests.locations.0.description", "Cette lagune de 1000 ha est bord??e de marais et communiqueavec la mer par un goulet au sud-est", null, null, null, false)
                .values(IMAGE, "interests.locations.1.image", null, 102, "2", null, false)
                .values(TEXT, "interests.locations.1.name", "Le petit barachois", null, null, null, false)
                .values(TEXT, "interests.locations.1.description", "Le plan d'eau et les marais qui le bordentsont d'une importante richesse tant faunistique que floristique", null, null, null, false)
                .values(TEXT, "species.0.name", "Tortue", null, null, null, false)
                .values(TEXT, "species.0.description", "Oh la belle tortue! Comme elle est gracieuse!", null, null, null, false)
                .values(IMAGE, "species.0.image", null, 103, "Tortue", null, false)
                .values(TEXT, "species.1.name", "Papangue", null, null, null, false)
                .values(TEXT, "species.1.description", "Dernier rapace de la R??union", null, null, null, false)
                .values(IMAGE, "species.1.image", null, 104, "Papangue", null, false)
                .values(TEXT, "events.0.date", "1535", null, null, null, false)
                .values(TEXT, "events.0.description", "Prise de possession fran??aise des ??les par Jacques Cartier", null, null, null, false)
                .values(TEXT, "events.1.date", "17??me si??cle", null, null, null, false)
                .values(TEXT, "events.1.description", "Premi??re installation permanente des p??cheurs bretons, normands et basques", null, null, null, false)
                .values(TEXT, "events.2.date", "18??me si??cle", null, null, null, false)
                .values(TEXT, "events.2.description", "Fermeture naturelle de l???isthme", null, null, null, false)
                .values(TEXT, "events.3.date", "1866", null, null, null, false)
                .values(TEXT, "events.3.description", "Premi??re ??tude publi??e sur la flore de Saint-Pierre et Miquelon", null, null, null, false)
                .values(TEXT, "events.4.date", "1889", null, null, null, false)
                .values(TEXT, "events.4.description", "Introduction volontaire du Li??vre d???Am??rique pour la chasse", null, null, null, false)
                .values(TEXT, "events.5.date", "1985", null, null, null, false)
                .values(TEXT, "events.5.description", "disparition de l???archipel du Lagop??de des saules, oiseau caract??ristique des for??ts bor??ales", null, null, null, false)
                .values(TEXT, "events.6.date", "2007", null, null, null, false)
                .values(TEXT, "events.6.description", "Cr??ation du Conseil Scientifique Territorial du Patrimoine Naturel (CSTPN) et projet de r??serve naturelle nationale duGrand Colombier", null, null, null, false)
                .values(TEXT, "ecosystems.ecosystems.0.name", "Les marais tourbeux et les tourbi??res", null, null, null, false)
                .values(TEXT, "ecosystems.ecosystems.0.description", "Ces zones humides occupent des superficies importantes. Caract??ris??es par la formation et l???accumulation de tourbe, parfois sur plusieurs m??tres d?????paisseur, elles pr??sentent des groupements v??g??taux remarquables et vari??s, parmi lesquelles on trouve des sphaignes, des mousses, ainsi que des orchid??es.", null, null, null, false)
                .values(IMAGE, "ecosystems.ecosystems.0.image", null, 12, "Tourbi??res", null, false)
                .values(TEXT, "ecosystems.ecosystems.1.name", "Second ??cosyst??me", null, null, null, false)
                .values(TEXT, "ecosystems.ecosystems.1.description", "Bla bla bla...", null, null, null, false)
                .values(IMAGE, "ecosystems.ecosystems.1.image", null, 104, "Bla bla", null, false)
                .values(IMAGE, "ecosystems.image", null, 3, "Bla bla", null, false)
                .values(TEXT, "risks.title", "Enjeux et menaces", null, null, null, false)
                .values(TEXT, "risks.risks.0.name", "Les ressources naturelles : un enjeu important", null, null, null, false)
                .values(TEXT, "risks.risks.0.description", "Le territoire est pour le moment le seul d???outre-mer fran??ais ?? ne disposer d???aucun dispositif de protection r??glementaire. Les esp??ces exotiques envahissantes, en particulier les mammif??res terrestres qui d??gradent la for??t mais ??galement des esp??ces v??g??tales telles que les Renou??es ou des esp??ces marines comme le Crabe vert, ont des impacts potentiellement pr??occupants. La chasse, bien que permettant la r??gulation des mammif??res introduits, est une question d??licate, et freine l???augmentation des populations de plusieurs esp??ces d???oiseaux dont l???Eider ?? duvet. L?????conomie de l?????le a ??t?? victime de la surp??che et de l???effondrement des populations de morue de Terre Neuve. Un projet de cr??ation de la r??serve naturelle des Grand et Petit Colombier a ??t?? lanc?? en 2007, permettant de pr??server ce site de nidification remarquable. Malgr?? un avis favorable du Conseil national de protection de la nature, ce projet a ??t?? mal per??u par les populations locales craignant des contraintes sur leurs activit??s traditionnelles, c???est pourquoi il n???a pour le moment pas abouti. ", null, null, null, false)
                .values(TEXT, "risks.risks.1.name", "Les ressources naturelles : un enjeu important", null, null, null, false)
                .values(TEXT, "risks.risks.1.description", "Le territoire est pour le moment le seul d???outre-mer fran??ais ?? ne disposer d???aucun dispositif de protection r??glementaire. Les esp??ces exotiques envahissantes, en particulier les mammif??res terrestres qui d??gradent la for??t mais ??galement des esp??ces v??g??tales telles que les Renou??es ou des esp??ces marines comme le Crabe vert, ont des impacts potentiellement pr??occupants. La chasse, bien que permettant la r??gulation des mammif??res introduits, est une question d??licate, et freine l???augmentation des populations de plusieurs esp??ces d???oiseaux dont l???Eider ?? duvet. L?????conomie de l?????le a ??t?? victime de la surp??che et de l???effondrement des populations de morue de Terre Neuve. Un projet de cr??ation de la r??serve naturelle des Grand et Petit Colombier a ??t?? lanc?? en 2007, permettant de pr??server ce site de nidification remarquable. Malgr?? un avis favorable du Conseil national de protection de la nature, ce projet a ??t?? mal per??u par les populations locales craignant des contraintes sur leurs activit??s traditionnelles, c???est pourquoi il n???a pour le moment pas abouti. ", null, null, null, false)
                .build();

        Operation indicatorHomeElements =
            insertInto("page_element")
                .withDefaultValue("page_id", indicatorHome)
                .withGeneratedValue("id", elementIdGenerator)
                .columns("type", "key", "text", "image_id", "alt", "href", "title")
                .values(TEXT, "title", "Compter la biodiversit??, oui mais comment ?", null, null, null, true)
                .values(TEXT, "presentation", "Les indicateurs sont des outils qui permettent de suivre diff??rents param??tres de la biodiversit??, pour renseigner de son ??volution. De nombreux param??tres sont ??tudi??s : effectifs des populations, ??tat de sant?? des ??cosyst??mes, taux de pollutions, etc. Ils renseignent ainsi l'??tat de la biodiversit?? ?? un instant pr??cis, mais ??galement l'??tat des connaissances et des moyens scientifiques actuels.", null, null, null, false)
                .values(IMAGE, "image", null, 40, "Chelonia mydas ?? ??B. Guichard", null, false)
                .values(CHECKBOX, "shadowed", true, null, null, null, false)
                .values(TEXT, "understand.title", "Les indicateurs, des outils pour ??valuer la biodiversit??", null, null, null, false)
                .values(TEXT, "understand.description", "La conservation de la biodiversit?? d??pend de sa connaissance et de la compr??hension des ph??nom??nes qui l'impactent. Les indicateurs sont des outils qui permettent de suivre diff??rents param??tres de la biodiversit??, pour renseigner de son ??volution. De nombreux param??tres sont ??tudi??s : effectifs des populations, ??tat de sant?? des ??cosyst??mes, taux de pollutions, etc. Cependant, ils connaissent des limites par manque de donn??es disponibles, manque de moyens de recherche ou parce que certains param??tres sont tr??s complexes ?? ??valuer. Ils renseignent ainsi l'??tat de la biodiversit?? ?? un instant pr??cis, mais ??galement l'??tat des connaissances et des moyens scientifiques actuels.", null, null, null, false)
                .values(IMAGE, "understand.image", null, 5, "Chelonia mydas ?? ??B. Guichard", null, false)
                .values(LINK, "understand.article", "Lien article", null, null, "/media/article/toto", false)
                .values(TEXT, "onb.title", "L'observatoire national de la biodiversit??", null, null, null, false)
                .values(TEXT, "onb.description", "Depuis 2012, l'Observatoire national de la biodiversit??, actuellement pilot?? par l'Office fran??ais pour la biodiversit?? (OFB), publie des indicateurs sur l'??tat de la biodiversit?? fran??aise, avec la contribution de nombreux partenaires. Ils sont r??guli??rement actualis??s et enrichis. Le Compteur met en lumi??re les indicateurs de l'ONB qui concernent les outre-mer et contribue ?? la cr??ation de nouveaux indicateurs pour ces territoires.", null, null, null, false)
                .values(LINK, "onb.link", "Retrouvez les indicateurs sur le site de l'ONB", null, null, "http://indicateurs-biodiversite.naturefrance.fr", false)
                .values(TEXT, "inpn.title", "L'inventaire national du patrimoine naturel", null, null, null, false)
                .values(TEXT, "inpn.description", "Depuis 2012, l'inventaire national de la biodiversit??, actuellement pilot?? par l'Office fran??ais pour la biodiversit?? (OFB), publie des indicateurs sur l'??tat de la biodiversit?? fran??aise, avec la contribution de nombreux partenaires. Ils sont r??guli??rement actualis??s et enrichis. Le Compteur met en lumi??re les indicateurs de l'ONB qui concernent les outre-mer et contribue ?? la cr??ation de nouveaux indicateurs pour ces territoires.", null, null, null, false)
                .values(LINK, "inpn.link", "Retrouvez les indicateurs sur le site de l'inpn", null, null, "http://indicateurs-biodiversite.naturefrance.fr", false)
                .values(TEXT, "quote", "Documenter et pr??senter l'??volution de la biodiversit??, c'est permettre ?? chacun de prendre conscience des enjeux et des menaces.", null, null, null, false)
                .values(TEXT, "questions.0.question", "Qu'est-ce que la biodiversit?? ?", null, null, null, false)
                .values(TEXT, "questions.0.answer", "La majorit?? des territoires d'outre-mer fran??ais sont situ??s dans des r??gions particuli??rement riches en esp??ces, notamment en esp??ces end??miques, ce qu'on appelle des points chauds de la biodiversit?? (?? Hotspots ?? en anglais) (Nouvelle-Cal??donie, Antilles, Mayotte, La R??union, Wallis et Futuna, Polyn??sie fran??aise). Avec la Guyane, la France poss??de une partie du plus grand massif forestier de la plan??te, l'Amazonie, et les territoires fran??ais r??unissent 10 % des r??cifs coralliens r??partis dans trois oc??ans. Certains territoires isol??s comme les ??les des Terres australes et antarctiques abritent une faune et une flore adapt??s ?? des climats extr??mes.", null, null, null, false)
                .values(TEXT, "questions.0.quote", "Avec ses territoires d'outre-mer, la France a donc une responsabilit?? forte dans la conservation de la biodiversit?? mondiale.", null, null, null, false)
                .values(TEXT, "questions.1.question", "Les outre-mer, des points chauds de biodiversit??", null, null, null, false)
                .values(TEXT, "questions.1.answer", "R??ponse 2", null, null, null, false)
                .values(TEXT, "questions.1.quote", "Citation 2", null, null, null, false)
                .build();

        Operation especesEnvahissantesElements =
            insertInto("page_element")
                .withDefaultValue("page_id", especesEnvahissantes)
                .withGeneratedValue("id", elementIdGenerator)
                .columns("type", "key", "text", "image_id", "alt", "href", "title")
                .values(TEXT, "presentation.description",  "des esp??ces consid??r??es comme les plus envahissantes au monde se trouvent en outre-mer.", null, null, null, false)
                .values(TEXT, "presentation.descriptionTerritories",  "des esp??ces consid??r??es comme les plus envahissantes au monde peuvent ??tre observ??.", null, null, null, false)
                .values(IMAGE, "presentation.image",  null, 104, "Esp??ces envahissantes", null, false)
                .values(CHECKBOX, "presentation.shadowed", true, null, null, null, false)
                .values(TEXT, "presentation.logoUrl",  "http://indicateurs-biodiversite.naturefrance.fr/fr/indicateurs/nombre-despeces-en-outremer-parmi-les-plus-envahissantes-au-monde", null, null, null, false)
                .values(SELECT, "presentation.sourceSelect",  "onb", null, null, null, false)
                .values(IMAGE, "understand.image",  null, 110, "Esp??ces envahissantes", null, false)
                .values(TEXT, "understand.title1",  "Qu???est-ce qu???une esp??ce exotique envahissante ?", null, null, null, false)
                .values(TEXT, "understand.text1",  "Une esp??ce exotique envahissante est une esp??ce introduite par l???Homme hors de son territoire d???origine et qui pr??sente, sur son territoire d???introduction, une dispersion rapide et des impacts environnementaux mais ??galement parfois sociaux et ??conomiques. Ces introductions sont parfois volontaires (pour l???agriculture, l???ornementation, comme animaux de compagnie, etc.) ou involontaires (via la multiplication des voyages internationaux, ?? passagers clandestins ?? dans les marchandises ??chang??es, dans les eaux de ballast des bateaux, etc.).", null, null, null, false)
                .values(TEXT, "understand.title2",  "Esp??ces exotiques vs. esp??ces indig??nes", null, null, null, false)
                .values(TEXT, "understand.text2",  "Si de nombreuses esp??ces n???arrivent pas ?? s???adapter ou se reproduire dans leurs territoires d???introduction, certaines s???adaptent au contraire tr??s bien, se d??veloppant en l???absence de leurs pr??dateurs ou parasites naturels. Elles peuvent alors causer de lourds impacts, en particulier dans les ??les o?? les ??cosyst??mes sont plus fragiles, par comp??tition avec les esp??ces indig??nes, pr??dation, hybridation, modification des habitats, etc.", null, null, null, false)
                .values(TEXT, "understand.keyword",  "Esp??ces", null, null, null, false)
                .values(TEXT, "understand.paragraphs.0.title",  "Une liste mondiale des esp??ces les plus probl??matiques", null, null, null, false)
                .values(TEXT, "understand.paragraphs.0.text",  "En 2007, l???Union international pour la conservation de la nature a publi?? une liste de 100 esp??ces exotiques consid??r??es comme les plus envahissantes du monde, c???est-??-dire celles avec le plus fort potentiel de dispersion et le plus lourd impact sur leur environnement d???accueil. On y trouve notamment le rat noir (Rattus rattus) introduit dans de nombreuses ??les et responsable, entre autres, du d??clin des populations de nombreuses esp??ces d???oiseau dans les outre-mer, ou encore le Miconia (Miconia calvescens) introduit ?? Tahiti en 1937 et aujourd???hui tr??s r??pandu sur l?????le o?? il remplace la v??g??tation indig??ne. Ce dernier a ??galement ??t?? introduit en Nouvelle-Cal??donie et d??couvert r??cemment en Martinique et en Guadeloupe. De nombreuses interventions sont r??alis??es pour ??radiquer ces esp??ces ou ?? d??faut tenter de limiter leur propagation et leurs impacts, et de nombreuses collectivit??s mettent en ??uvre des processus de bios??curit?? ?? l???entr??e des territoires afin de limiter les risques d???entr??e d???esp??ces exotiques.", null, null, null, false)
                .values(LINK, "understand.article", "Lien article", null, null, "/media/article/toto", false)
                .values(TEXT, "visual.title", "Titre", null, null, null, false)
                .values(IMAGE,"visual.image",null,110,"Infographie Saint-Pierre-et-Miquelon",null,false)
                .values(TEXT, "visual.subtitle", "Sous - Titre", null, null, null, false)
                .values(TEXT, "territories.title",  "Nombre d???esp??ces exotiques envahissantes par territoires", null, null, null, false)
                .values(TEXT, "ecogestures.title", "Que puis-je faire pour ??viter l???introduction et la dispersion d???esp??ces exotiques envahissantes ?", null, null, null, false)
                .build();

        Operation deforestationElements =
            insertInto("page_element")
                .withDefaultValue("page_id", deforestation)
                .withGeneratedValue("id", elementIdGenerator)
                .columns("type", "key", "text", "image_id", "alt", "href", "title")
                .values(TEXT, "presentation.description",  "de la for??t dispara??t...", null, null, null, false)
                .values(TEXT, "presentation.descriptionTerritories",  "de la for??t du territoire dispara??t...", null, null, null, false)
                .values(IMAGE, "presentation.image",  null, 11, "For??t", null, false)
                .values(CHECKBOX, "presentation.shadowed", true, null, null, null, false)
                .values(TEXT, "presentation.logoUrl",  "http://indicateurs-biodiversite.naturefrance.fr/fr/indicateurs/nombre-despeces-en-outremer-parmi-les-plus-envahissantes-au-monde", null, null, null, false)
                .values(SELECT, "presentation.sourceSelect", "inpn", null, null, null, false)
                .values(IMAGE, "understand.image",  null, 110, "For??t", null, false)
                .values(TEXT, "understand.title1",  "Raison 1", null, null, null, false)
                .values(TEXT, "understand.text1",  "Explication raison 1", null, null, null, false)
                .values(TEXT, "understand.title2",  "Raison 2", null, null, null, false)
                .values(TEXT, "understand.text2",  "Explication raison 2", null, null, null, false)
                .values(TEXT, "understand.keyword",  "For??ts", null, null, null, false)
                .values(LINK, "understand.article", "Lien article", null, null, "/media/article/toto", false)
                .values(TEXT, "visual.title", "Titre", null, null, null, false)
                .values(IMAGE,"visual.image",null,110,"Infographie Saint-Pierre-et-Miquelon",null,false)
                .values(TEXT, "visual.subtitle", "Sous - Titre", null, null, null, false)
                .values(TEXT, "territories.title",  "D??forestation par territoire", null, null, null, false)
                .values(TEXT, "ecogestures.title", "??cogestes", null, null, null, false)
                .build();

        Operation legalTermsElements =
            insertInto("page_element")
                .withDefaultValue("page_id", legalTerms)
                .withGeneratedValue("id", elementIdGenerator)
                .columns("type", "key", "text", "image_id", "alt", "href", "title")
                .values(TEXT, "title", "Mentions l??gales", null, null, null, true)
                .values(TEXT, "paragraphs.0.title", "Clause 1", null, null, null, false)
                .values(TEXT, "paragraphs.0.text", "Bla bla", null, null, null, false)
                .values(TEXT, "paragraphs.1.title", "Clause 2", null, null, null, false)
                .values(TEXT, "paragraphs.1.text", "Bla bla", null, null, null, false)
                .build();

        Operation antillesElements =
            insertInto("page_element")
                .withDefaultValue("page_id", antilles)
                .withGeneratedValue("id", elementIdGenerator)
                .columns("type", "key", "text", "image_id", "alt", "href", "title")
                .values(TEXT, "description", "Les Antilles sont un ????point chaud???? de la biodiversit?? mondiale. Si chacun des quatre territoires fran??ais du bassin a son identit?? et ses sp??cificit??s, les acteurs locaux collaborent autour de probl??matiques partag??es.", null, null, null, true)
                .build();

        Operation users = insertInto("app_user")
            .withGeneratedValue("id", ValueGenerators.sequence())
            .columns("login", "hashed_password")
            .values("admin", passwordHasher.hash("password"))
            .build();

        List<Operation> operations = new ArrayList<>(List.of(
            deleteAll,
            categories,
            indicators,
            indicatorCategories,
            indicatorValues,
            ecogestures,
            indicatorEcogestures,
            images,
            pages,
            homeElements,
            aboutElements,
            actElements,
            scienceElements,
            ecogestureHomeElements,
            ecogestureActSectionElements,
            territoryHomeElements,
            reunionElements,
            stPierreEtMiquelonElements,
            indicatorHomeElements,
            especesEnvahissantesElements,
            deforestationElements,
            legalTermsElements,
            antillesElements,
            users
        ));
        // 13 ecogestures
        operations.addAll(generateEcogestures(ecogesture1, elementIdGenerator));
        new DbSetup(destination, sequenceOf(operations)).launch();
    }
}
