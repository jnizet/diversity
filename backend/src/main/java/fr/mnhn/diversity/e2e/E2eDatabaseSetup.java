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
import fr.mnhn.diversity.home.HomeModel;
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

    public E2eDatabaseSetup(DataSource dataSource) {
        this.destination = new DataSourceDestination(dataSource);
    }

    @Override
    public void run(String... args) throws Exception {
        Operation deleteAll = deleteAllFrom("territory", "page_element", "page");
        Operation territories =
            insertInto("territory")
                .columns("id", "name", "slug")
                .values(1L, "Guadeloupe", "guadeloupe")
                .values(2L, "Saint-Pierre-Et-Miquelon", "st-pierre-et-miquelon")
                .values(3L, "Réunion", "reunion")
                .build();

        Long home = 1L;
        Long about = 2L;
        Operation pages =
            insertInto("page")
                .columns("id", "name", "model_name")
                .values(home, HomeModel.HOME_PAGE_NAME, HomeModel.HOME_PAGE_MODEL.getName())
                .values(about, AboutModel.ABOUT_PAGE_NAME, AboutModel.ABOUT_PAGE_MODEL.getName())
                .build();

        SequenceValueGenerator elementIdGenerator = ValueGenerators.sequence();

        Operation homeElements =
            insertInto("page_element")
                .withDefaultValue("page_id", home)
                .withGeneratedValue("id", elementIdGenerator)
                .columns("type", "key", "text", "image_id", "alt", "href")
                .values(TEXT, "carousel.title", "Ensemble protégeons la diversité des Outre-Mer", null, null, null)
                .values(TEXT, "carousel.text", "Partez à la rencontre des espèces et des écosystèmes des territoires français d'outre-mer", null, null, null)
                .values(IMAGE, "carousel.images.0.image", null, "600x400?text=carousel+1", "1", null)
                .values(IMAGE, "carousel.images.1.image", null, "600x400?text=carousel+2", "2", null)
                .values(IMAGE, "carousel.images.2.image", null, "600x400?text=carousel+3", "3", null)
                .values(TEXT, "carousel.territoriesButton", "Découvrir les territoires", null, null, null)
                .values(TEXT, "presentation.title", "Le compteur de biodiversité Outre-Mer", null, null, null)
                .values(TEXT, "presentation.text", "Les territoires d'outre-mer présentent une biodiversité perticulièrement riche et variée, ...", null, null, null)
                .values(IMAGE, "presentation.image", null, "600x400?text=a+propos", "À propos", null)
                .values(TEXT, "presentation.more", "En savoir plus", null, null, null)
                .values(TEXT, "indicators.title", "Une biodiversité unique et fragile, protégons-la !", null, null, null)
                .values(TEXT, "indicators.text", "La concervation de la biodiversité dépend de sa connaissance ...", null, null, null)
                .values(IMAGE, "indicators.image", null, "600x400?text=indicateurs", "Indicateurs", null)
                .values(TEXT, "science.title", "Du « super-corail » pour sauver les récifs", null, null, null)
                .values(TEXT, "science.text", "Au sein de l'archipel des Seychelles le premier projet de restauration des récifs coraliens à grande échelle a débuté.", null, null, null)
                .values(IMAGE, "science.image", null, "600x400?text=science+participative", "Science participative", null)
                .build();

        Operation aboutElements =
            insertInto("page_element")
                .withDefaultValue("page_id", about)
                .withGeneratedValue("id", elementIdGenerator)
                .columns("type", "key", "text", "image_id", "alt", "href")
                .values(TEXT, "header.title", "Pourquoi un compteur de la biodiversité en outre-mer ?", null, null, null)
                .values(TEXT, "header.subtitle", "Partager la connaissance et encourager chacun...", null, null, null)
                .values(IMAGE, "header.background", null, "600x400?text=fond", "Fond", null)
                .values(TEXT, "header.paragraphs.0.text", "Les territoires d'outre-mer présentent...", null, null, null)
                .values(TEXT, "header.paragraphs.1.text", "La mission du compteur est de...", null, null, null)
                .values(TEXT, "carousel.0.title", "Partager la connaissance scientifique", null, null, null)
                .values(TEXT, "carousel.0.text", "Quelles sont les espèces présentes sur ce territoire...", null, null, null)
                .values(IMAGE, "carousel.0.image", null, "600x400?text=carousel+1", "1", null)
                .values(LINK, "carousel.0.link", "Voir tous les indicateurs", null, null, "/indicators")
                .values(TEXT, "partners.title", "Ils contribuent au compteur de la biodiversité", null, null, null)
                .values(IMAGE, "partners.partners.0.logo", null, "50?text=logo1", "Logo1", null)
                .values(IMAGE, "partners.partners.1.logo", null, "50?text=logo2", "Logo2", null)
                .values(IMAGE, "partners.partners.2.logo", null, "50?text=logo3", "Logo3", null)
                .build();

        new DbSetup(destination, sequenceOf(
            deleteAll,
            territories,
            pages,
            homeElements,
            aboutElements
        )).launch();
    }
}
