package fr.mnhn.diversity.indicator;

import fr.mnhn.diversity.model.meta.ListElement;
import fr.mnhn.diversity.model.meta.PageModel;
import fr.mnhn.diversity.model.meta.SectionElement;
import java.util.Map;

/**
 * The model for the indicator page
 */
public final class IndicatorModel {

    public static final PageModel INDICATOR_PAGE_MODEL =
        PageModel.builder("indicator")
                 .describedAs("Indicator page")
                 .withPathFactory(slug -> "/indicateurs/" + slug)
                 // Présentation
                 .section(SectionElement.builder("presentation").describedAs("Présentation de l'indicateur")
                                        .text("description", "Description de l'indicateur - pages indicateurs")
                                        .text("descriptionTerritories", "Description de l'indicateur - pages territoires")
                                        .multiSizeImage("image", "Illustration de l'indicateur")
                                        .select("sourceSelect", "Choix du logo source", Map.of("onb", "ONB", "inpn", "INPN"))
                                        .text("logoUrl", "URL de redirection du logo")
                 )
                 // Comprendre l'indicateur
                 .section(SectionElement.builder("understand").describedAs("Comprendre l'indicateur")
                                        .multiSizeImage("image", "Illustration")
                                        .text("title1", "Titre du premier paragraphe")
                                        .multiLineText("text1", "Texte du premier paragraphe")
                                        .text("title2", "Titre du second paragraphe")
                                        .multiLineText("text2", "Texte du second paragraphe")
                                        .text("keyword", "Mot-clé affiché en filigrane")
                                        .list(ListElement.builder("paragraphs").describedAs("Paragraphes additionnels")
                                                         .text("title", "Titre")
                                                         .multiLineText("text", "Texte")
                                        )
                 )
                 // Indicateurs par territoire
                 .section(SectionElement.builder("territories").describedAs("Indicateur par territoires")
                                        .text("title", "Titre de la section indicateur par territoire")
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
                 .withPath("/indicateurs")
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
                .section(
                     SectionElement.builder("inpn").describedAs("Inventaire national du patrimoine naturel")
                                   .titleText("title", "Titre")
                                   .multiLineText("description", "Description de l'inventaire'")
                                   .link("link", "Lien vers l'inventaire")
                 )
                 .titleText("quote", "Citation")
                 .list(
                     ListElement.builder("questions").describedAs("Questions pour apprendre")
                                .text("question", "Texte de la question")
                                .multiLineText("answer", "Texte de la réponse")
                                .text("quote", "Citation")
                 )
                 .build();
    public static final String INDICATOR_HOME_PAGE_NAME = "indicateurs";

    private IndicatorModel() {
    }
}
