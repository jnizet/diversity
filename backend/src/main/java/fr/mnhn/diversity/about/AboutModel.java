package fr.mnhn.diversity.about;

import fr.mnhn.diversity.model.meta.ListElement;
import fr.mnhn.diversity.model.meta.PageModel;
import fr.mnhn.diversity.model.meta.SectionElement;

/**
 * The model for the about page (A propos)
 * @author JB Nizet
 */
public final class AboutModel {

    public static final String ABOUT_PAGE_NAME = "About";
    public static final PageModel ABOUT_PAGE_MODEL =
        PageModel.builder("about")
                 .section(SectionElement.builder("header").describedAs("En-tête")
                                        .multiSizeImage("background", "Image de fond de l'en-tête")
                                        .titleText("title", "Titre de la page")
                                        .multiLineText("subtitle", "Sous-titre de la page")
                 )
                 .section(SectionElement.builder("goal1").describedAs("Objectif 1 (indicateurs)")
                                        .titleText("title", "Titre de l'objectif 1")
                                        .text("description", "Description de l'objectif 1")
                                        .multiSizeImage("image", "Image de l'objectif 1")
                 )
                 .section(SectionElement.builder("goal2").describedAs("Objectif 2 (territoires)")
                                        .titleText("title", "Titre de l'objectif 2")
                                        .text("description", "Description de l'objectif 2")
                                        .multiSizeImage("image", "Image de l'objectif 2")
                                        .text("quote", "Citation")
                                        .multiSizeImage("quoteImage", "Image illustrant la citation")
                 )
                 .section(SectionElement.builder("goal3").describedAs("Objectif 3 (agir ensemble)")
                                        .titleText("title", "Titre de l'objectif 3")
                                        .text("description", "Description de l'objectif 3")
                                        .multiSizeImage("image", "Image de l'objectif 3")
                 )
                 .section(SectionElement.builder("partners").describedAs("Section des partenaires")
                                        .text("title", "Titre de la section")
                                        .list(ListElement.builder("partners")
                                                         .describedAs("Liste des partenaires")
                                                         .image("logo", "Logo du partenaire")
                                                         .text("url", "URL du partenaire")
                                        )
                 )
                 .build();

    private AboutModel() {
    }
}

