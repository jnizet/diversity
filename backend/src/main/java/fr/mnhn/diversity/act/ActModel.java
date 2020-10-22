package fr.mnhn.diversity.act;

import fr.mnhn.diversity.model.meta.ListElement;
import fr.mnhn.diversity.model.meta.PageModel;
import fr.mnhn.diversity.model.meta.SectionElement;

/**
 * The model for the about page (A propos)
 * @author JB Nizet
 */
public final class ActModel {

    public static final String ACT_PAGE_NAME = "Act";
    public static final PageModel ACT_PAGE_MODEL =
        PageModel.builder("act")
                 .section(SectionElement.builder("header").describedAs("En-tête")
                                        .image("background", "Image de fond de l'en-tête")
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

    private ActModel() {
    }
}

