package fr.mnhn.diversity.territory;

import fr.mnhn.diversity.model.meta.ListElement;
import fr.mnhn.diversity.model.meta.PageModel;
import fr.mnhn.diversity.model.meta.SectionElement;

/**
 * The model for the territory page
 */
public final class TerritoryModel {
    public static final PageModel TERRITORY_PAGE_MODEL =
            PageModel.builder("territory").describedAs("Territoire")
                     .withPathFactory(slug -> "/territoires/" + slug)
                     // Carte d'identité
                     .section(SectionElement.builder("identity").describedAs("Carte d'identité")
                                            .titleText("title", "Titre de la présentation")
                                            .text("subtitle", "Sous-titre de la présentation")
                                            .multiLineText("presentation", "Texte de la présentation")
                                            .image("image", "Illustration de la présentation")
                                            .text("area", "Superficie terrestre en km2")
                                            .text("marineArea", "Superficie marine en km2")
                                            .text("population", "Nombre d'habitants")
                                            .text("populationYear", "Année de recensement du nombre d'habitants")
                                            .text("species", "Nombre d'espèces indigènes")
                                            .text("highestPoint", "Altitude du point culminant en mètres")
                                            .text("highestPointName", "Nom du point culminant")
                     )
                     // statistiques indiquées dans la carte
                     .list(ListElement.builder("statistics").describedAs("Statistiques affichées dans la carte")
                           .text("number", "Nombre (et unité)")
                           .text("text", "Texte suivant le nombre")
                     )
                     // Lieux d'intérêts
                     .section(SectionElement.builder("interests").describedAs("Lieux d'intérêts")
                                            .text("title", "Titre du carousel des lieux d'intérêts")
                                            .list(ListElement.builder("locations").describedAs("Lieux affichés par le carousel")
                                                             .text("name", "Nom du lieu")
                                                             .text("description", "Courte description du lieu")
                                                             .image("image", "Petite photo du lieu")
                                            )
                     )
                     // Espèces
                     .list(ListElement.builder("species").describedAs("Les espèces du territoire")
                                      .text("name", "Nom de l'espèce")
                                      .multiLineText("description", "Description de l'espèce")
                                      .multiSizeImage("image", "Photo illustrant l'espèce")
                     )
                     // Frise chronologique
                     .list(ListElement.builder("events").describedAs("Événements de la frise chronologique")
                                       .text("date", "Année / période de l'énénement")
                                       .text("description", "Brève description de l'événement")
                     )
                     // Écosystèmes
                     .section(SectionElement.builder("ecosystems")
                                            .describedAs("Écosystèmes")
                                            .list(ListElement.builder("ecosystems").describedAs("Écosystèmes du territoire")
                                                             .text("name", "Nom de l'écosystème")
                                                             .multiLineText("description", "Description de l'écosystème")
                                                             .multiSizeImage("image", "Image illustrant l'écosystème")
                                            )
                                            .multiSizeImage("image", "Image placée à côté de l'indicateur")
                     )
                     // Enjeux et menaces
                     .section(SectionElement.builder("risks").describedAs("Enjeux et menaces")
                                            .text("title", "Titre de la section")
                                            .list(ListElement.builder("risks").describedAs("Les enjeux et menaces du territoire")
                                                             .text("name", "Nom de l'enjeu")
                                                             .multiLineText("description", "Description de l'enjeu")
                                            )
                     )
                     .build();

    public static final String TERRITORY_HOME_PAGE_NAME = "territoires";
    public static final PageModel TERRITORY_HOME_PAGE_MODEL =
        PageModel.builder("territories").describedAs("Territoires")
                 .withPath("/territoires")
                 .section(SectionElement
                              .builder("header").describedAs("En-tête")
                              .titleText("title", "Titre de la page")
                              .multiLineText("text", "Texte de l'en-tête")
                              .text("population", "Nombre d'habitants")
                              .text("species", "Nombre d'espèces indigènes")
                 )
                 .build();

    public static final PageModel ZONE_PAGE_MODEL =
        PageModel.builder("territory-zone").describedAs("Bassin de territoires")
                 .withPath("/territoires")
                 .text("description", "Description du bassin, affichée dans la carte")
                 .build();

    private TerritoryModel() {
    }
}
