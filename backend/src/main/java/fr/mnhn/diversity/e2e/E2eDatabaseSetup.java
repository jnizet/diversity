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
        Operation deleteAll = deleteAllFrom("page_element", "page", "image", "indicator_value", "indicator_category", "indicator", "category");

        Operation categories =
            insertInto("category")
                .columns("id", "name")
                .values(1, "Écosystèmes")
                .values(2, "Espèces menacées")
                .build();

        Operation indicators =
            insertInto("indicator")
                .columns("id", "biom_id")
                .values(1, "especes-envahissantes")
                .build();

        Operation indicatorCategories =
            insertInto("indicator_category")
                .columns("indicator_id", "category_id")
                .values(1, 1)
                .build();

        Operation indicatorValues =
            insertInto("indicator_value")
                .columns("id", "indicator_id", "territory", "value", "unit")
                .values(1, 1, "OUTRE_MER", 64, null)
                .values(2, 1, "REUNION", 40, null)
                .build();

        Long home = 1L;
        Long about = 2L;
        Long ecogesture1 = 3L;
        Long ecogestureHome = 4L;
        Long reunion = 10L;
        Long stPierreEtMiquelon = 11L;
        Long indicatorHome = 29L;
        Long especesEnvahissantes = 30L;
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
                .build();

        String png = MediaType.IMAGE_PNG_VALUE;
        Operation images =
            insertInto("image")
                .columns("id", "content_type", "original_file_name")
                .values(1L, png, "carousel1.png")
                .values(2L, png, "carousel2.png")
                .values(3L, png, "carousel3.png")
                .values(4L, png, "apropos.png")
                .values(5L, png, "indicateurs.png")
                .values(6L, png, "science-participative.png")
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
                .values(TEXT, "carousel.title", "Ensemble protégeons la diversité des Outre-Mer", null, null, null, false)
                .values(TEXT, "carousel.text", "Partez à la rencontre des espèces et des écosystèmes des territoires français d'outre-mer", null, null, null, false)
                .values(IMAGE, "carousel.images.0.image", null, 1L, "1", null, false)
                .values(IMAGE, "carousel.images.1.image", null, 2L, "2", null, false)
                .values(IMAGE, "carousel.images.2.image", null, 3L, "3", null, false)
                .values(TEXT, "carousel.territoriesButton", "Découvrir les territoires", null, null, null, false)
                .values(TEXT, "presentation.title", "Le compteur de biodiversité Outre-Mer", null, null, null, false)
                .values(TEXT, "presentation.text", "Les territoires d'outre-mer présentent une biodiversité particulièrement riche et variée, ...", null, null, null, false)
                .values(IMAGE, "presentation.image", null, 4L, "À propos", null, false)
                .values(TEXT, "presentation.more", "En savoir plus", null, null, null, false)
                .values(TEXT, "indicators.title", "Une biodiversité unique et fragile, protégeons-la !", null, null, null, false)
                .values(TEXT, "indicators.text", "La conservation de la biodiversité dépend de sa connaissance ...", null, null, null, false)
                .values(IMAGE, "indicators.image", null, 5L, "Indicateurs", null, false)
                .values(TEXT, "science.title", "Du « super-corail » pour sauver les récifs", null, null, null, false)
                .values(TEXT, "science.text", "Au sein de l'archipel des Seychelles le premier projet de restauration des récifs coralliens à grande échelle a débuté.", null, null, null, false)
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
                .columns("type", "key", "text", "image_id", "alt", "href")
                .values(TEXT, "name", "Espèces envahissantes", null, null, null)
                .values(TEXT, "presentation.category", "Espèces", null, null, null)
                .values(TEXT, "indicator.OUTRE_MER.value", "60", null, null, null)
                .values(TEXT, "presentation.description", "espèces sur les 100...", null, null, null)
                .values(IMAGE, "presentation.image", null, 104L, "Illustration", null)
                .values(TEXT, "understand.title", "Comprendre", null, null, null)
                .values(IMAGE, "understand.image", null, 104L, "Espèces envahissantes", null)
                .values(TEXT, "understand.sections.0.title", "Raison 1", null, null, null)
                .values(TEXT, "understand.sections.0.description", "Comprendre raison 1", null, null, null)
                .values(TEXT, "indicators.title", "Indicateurs", null, null, null)
                .values(TEXT, "indicator.REUNION.value", "6", null, null, null)
                .values(TEXT, "indicator.GUADELOUPE.value", "14", null, null, null)
                .values(TEXT, "indicator.SAINT_PIERRE_ET_MIQUELON.value", "23", null, null, null)
                .values(TEXT, "ecogestures.title", "Écogestes", null, null, null)
                .values(TEXT, "ecogestures.ecogestures.0.name", "Protégeons les récifs coralliens", null, null, null)
                .values(TEXT, "ecogestures.ecogestures.0.category", "Loisirs", null, null, null)
                .values(TEXT, "ecogestures.ecogestures.0.description", "Protégeons les récifs corallien...", null, null, null)
                .values(IMAGE, "ecogestures.ecogestures.0.image", null, 108L, "Tortue", null)
                .values(LINK, "ecogestures.ecogestures.0.link", "/ecogestes/recifs", null, null, "/indicateurs/surfaces-forets")
                .values(TEXT, "next.name", "Surfaces des forêts", null, null, null)
                .values(IMAGE, "next.image", null, 105L, "Surfaces des forêts", null)
                .values(LINK, "next.link", "Surfaces des forêts", null, null, "/indicateurs/surfaces-forets")
                .build();

        new DbSetup(destination, sequenceOf(
            deleteAll,
            categories,
            indicators,
            indicatorCategories,
            indicatorValues,
            images,
            pages,
            homeElements,
            aboutElements,
            ecogesture1Elements,
            ecogestureHomeElements,
            reunionElements,
            stPierreEtMiquelonElements,
            indicatorHomeElements,
            especesEnvahissantesElements
        )).launch();
    }
}
