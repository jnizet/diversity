package fr.mnhn.diversity.ecogesture;

import fr.mnhn.diversity.model.meta.ListElement;
import fr.mnhn.diversity.model.meta.PageModel;
import fr.mnhn.diversity.model.meta.SectionElement;

/**
 * The model for eco-gestures.
 * @author JB Nizet
 */
public final class EcogestureModel {

    /**
     * The model of an ecogesture. This model has several (8 initially) instances in database, and more can be created.
     * These eco-gesture models are displayed one by one on the eco-gesture details page, but parts of them are also
     * displayed in the eco-gestures home page.
     */
    public static final PageModel ECO_GESTURE_PAGE_MODEL =
        PageModel.builder("ecogesture")
                 .withPathFactory(slug -> "/ecogestes/" + slug)
                 .section(SectionElement
                              .builder("presentation")
                              .describedAs("Présentation de l'écogeste, et informations affichées sur sa vignette dans la page d'accueil des écogestes")
                              .titleText("name", "Nom de l'écogeste")
                              .text("category", "Catégorie de l'écogeste")
                              .text("description", "Description de l'écogeste")
                              .image("image", "Illustration de l'écogeste")
                              .document("file", "Fiche technique de l'écogeste")
                 )
                 .section(SectionElement
                              .builder("understand")
                              .describedAs("Comprendre l'écogeste")
                              .text("title", "Titre de la rubrique")
                              .multiLineText("text", "Texte de la rubrique")
                              .text("quote", "Citation de la rubrique")
                 )
                 .section(SectionElement
                              .builder("action")
                              .describedAs("Écogeste en actions")
                              .text("title", "Titre de la rubrique")
                              .list(
                                  ListElement.builder("cards").describedAs("Vignettes")
                                      .image("icon", "Icône de la vignette")
                                      .multiLineText("description", "Icône de la vignette")
                              )
                 )
                 .build();

    /**
     * The model of the ecogestures home page. To display this page, this model is not sufficient: it also displays
     * one card per ecogesture, and these cards are created using the {@link #ECO_GESTURE_PAGE_MODEL}
     */
    public static final PageModel ECO_GESTURE_HOME_PAGE_MODEL =
        PageModel.builder("ecogestures")
                 .withPath("/ecogestes")
                 .text("title", "Titre de la page")
                 .multiLineText("presentation", "Présentation des écogestes")
                 .image("image", "Illustration")
                 .text("question", "Question de présentation")
                 .text("answer", "Réponse")
                 .text("quote", "Citation pour agir")
                 .section(
                     SectionElement.builder("other").describedAs("Autres gestes")
                                   .text("title", "Titre")
                                   .text("text", "Description")
                                   .image("image", "Illustration")
                 )
                 .build();
    public static final String ECO_GESTURE_HOME_PAGE_NAME = "ecogestes";

    private EcogestureModel() {
    }
}
