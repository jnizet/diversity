package fr.mnhn.diversity.indicator;

import fr.mnhn.diversity.model.meta.ListElement;
import fr.mnhn.diversity.model.meta.PageModel;
import fr.mnhn.diversity.model.meta.SectionElement;

/**
 * The model for the indicator page
 */
public final class IndicatorModel {

    public static final PageModel INDICATOR_PAGE_MODEL =
        PageModel.builder("indicator")
                 .describedAs("Indicator page")
                 .titleText("name", "Nom de l'indicateur")
                 // Présentation
                 .section(SectionElement.builder("presentation").describedAs("Présentation de l'indicateur")
                                        .text("description", "Description de l'indicateur")
                                        .image("image", "Illustration de l'indicateur")
                 )
                 // Comprendre l'indicateur
                 .section(SectionElement.builder("understand").describedAs("Comprendre l'indicateur")
                                        .text("title", "Titre")
                                        .image("image", "Illustration")
                                        .list(ListElement.builder("sections").describedAs("Sous-sections de l'explication")
                                                         .text("title", "Titre de la sous-section")
                                                         .text("description", "Description de la sous-section")
                                        )
                 )
                 // Indicateurs par territoire
                 .section(SectionElement.builder("indicators").describedAs("Indicateurs par territoires")
                                        .text("title", "Titre de la section indicateurs")
                 )
                 // Écogestes
                 .section(SectionElement.builder("ecogestures").describedAs("Écogestes")
                                        .text("title", "Titre de la section écogestes")
                 )
                 .build();

    /**
     * The model of the indicator home page. To display this page, this model is not sufficient: it also displays
     * indicator cards and their categories, and these cards are created using the {@link #INDICATOR_PAGE_MODEL}
     */
    public static final PageModel INDICATOR_HOME_PAGE_MODEL =
        PageModel.builder("indicators")
                 .text("title", "Titre de la page")
                 .multiLineText("presentation", "Présentation des indicateurs")
                 .image("image", "Illustration")
                 .section(
                     SectionElement.builder("understand").describedAs("Comprendre")
                                   .titleText("title", "Titre de la section")
                                   .multiLineText("description", "Description de la section")
                                   .image("image", "Illustration")
                 )
                 .section(
                     SectionElement.builder("onb").describedAs("Observatoire national de la biodiversité")
                                   .titleText("title", "Titre")
                                   .multiLineText("description", "Description de l'observatoire'")
                                   .link("link", "Lien vers l'observatoire")
                 )
                 .titleText("quote", "Citation")
                 .list(
                     ListElement.builder("questions").describedAs("Questions pour apprendre")
                                .text("question", "Texte de la question")
                                .text("answer", "Texte de la réponse")
                                .text("quote", "Citation")
                 )
                 .build();
    public static final String INDICATOR_HOME_PAGE_NAME = "IndicatorHome";

    private IndicatorModel() {
    }
}
