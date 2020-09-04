package fr.mnhn.diversity.web.home;

import fr.mnhn.diversity.web.model.ListElement;
import fr.mnhn.diversity.web.model.PageModel;
import fr.mnhn.diversity.web.model.SectionElement;
import fr.mnhn.diversity.web.model.TextElement;

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
                     .text(TextElement.builder("text").describedAs("Texte du carousel, affiché sur chaque slide").multiLine())
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
                     .text(TextElement.builder("text").describedAs("Texte introductif").multiLine())
                     .image("image")
                 )
                 .section(SectionElement.builder("science").describedAs("Section sciences participatives")
                     .text("title", "Titre de la section")
                     .text(TextElement.builder("text").describedAs("Texte introductif").multiLine())
                     .image("image")
                 )
                 .build();

    private HomeModel() {
    }
}
