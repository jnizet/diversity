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
                     // TODO: indicators value are stored as text for now
                     .section(SectionElement.builder("indicator")
                                            .section(SectionElement.builder("OUTRE_MER")
                                                                   .text("value", "Valeur de l'indicateur")
                                            )
                                            .section(SectionElement.builder("REUNION")
                                                                   .text("value", "Valeur de l'indicateur")
                                            )
                                            .section(SectionElement.builder("GUADELOUPE")
                                                                   .text("value", "Valeur de l'indicateur")
                                            )
                                            .section(SectionElement.builder("SAINT_PIERRE_ET_MIQUELON")
                                                                   .text("value", "Valeur de l'indicateur")
                                            )
                     )
                     // Présentation
                     .section(SectionElement.builder("presentation").describedAs("Présentation de l'indicateur")
                                            .text("category", "Catégorie de l'indicateur")
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
                                            .list(ListElement.builder("indicators").describedAs("Les indicateurs du territoire")
                                                             .text("name", "Nom du territoire")
                                                             // TODO: indicators value are stored as text for now
                                                             .text("value", "Valeur de l'indicateur")
                                                             .image("image", "Image illustrant le territoire")
                                            )
                     )
                     // Écogestes
                     .section(SectionElement.builder("ecogestures").describedAs("Écogestes")
                                            .text("title", "Titre de la section écogestes")
                                            .list(ListElement.builder("ecogestures").describedAs("Les écogestes du territoire")
                                                             .text("name", "Nom de l'écogeste")
                                                             .text("description", "Description de l'écogeste")
                                                             .text("category", "Catégorie de l'écogeste")
                                                             .image("image", "Image illustrant l'écogeste")
                                                             .link("link", "Lien vers l'écogeste")
                                            )
                     )
                     // Bandeau indicateur suivant
                     .section(SectionElement.builder("next").describedAs("Bandeau indicateur suivant")
                                            .text("name", "Nom de l'indicateur suivant")
                                            .image("image", "Image illustrant l'indicateur suivant")
                                            .link("link", "Lien vers l'indicateur suivant")
                     )
                     .build();

    /**
     * The model of the indicator home page. To display this page, this model is not sufficient: it also displays
     * indicator cards and their categories, and these cards are created using the {@link #INDICATOR_PAGE_MODEL}
     */
    public static final PageModel INDICATOR_HOME_PAGE_MODEL =
        // TODO this model is far from being complete. But we will wait until we have the actual page to decide what to add
        PageModel.builder("indicators")
                 .text("title", "Titre de la page")
                 .multiLineText("presentation", "Présentation des indicateurs")
                 .image("image", "Illustration")
                 .build();
    public static final String INDICATOR_HOME_PAGE_NAME = "IndicatorHome";

    private IndicatorModel() {
    }
}
