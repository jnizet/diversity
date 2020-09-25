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
import fr.mnhn.diversity.ecogesture.EcoGestureModel;
import fr.mnhn.diversity.home.HomeModel;
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

    public E2eDatabaseSetup(DataSource dataSource) {
        this.destination = new DataSourceDestination(dataSource);
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
                "image"
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

        String png = MediaType.IMAGE_PNG_VALUE;
        String jpg = MediaType.IMAGE_JPEG_VALUE;
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
                .values(7L, png, "fond.png")
                .values(8L, png, "logo1.png")
                .values(9L, png, "logo2.png")
                .values(10L, png, "logo3.png")
                .values(30L, png, "ecogeste.png")
                .values(31L, png, "fiche-technique.png")
                .values(32L, png, "comprendre.png")
                .values(33L, png, "vignette1.png")
                .values(34L, png, "vignette2.png")
                .values(40L, png, "indicateurs.png")
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
                .values(TEXT, "presentation.territories", "Découvrez l'identité de chaque territoire d’outre-mer", null, null, null, false)
                .values(TEXT, "presentation.quote", "Les outre-mer regroupent 80 % de la biodiversité française, réinventons, adaptons notre façon de vivre et de voyager", null, null, null, false)
                .values(TEXT, "testimony.title", "Une biodiversité unique et fragile, protégeons-la !", null, null, null, false)
                .values(TEXT, "testimony.text", "La conservation de la biodiversité dépend de sa connaissance et de la compréhension des phénomènes qui l'impactent. Les indicateurs sont des outils qui permettent de suivre différents paramètres de la biodiversité, pour renseigner de son évolution.", null, null, null, false)
                .values(TEXT, "testimony.quote", "Ces actions conjuguées entraînent un accroissement de la biodiversité, une amélioration de la qualité de l'eau, de l'air mais aussi de la qualité de vie.", null, null, null, false)
                .values(IMAGE, "testimony.image", null, 5L, "Témoignage", null, false)
                .values(TEXT, "science.title", "La photo-identification de serpents de mer par un groupe de plongeuses amatrices", null, null, null, false)
                .values(TEXT, "science.text", "Surnommées les « grand-mères fantastiques », un groupe de 7 plongeuses entre 60 et 75 ans ont collaboré avec les chercheurs de l’Université de Nouvelle-Calédonie pour étudier un serpent de mer méconnu, l'Hydrophis major, dans la baie des Citrons, au sud de Nouméa. Leurs photographies très régulières ont permis de recenser 250 individus différents de ce serpent dont on pensait la présence anecdotique.", null, null, null, false)
                .values(IMAGE, "science.image", null, 6L, "Science participative", null, false)
                .build();

        Operation aboutElements =
            insertInto("page_element")
                .withDefaultValue("page_id", about)
                .withGeneratedValue("id", elementIdGenerator)
                .columns("type", "key", "text", "image_id", "alt", "href", "title")
                .values(TEXT, "header.title", "Pourquoi un compteur de la biodiversité en outre-mer ?", null, null, null, true)
                .values(TEXT, "header.subtitle", "Partager la connaissance et encourager chacun...", null, null, null, false)
                .values(IMAGE, "header.background", null, 7L, "Fond", null, false)
                .values(TEXT, "header.paragraphs.0.text", "Les territoires d'outre-mer présentent...", null, null, null, false)
                .values(TEXT, "header.paragraphs.1.text", "La mission du compteur est de...", null, null, null, false)
                .values(TEXT, "carousel.0.title", "Partager la connaissance scientifique", null, null, null, false)
                .values(TEXT, "carousel.0.text", "Quelles sont les espèces présentes sur ce territoire...", null, null, null, false)
                .values(IMAGE, "carousel.0.image", null, 1L, "1", null, false)
                .values(LINK, "carousel.0.link", "Voir tous les indicateurs", null, null, "/indicators", false)
                .values(TEXT, "partners.title", "Ils contribuent au compteur de la biodiversité", null, null, null, false)
                .values(IMAGE, "partners.partners.0.logo", null, 8L, "Logo1", null, false)
                .values(IMAGE, "partners.partners.1.logo", null, 9L, "Logo2", null, false)
                .values(IMAGE, "partners.partners.2.logo", null, 10L, "Logo3", null, false)
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
                .values(TEXT, "presentation", "Lorem ipsum dolor", null, null, null, false)
                .values(IMAGE, "image", null, 40, "Indicateurs", null, false)
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
                .values(TEXT, "next.name", "Surfaces des forêts", null, null, null, false)
                .values(IMAGE, "next.image", null, 105L, "Surfaces des forêts", null, false)
                .values(LINK, "next.link", "Surfaces des forêts", null, null, "/indicateurs/surfaces-forets", false)
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
                .values(TEXT, "next.name", "Espèces envahissantes", null, null, null, false)
                .values(IMAGE, "next.image", null, 104, "Espèces envahissantes", null, false)
                .values(LINK, "next.link", "Espèces envahissantes", null, null, "/indicateurs/especes-envahissantes", false)
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
            deforestationElements
        )).launch();
    }
}
