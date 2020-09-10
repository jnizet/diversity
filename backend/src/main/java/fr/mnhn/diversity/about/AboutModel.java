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
                                        .image("background", "Image de fond de l'en-tête")
                                        .titleText("title", "Titre de la page")
                                        .multiLineText("subtitle", "Sous-titre de la page")
                                        .list(ListElement.builder("paragraphs").describedAs("Les paragraphes de texte présentant le site")
                                                         .text("text")
                                        )
                 )
                 .list(ListElement.builder("carousel").describedAs("Carousel de présentation des pages")
                                  .text("title", "Titre du slide")
                                  .multiLineText("text", "Texte du slide")
                                  .link("link", "Lien vers la page décrite par le slide")
                                  .image("image", "Image du slide")
                 )
                 .section(SectionElement.builder("partners").describedAs("Section des partenaires")
                                        .text("title", "Titre de la section")
                                        .list(ListElement.builder("partners")
                                                         .describedAs("Liste des partenaires")
                                                         .image("logo", "Logo du partenaire")
                                        )
                 )
                 .build();

    private AboutModel() {
    }
}

