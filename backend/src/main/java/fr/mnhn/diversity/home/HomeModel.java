package fr.mnhn.diversity.home;

import fr.mnhn.diversity.model.meta.ListElement;
import fr.mnhn.diversity.model.meta.PageModel;
import fr.mnhn.diversity.model.meta.SectionElement;

/**
 * The model for the home page
 * @author JB Nizet
 */
public final class HomeModel {

    public static final String HOME_PAGE_NAME = "Home";
    public static final PageModel HOME_PAGE_MODEL =
        PageModel.builder("home")
                 .section(SectionElement.builder("carousel").describedAs("Carousel")
                     .text("title", "Titre du carousel, affiché sur chaque slide")
                     .multiLineText("text", "Texte du carousel, affiché sur chaque slide")
                     .list(ListElement.builder("images").describedAs("Les photos affichées par le carousel")
                         .image("image")
                     )
                     .text("territoriesButton", "Bouton découvrir les territoires") // internal and thus hard-coded href
                 )
                 .section(SectionElement.builder("presentation").describedAs("Section présentation")
                     .text("title", "Titre de la section")
                     .text("text", "Texte de la présentation du site")
                     .image("image", "Image affichée pour présenter le site")
                     .text("more", "Lien pour en savoir plus") // internal and thus hard-coded href
                 )
                 .section(SectionElement.builder("indicators").describedAs("Section indicateurs")
                     .text("title", "Titre de la section")
                     .multiLineText("text", "Texte introductif")
                     .image("image")
                 )
                 .section(SectionElement.builder("science").describedAs("Section sciences participatives")
                     .text("title", "Titre de la section")
                     .multiLineText("text", "Texte introductif")
                     .image("image")
                 )
                 .build();

    private HomeModel() {
    }
}
