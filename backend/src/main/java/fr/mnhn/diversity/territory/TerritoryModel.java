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
                     .titleText("name", "Nom du territoire")
                     // Carte d'identité
                     .section(SectionElement.builder("identity").describedAs("Carte d'identité")
                                            .titleText("title", "Titre de la présentation")
                                            .multiLineText("presentation", "Texte de la présentation")
                                            .image("infography", "Infographie de la présentation")
                     )
                    // Lieux d'intérêts
                    .section(SectionElement.builder("interests").describedAs("Lieux d'intérêts")
                                           .text("title", "Titre du carousel des lieux d'intérêts")
                                           .list(ListElement.builder("images").describedAs("Les photos affichées par le carousel")
                                                            .image("image", "Photo")
                                           )
                     )
                     // Indicateurs
                     .section(SectionElement.builder("indicators").describedAs("Indicateurs")
                                            .text("title", "Titre de la section indicateurs")
                                            .list(ListElement.builder("indicators").describedAs("Les indicateurs du territoire")
                                                             .text("name", "Nom de l'indicateur")
                                                             // TODO: indicators value are stored as text for now
                                                             .text("value", "Valeur de l'indicateur")
                                                             .image("image", "Image illustrant l'indicateur")
                                                             .link("link", "Lien vers l'indicateur")
                                            )
                     )
                     // Espèces
                     .section(SectionElement.builder("species").describedAs("Espèces")
                                            .text("title", "Titre de la section espèces")
                                            .list(ListElement.builder("species").describedAs("Les espèces du territoire")
                                                             .text("name", "Nom de l'espèce")
                                                             .multiLineText("description", "Description de l'espèce")
                                                             .image("image", "Image illustrant l'espèce")
                                            )
                     )
                     // Écosystèmes
                     .section(SectionElement.builder("ecosystems").describedAs("Écosystèmes")
                                            .text("title", "Titre de la section écosystèmes")
                                            .list(ListElement.builder("ecosystems").describedAs("Les écosystèmes du territoire")
                                                             .text("name", "Nom de l'écosystème")
                                                             .multiLineText("description", "Description de l'écosystème")
                                                             .image("image", "Image illustrant l'écosystème")
                                            )
                     )
                     // Frise chronologie
                     .section(SectionElement.builder("timeline").describedAs("Frise chronologique")
                                            .text("title", "Titre de la frise chronologique")
                                            .list(ListElement.builder("events").describedAs("Les événements de la frise")
                                                             .text("name", "Nom de l'événement")
                                                             .multiLineText("description", "Description de l'événement")
                                            )
                     )
                     // Risques et menaces
                     .section(SectionElement.builder("risks").describedAs("Risques et menaces")
                                            .text("title", "Titre de la section risques")
                                            .list(ListElement.builder("risks").describedAs("Les risques et menaces du territoire")
                                                             .text("name", "Nom du risque")
                                                             .multiLineText("description", "Description du risque")
                                                             .image("image", "Image illustrant le risque")
                                            )
                     )
                     .build();

    public static final String TERRITORY_HOME_PAGE_NAME = "Territories";
    public static final PageModel TERRITORY_HOME_PAGE_MODEL =
        PageModel.builder("territories").describedAs("Territoires")
                 .section(SectionElement
                              .builder("header").describedAs("En-tête")
                              .titleText("title", "Titre de la page")
                              .text("text", "Texte de l'en-tête")
                              .text("population", "Nombre d'habitants")
                              .text("species", "Nombre d'espèces indigènes")
                 )
                 .build();

    private TerritoryModel() {
    }
}
