package fr.mnhn.diversity.act;

import fr.mnhn.diversity.model.meta.ListElement;
import fr.mnhn.diversity.model.meta.PageModel;
import fr.mnhn.diversity.model.meta.SectionElement;

/**
 * The models for the Act together and the participative science pages
 * @author JB Nizet
 */
public final class ActModel {

    public static final String ACT_PAGE_NAME = "agir-ensemble";
    public static final PageModel ACT_PAGE_MODEL =
        PageModel.builder("act")
                 .withPath("/agir-ensemble")
                 .section(SectionElement.builder("header").describedAs("En-tête")
                                        .multiSizeImage("background", "Image de fond de l'en-tête")
                                        .checkbox("shadowed", "Ajouter une ombre sur le coté gauche de la photo")
                                        .titleText("title", "Titre de la page")
                                        .multiLineText("subtitle", "Sous-titre de la page")
                 )
                 .section(SectionElement.builder("ecogestures").describedAs("Éco-gestes")
                                        .titleText("title", "Titre")
                                        .multiLineText("subtitle", "Sous-titre")
                 )
                 .section(SectionElement.builder("science").describedAs("Sciences participatives")
                                        .titleText("title", "Titre de la section")
                                        .text("subtitle", "Sous-titre de la section")
                                        .section(SectionElement.builder("project").describedAs("Projet présenté")
                                            .titleText("title", "Titre du projet")
                                            .multiLineText("description", "Description du projet")
                                            .multiSizeImage("image", "Image illustrant le projet")
                                        )
                 )
                 .build();

    public static final String SCIENCE_PAGE_NAME = "sciences-participatives";
    public static final PageModel SCIENCE_PAGE_MODEL =
        PageModel.builder("science")
                 .withPath("/sciences-participatives")
                 .section(SectionElement.builder("header").describedAs("En-tête")
                                        .multiSizeImage("background", "Image de fond de l'en-tête")
                                        .checkbox("shadowed", "Ajouter une ombre sur le coté gauche de la photo")
                                        .titleText("title", "Titre de la page")
                                        .multiLineText("subtitle", "Sous-titre de la page")
                 )
                 .section(SectionElement.builder("presentation").describedAs("Présentation")
                                        .titleText("title", "Titre")
                                        .multiLineText("description", "Description")
                 )
                 .list(ListElement.builder("paragraphs").describedAs("Paragraphes de la colonne de gauche")
                                        .titleText("title", "Titre")
                                        .multiLineText("text", "Texte")
                 )
                 .list(ListElement.builder("images").describedAs("Images de la colonne de droite")
                                  .image("image", "Image")
                 )
                 .section(SectionElement.builder("examples").describedAs("Exemples")
                                        .text("title", "Titre de la section")
                                        .list(ListElement.builder("projects").describedAs("Projets présentés")
                                                         .titleText("title", "Titre du projet")
                                                         .multiLineText("description", "Description du projet")
                                                         .link("more", "Lien 'En savoir plus'")
                                                         .multiSizeImage("image", "Image illustrant le projet")
                                                         .multiLineText("subject", "Objet de l'étude")
                                                         .multiLineText("actor", "Acteur")
                                                         .multiLineText("target", "public visé")
                                        )
                 )
                 .section(SectionElement.builder("application").describedAs("Application smartphone")
                                        .text("title", "Titre de la section")
                                        .text("subtitle", "Sous-titre de la section")
                                        .link("downloadLink", "Lien de téléchargement")
                 )
                 .build();

    private ActModel() {
    }
}

