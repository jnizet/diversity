package fr.mnhn.diversity.home;

import fr.mnhn.diversity.model.meta.ListElement;
import fr.mnhn.diversity.model.meta.PageModel;
import fr.mnhn.diversity.model.meta.SectionElement;

/**
 * The model for the home page
 * @author JB Nizet
 */
public final class HomeModel {

    public static final String HOME_PAGE_NAME = "accueil";
    public static final PageModel HOME_PAGE_MODEL =
        PageModel.builder("home")
                 .withPath("/")
                 .section(SectionElement.builder("carousel").describedAs("Carousel")
                     .list(ListElement.builder("carouselElements").describedAs("Les slides affichées par le carousel")
                         .text("title", "Titre du carousel, affiché sur cette slide")
                         .multiLineText("text", "Texte du carousel, affiché sur cette slide")
                         .multiSizeImage("image", "Photo")
                         .checkbox("shadowed", "Ajouter une ombre sur le coté gauche de la photo")
                         .link("button", "Bouton de la slide") // internal and thus hard-coded href
                     )
                 )
                 .section(SectionElement.builder("presentation").describedAs("Section présentation")
                     .text("title", "Titre de la section")
                     .multiLineText("text", "Texte de la présentation du site")
                     .text("indicators", "Courte présentation des indicateurs")
                     .text("ecogestures", "Courte présentation des éco-gestes")
                     .text("science", "Courte présentation des sciences participatives")
                     .text("territories", "Courte présentation des territoires")
                     .text("quote", "Citation")
                 )
                 .section(SectionElement.builder("testimony").describedAs("Section témoignage")
                     .text("title", "Titre de la section")
                     .multiLineText("text", "Texte introductif")
                     .multiLineText("quote", "Citation")
                     .multiSizeImage("image", "Témoin")
                 )
                 .section(SectionElement.builder("science").describedAs("Section sciences participatives")
                     .text("title", "Titre de la section")
                     .multiLineText("text", "Texte introductif")
                     .multiSizeImage("image", "Illustration")
                     .link("more", "Bouton de la slide")
                 )
                 .build();

    private HomeModel() {
    }
}
