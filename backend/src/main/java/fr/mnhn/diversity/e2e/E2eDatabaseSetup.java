package fr.mnhn.diversity.e2e;

import static com.ninja_squad.dbsetup.Operations.*;
import static fr.mnhn.diversity.model.ElementType.*;

import javax.sql.DataSource;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.generator.SequenceValueGenerator;
import com.ninja_squad.dbsetup.generator.ValueGenerators;
import com.ninja_squad.dbsetup.operation.Operation;
import fr.mnhn.diversity.about.AboutModel;
import fr.mnhn.diversity.admin.security.PasswordHasher;
import fr.mnhn.diversity.ecogesture.EcoGestureModel;
import fr.mnhn.diversity.home.HomeModel;
import fr.mnhn.diversity.image.ImageType;
import fr.mnhn.diversity.indicator.IndicatorModel;
import fr.mnhn.diversity.territory.TerritoryModel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
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
                .values(1, "Écosystèmes")
                .values(2, "Espèces menacées")
                .values(3, "Végétation")
                .build();

        Operation indicators =
            insertInto("indicator")
                .columns("id", "biom_id", "slug")
                .values(1, "i1", "especes-envahissantes")
                .values(2, "i2", "deforestation")
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
                .values(11, 1, "OUTRE_MER", 64, null)
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
                .build();

        Operation indicatorEcogestures =
            insertInto("indicator_ecogesture")
                .columns("indicator_id", "ecogesture_id")
                .values(1, 1)
                .values(2, 1)
                .build();

        Long home = 1L;
        Long about = 2L;
        Long ecogesture1 = 3L;
        Long ecogestureHome = 4L;
        Long reunion = 10L;
        Long stPierreEtMiquelon = 11L;
        Long indicatorHome = 29L;
        Long especesEnvahissantes = 30L;
        Long deforestation = 31L;
        Operation pages =
            insertInto("page")
                .columns("id", "name", "model_name", "title")
                .values(home, HomeModel.HOME_PAGE_NAME, HomeModel.HOME_PAGE_MODEL.getName(), "Accueil")
                .values(about, AboutModel.ABOUT_PAGE_NAME, AboutModel.ABOUT_PAGE_MODEL.getName(), "À propos")
                .values(ecogesture1, "recifs", EcoGestureModel.ECO_GESTURE_PAGE_MODEL.getName(), "Écogeste: protéger les récifs")
                .values(ecogestureHome, EcoGestureModel.ECO_GESTURE_HOME_PAGE_NAME, EcoGestureModel.ECO_GESTURE_HOME_PAGE_MODEL.getName(), "Écogestes")
                .values(reunion, "reunion", TerritoryModel.TERRITORY_PAGE_MODEL.getName(), "La Réunion")
                .values(stPierreEtMiquelon, "st-pierre-et-miquelon", TerritoryModel.TERRITORY_PAGE_MODEL.getName(), "Saint Pierre et Miquelon")
                .values(indicatorHome, IndicatorModel.INDICATOR_HOME_PAGE_NAME, IndicatorModel.INDICATOR_HOME_PAGE_MODEL.getName(), "Indicateurs")
                .values(especesEnvahissantes, "especes-envahissantes", IndicatorModel.INDICATOR_PAGE_MODEL.getName(), "Espèces envahissantes")
                .values(deforestation, "deforestation", IndicatorModel.INDICATOR_PAGE_MODEL.getName(), "Déforestation")
                .build();

        String png = ImageType.PNG.getMediaType().toString();
        String jpg = ImageType.JPG.getMediaType().toString();
        String svg = ImageType.SVG.getMediaType().toString();
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
                .values(30L, png, "ecogeste.png")
                .values(31L, png, "fiche-technique.png")
                .values(32L, png, "comprendre.png")
                .values(33L, png, "vignette1.png")
                .values(34L, png, "vignette2.png")
                .values(40L, jpg, "indicateurs.jpg")
                .values(91L, png, "ecogestes.png")
                .values(101L, png, "interest1.png")
                .values(102L, png, "interest2.png")
                .values(103L, png, "indicators1.png")
                .values(104L, png, "papangue.png")
                .values(105L, png, "other.png")
                .values(106L, png, "reunion.png")
                .values(107L, png, "st-pierre-et-miquelon.png")
                .values(108L, png, "tourbieres.png")
                .values(109L, png, "ressources-naturelles.png")
                .build();

        SequenceValueGenerator elementIdGenerator = ValueGenerators.sequence();

        Operation homeElements =
            insertInto("page_element")
                .withDefaultValue("page_id", home)
                .withGeneratedValue("id", elementIdGenerator)
                .columns("type", "key", "text", "image_id", "alt", "href", "title")
                .values(TEXT, "carousel.title", "Découvrez la biodiversité des Outre-Mer", null, null, null, false)
                .values(TEXT, "carousel.text", "Partez à la rencontre des espèces et des écosystèmes des territoires français d'outre-mer", null, null, null, false)
                .values(IMAGE, "carousel.images.0.image", null, 1L, "Chelonia mydas ©  B. Guichard", null, false)
                .values(IMAGE, "carousel.images.1.image", null, 2L, "Légende n°2", null, false)
                .values(IMAGE, "carousel.images.2.image", null, 3L, "Légende n°3", null, false)
                .values(IMAGE, "carousel.images.3.image", null, 300L, "Légende n°4", null, false)
                .values(TEXT, "carousel.territoriesButton", "Découvrir les territoires", null, null, null, false)
                .values(TEXT, "presentation.title", "Le compteur de biodiversité Outre-Mer", null, null, null, false)
                .values(TEXT, "presentation.text", "Les territoires d'outre-mer présentent une biodiversité particulièrement riche et variée, mais fragilisée par les activités humaines. La mission du compteur est de donner une vision d'ensemble des enjeux liés à la biodiversité en outre-mer afin que chacun, résident ou voyageur de passage, puisse s'informer et s'impliquer à son échelle pour la préservation de cette biodiversité exceptionnelle.", null, null, null, false)
                .values(TEXT, "presentation.indicators", "Des chiffres clés pour comprendre", null, null, null, false)
                .values(TEXT, "presentation.ecogestures", "Des gestes simples à mettre en place au quotidien", null, null, null, false)
                .values(TEXT, "presentation.science", "Participez aux programmes pour faire progresser la connaissance", null, null, null, false)
                .values(TEXT, "presentation.territories", "Découvrez l'identité de chaque territoire d'outre-mer", null, null, null, false)
                .values(TEXT, "presentation.quote", "Les outre-mer regroupent 80 % de la biodiversité française, réinventons, adaptons notre façon de vivre et de voyager", null, null, null, false)
                .values(TEXT, "testimony.title", "Une biodiversité unique et fragile, protégeons-la !", null, null, null, false)
                .values(TEXT, "testimony.text", "La conservation de la biodiversité dépend de sa connaissance et de la compréhension des phénomènes qui l'impactent. Les indicateurs sont des outils qui permettent de suivre différents paramètres de la biodiversité, pour renseigner de son évolution.", null, null, null, false)
                .values(TEXT, "testimony.quote", "Ces actions conjuguées entraînent un accroissement de la biodiversité, une amélioration de la qualité de l'eau, de l'air mais aussi de la qualité de vie.", null, null, null, false)
                .values(IMAGE, "testimony.image", null, 5L, "Témoignage", null, false)
                .values(TEXT, "science.title", "La photo-identification de serpents de mer par un groupe de plongeuses amatrices", null, null, null, false)
                .values(TEXT, "science.text", "Surnommées les « grand-mères fantastiques », un groupe de 7 plongeuses entre 60 et 75 ans ont collaboré avec les chercheurs de l'Université de Nouvelle-Calédonie pour étudier un serpent de mer méconnu, l'Hydrophis major, dans la baie des Citrons, au sud de Nouméa. Leurs photographies très régulières ont permis de recenser 250 individus différents de ce serpent dont on pensait la présence anecdotique.", null, null, null, false)
                .values(IMAGE, "science.image", null, 6L, "Science participative", null, false)
                .build();

        Operation aboutElements =
            insertInto("page_element")
                .withDefaultValue("page_id", about)
                .withGeneratedValue("id", elementIdGenerator)
                .columns("type", "key", "text", "image_id", "alt", "href", "title")
                .values(TEXT, "header.title", "Pourquoi un compteur de la biodiversité en outre-mer ?", null, null, null, true)
                .values(TEXT, "header.subtitle", "La mission du compteur est de donner une vision d’ensemble des enjeux liés à la biodiversité en outre-mer, et s’articule autour de trois objectifs : ", null, null, null, false)
                .values(IMAGE, "header.background", null, 7, "Légende", null, false)
                .values(TEXT, "goal1.title", "Partager la connaissance scientifique", null, null, null, true)
                .values(TEXT, "goal1.description", "Quelles sont les espèces présentes dans ce territoire ? Comment évoluent leurs populations ? Quel est l’état de santé des récifs coralliens ? Pour répondre à ces questions et bien d’autres, différents paramètres, appelés « indicateurs », sont mesurés par les scientifiques pour suivre l’évolution de l’état de la biodiversité. Le compteur a pour mission de rendre compte de l’état de ces connaissances dans tous les territoires.", null, null, null, false)
                .values(IMAGE, "goal1.image", null, 11, "Légende", null, false)
                .values(TEXT, "goal2.title", "Valoriser les actions des territoires", null, null, null, true)
                .values(TEXT, "goal2.description", "De nombreuses actions locales sont mises en place afin de tenter de préserver les écosystèmes et les espèces. Donner de la visibilité aux acteurs locaux, partager les expériences et les réussites, au bénéfice de tous, c’est également l’un des objectifs du compteur.", null, null, null, false)
                .values(IMAGE, "goal2.image", null, 12, "Légende", null, false)
                .values(TEXT, "goal2.quote", "Les territoires d’outre-mer présentent une biodiversité particulièrement riche et variée, mais fragilisée par les activités humaines.", null, null, null, false)
                .values(IMAGE, "goal2.quoteImage", null, 14, "Légende", null, false)
                .values(TEXT, "goal3.title", "Encourager chacun à agir", null, null, null, true)
                .values(TEXT, "goal3.description", "Chacun, à son échelle, peut agir pour préserver la biodiversité. Connaitre et appliquer des gestes simples, modifier ses habitudes et son comportement ou encore s’investir dans desprogrammes de sciences participatives, tout le monde est concerné par la préservation de la biodiversité. Le rôle du compteur est d’accompagner les citoyens en partageant desinformations claires et adaptées à tous les territoires, et en encourageant à se mobiliser.", null, null, null, false)
                .values(IMAGE, "goal3.image", null, 13, "Légende", null, false)
                .values(TEXT, "partners.title", "Ils contribuent au compteur de la biodiversité", null, null, null, false)
                .values(IMAGE, "partners.partners.0.logo", null, 8, "Logo1", null, false)
                .values(TEXT, "partners.partners.0.url", "http://google.com", null, null, null, false)
                .values(IMAGE, "partners.partners.1.logo", null, 9, "Logo2", null, false)
                .values(TEXT, "partners.partners.1.url", "http://google.com", null, null, null, false)
                .values(IMAGE, "partners.partners.2.logo", null, 10, "Logo3", null, false)
                .values(TEXT, "partners.partners.2.url", "http://google.com", null, null, null, false)
                .build();

        Operation ecogesture1Elements =
            insertInto("page_element")
                .withDefaultValue("page_id", ecogesture1)
                .withGeneratedValue("id", elementIdGenerator)
                .columns("type", "key", "text", "image_id", "alt", "href", "title")
                .values(TEXT, "presentation.name", "Protégeons les récifs coralliens", null, null, null, true)
                .values(TEXT, "presentation.category", "Loisirs", null, null, null, false)
                .values(TEXT, "presentation.description", "Sinon ils vont mourir", null, null, null, false)
                .values(IMAGE, "presentation.image", null, 30L, "Jolis coraux", null, false)
                .values(IMAGE, "presentation.file", null, 31L, "Fiche technique", null, false)
                .values(TEXT, "understand.title", "Comprendre : un écosystème très riche", null, null, null, false)
                .values(TEXT, "understand.text", "Les récifs coralliens affichent plus d'un tiers des espèces marines connues...", null, null, null, false)
                .values(IMAGE, "understand.image", null, 32L, "Comprendre", null, false)
                .values(TEXT, "action.title", "Les bons gestes pour protéger les récifs", null, null, null, false)
                .values(IMAGE, "action.cards.0.icon", null, 33L, "Crème solaire", null, false)
                .values(TEXT, "action.cards.0.description", "Je choisis une crème solaire non nocive pour l'environnement", null, null, null, false)
                .values(IMAGE, "action.cards.1.icon", null, 34L, "Bateau", null, false)
                .values(TEXT, "action.cards.1.description", "En bâteau, je ne jette pas l'ancre à proximité de récifs", null, null, null, false)
                .build();

        Operation ecogestureHomeElements =
            insertInto("page_element")
                .withDefaultValue("page_id", ecogestureHome)
                .withGeneratedValue("id", elementIdGenerator)
                .columns("type", "key", "text", "image_id", "alt", "href", "title")
                .values(TEXT, "title", "Réinventons notre façon de vivre et de voyager grâce aux écogestes", null, null, null, true)
                .values(TEXT, "presentation", "Lorem ipsum dolor", null, null, null, false)
                .values(IMAGE, "image", null, 91L, "Ecogestes", null, false)
                .build();

        Operation reunionElements =
            insertInto("page_element")
                .withDefaultValue("page_id", reunion)
                .withGeneratedValue("id", elementIdGenerator)
                .columns("type", "key", "text", "image_id", "alt", "href", "title")
                .values(TEXT, "name", "La Réunion", null, null, null, true)
                .values(TEXT, "identity.title", "Un climat tropical", null, null, null, true)
                .values(TEXT, "identity.presentation", "À l'ouest de l'Océan Indien...", null, null, null, false)
                .values(IMAGE, "identity.infography", null, 106L, "Infographie Réunion", null, false)
                .values(TEXT, "interests.title", "À la découverte d'endroits emblématiques", null, null, null, false)
                .values(IMAGE, "interests.images.0.image", null, 101L, "Intérêt 1", null, false)
                .values(IMAGE, "interests.images.1.image", null, 102L, "Intérêt 2", null, false)
                .values(TEXT, "indicators.title", "Indicateurs", null, null, null, false)
                .values(TEXT, "indicators.indicators.0.name", "Espèces inventoriées à la Réunion", null, null, null, false)
                .values(TEXT, "indicators.indicators.0.value", "4123", null, null, null, false)
                .values(IMAGE, "indicators.indicators.0.image", null, 103L, "Espèces inventoriées à la Réunion", null, false)
                .values(LINK, "indicators.indicators.0.link", "Voir les espèces", null, null, "/indicators/especes", false)
                .values(TEXT, "species.title", "Espèces", null, null, null, false)
                .values(TEXT, "species.species.0.name", "Papangue", null, null, null, false)
                .values(TEXT, "species.species.0.description", "Dernier rapace de la Réunion", null, null, null, false)
                .values(IMAGE, "species.species.0.image", null, 104L, "Papangue", null, false)
                .values(TEXT, "ecosystems.title", "Écosystèmes", null, null, null, false)
                .values(TEXT, "timeline.title", "Frise chronologique", null, null, null, false)
                .values(TEXT, "risks.title", "Risques et enjeux", null, null, null, false)
                .values(IMAGE, "other.image", null, 105L, "Autre territoire", null, false)
                .values(LINK, "other.link", "Portail local de l'environnement", null, null, "https://oeil.nc", false)
                .build();

        Operation stPierreEtMiquelonElements =
            insertInto("page_element")
                .withDefaultValue("page_id", stPierreEtMiquelon)
                .withGeneratedValue("id", elementIdGenerator)
                .columns("type", "key", "text", "image_id", "alt", "href", "title")
                .values(TEXT, "name", "Saint-Pierre-Et-Miquelon", null, null, null, true)
                .values(TEXT, "identity.title", "Un climat subarctique, froid et humide", null, null, null, true)
                .values(TEXT, "identity.presentation", "Au sud de Terre-Neuve (Canada)...", null, null, null, false)
                .values(IMAGE, "identity.infography", null, 107L, "Infographie St-Pierre-Et-Miquelon", null, false)
                .values(TEXT, "interests.title", "À la découverte d'endroits emblématiques", null, null, null, false)
                .values(IMAGE, "interests.images.0.image", null, 101L, "Intérêt 1", null, false)
                .values(TEXT, "indicators.title", "Indicateurs", null, null, null, false)
                .values(TEXT, "ecosystems.title", "Écosystèmes", null, null, null, false)
                .values(TEXT, "ecosystems.ecosystems.0.name", "Les marais tourbeux", null, null, null, false)
                .values(TEXT, "ecosystems.ecosystems.0.description", "Ces zones humides...", null, null, null, false)
                .values(IMAGE, "ecosystems.ecosystems.0.image", null, 108L, "Tourbières", null, false)
                .values(TEXT, "species.title", "Espèces", null, null, null, false)
                .values(TEXT, "timeline.title", "Frise chronologique", null, null, null, false)
                .values(TEXT, "timeline.events.0.name", "1535", null, null, null, false)
                .values(TEXT, "timeline.events.0.description", "Prise de possession française...", null, null, null, false)
                .values(TEXT, "timeline.events.1.name", "17ème siècle", null, null, null, false)
                .values(TEXT, "timeline.events.1.description", "Premières installations permanentes...", null, null, null, false)
                .values(TEXT, "risks.title", "Risques et enjeux", null, null, null, false)
                .values(TEXT, "risks.risks.0.name", "Les ressources naturelles", null, null, null, false)
                .values(TEXT, "risks.risks.0.description", "Le territoire est pour l'instant...", null, null, null, false)
                .values(IMAGE, "risks.risks.0.image", null, 109L, "Ressources naturelles", null, false)
                .values(IMAGE, "other.image", null, 105L, "Autre territoire", null, false)
                .values(LINK, "other.link", "Portail local de l'environnement", null, null, "https://oeil.nc", false)
                .build();

        Operation indicatorHomeElements =
            insertInto("page_element")
                .withDefaultValue("page_id", indicatorHome)
                .withGeneratedValue("id", elementIdGenerator)
                .columns("type", "key", "text", "image_id", "alt", "href", "title")
                .values(TEXT, "title", "Compter la biodiversité, oui mais comment ?", null, null, null, true)
                .values(TEXT, "presentation", "Les indicateurs sont des outils qui permettent de suivre différents paramètres de la biodiversité, pour renseigner de son évolution. De nombreux paramètres sont étudiés : effectifs des populations, état de santé des écosystèmes, taux de pollutions, etc. Ils renseignent ainsi l'état de la biodiversité à un instant précis, mais également l'état des connaissances et des moyens scientifiques actuels.", null, null, null, false)
                .values(IMAGE, "image", null, 40, "Chelonia mydas ©  B. Guichard", null, false)
                .values(TEXT, "understand.title", "Les indicateurs, des outils pour évaluer la biodiversité", null, null, null, false)
                .values(TEXT, "understand.description", "La conservation de la biodiversité dépend de sa connaissance et de la compréhension des phénomènes qui l'impactent. Les indicateurs sont des outils qui permettent de suivre différents paramètres de la biodiversité, pour renseigner de son évolution. De nombreux paramètres sont étudiés : effectifs des populations, état de santé des écosystèmes, taux de pollutions, etc. Cependant, ils connaissent des limites par manque de données disponibles, manque de moyens de recherche ou parce que certains paramètres sont très complexes à évaluer. Ils renseignent ainsi l'état de la biodiversité à un instant précis, mais également l'état des connaissances et des moyens scientifiques actuels.", null, null, null, false)
                .values(IMAGE, "understand.image", null, 5, "Chelonia mydas ©  B. Guichard", null, false)
                .values(TEXT, "onb.title", "L'observatoire national de la biodiversité", null, null, null, false)
                .values(TEXT, "onb.description", "Depuis 2012, l'Observatoire national de la biodiversité, actuellement piloté par l'Office français pour la biodiversité (OFB), publie des indicateurs sur l'état de la biodiversité française, avec la contribution de nombreux partenaires. Ils sont régulièrement actualisés et enrichis. Le Compteur met en lumière les indicateurs de l'ONB qui concernent les outre-mer et contribue à la création de nouveaux indicateurs pour ces territoires.", null, null, null, false)
                .values(LINK, "onb.link", "Retrouvez les indicateurs sur le site de l'ONB", null, null, "http://indicateurs-biodiversite.naturefrance.fr", false)
                .values(TEXT, "quote", "Documenter et présenter l'évolution de la biodiversité, c'est permettre à chacun de prendre conscience des enjeux et des menaces.", null, null, null, false)
                .values(TEXT, "questions.0.question", "Qu'est-ce que la biodiversité ?", null, null, null, false)
                .values(TEXT, "questions.0.answer", "La majorité des territoires d'outre-mer français sont situés dans des régions particulièrement riches en espèces, notamment en espèces endémiques, ce qu'on appelle des points chauds de la biodiversité (« Hotspots » en anglais) (Nouvelle-Calédonie, Antilles, Mayotte, La Réunion, Wallis et Futuna, Polynésie française). Avec la Guyane, la France possède une partie du plus grand massif forestier de la planète, l'Amazonie, et les territoires français réunissent 10 % des récifs coralliens répartis dans trois océans. Certains territoires isolés comme les îles des Terres australes et antarctiques abritent une faune et une flore adaptés à des climats extrêmes.", null, null, null, false)
                .values(TEXT, "questions.0.quote", "Avec ses territoires d'outre-mer, la France a donc une responsabilité forte dans la conservation de la biodiversité mondiale.", null, null, null, false)
                .values(TEXT, "questions.1.question", "Les outre-mer, des points chauds de biodiversité", null, null, null, false)
                .values(TEXT, "questions.1.answer", "Réponse 2", null, null, null, false)
                .values(TEXT, "questions.1.quote", "Citation 2", null, null, null, false)
                .build();

        Operation especesEnvahissantesElements =
            insertInto("page_element")
                .withDefaultValue("page_id", especesEnvahissantes)
                .withGeneratedValue("id", elementIdGenerator)
                .columns("type", "key", "text", "image_id", "alt", "href", "title")
                .values(TEXT, "name", "Espèces envahissantes", null, null, null, true)
                .values(TEXT, "presentation.description", "espèces sur les 100...", null, null, null, false)
                .values(IMAGE, "presentation.image", null, 104L, "Illustration", null, false)
                .values(TEXT, "understand.title", "Comprendre", null, null, null, false)
                .values(IMAGE, "understand.image", null, 104L, "Espèces envahissantes", null, false)
                .values(TEXT, "understand.sections.0.title", "Raison 1", null, null, null, false)
                .values(TEXT, "understand.sections.0.description", "Comprendre raison 1", null, null, null, false)
                .values(TEXT, "indicators.title", "Indicateurs", null, null, null, false)
                .values(TEXT, "ecogestures.title", "Écogestes", null, null, null, false)
                .build();

        Operation deforestationElements =
            insertInto("page_element")
                .withDefaultValue("page_id", deforestation)
                .withGeneratedValue("id", elementIdGenerator)
                .columns("type", "key", "text", "image_id", "alt", "href", "title")
                .values(TEXT, "name",  "Déforestation", null, null, null, true)
                .values(TEXT, "presentation.description",  "de la forêt disparaît...", null, null, null, false)
                .values(IMAGE, "presentation.image",  null, 105, "Forêt", null, false)
                .values(TEXT, "understand.title",  "Comprendre", null, null, null, false)
                .values(IMAGE, "understand.image",  null, 105, "Forêt", null, false)
                .values(TEXT, "understand.sections.0.title",  "Raison 1", null, null, null, false)
                .values(TEXT, "understand.sections.0.description",  "Explication raison 1", null, null, null, false)
                .values(TEXT, "understand.sections.1.title",  "Raison 2", null, null, null, false)
                .values(TEXT, "understand.sections.1.description",  "Explication raison 2", null, null, null, false)
                .values(TEXT, "indicators.title",  "Déforestation par territoire", null, null, null, false)
                .values(TEXT, "ecogestures.title", "Écogestes", null, null, null, false)
                .build();

        Operation users = insertInto("app_user")
            .withGeneratedValue("id", ValueGenerators.sequence())
            .columns("login", "hashed_password")
            .values("admin", passwordHasher.hash("password"))
            .build();

        new DbSetup(destination, sequenceOf(
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
            ecogesture1Elements,
            ecogestureHomeElements,
            reunionElements,
            stPierreEtMiquelonElements,
            indicatorHomeElements,
            especesEnvahissantesElements,
            deforestationElements,
            users
        )).launch();
    }
}
