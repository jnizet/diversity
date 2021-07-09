package fr.mnhn.diversity.media.interview;

import fr.mnhn.diversity.model.meta.ListElement;
import fr.mnhn.diversity.model.meta.PageModel;
import fr.mnhn.diversity.model.meta.SectionElement;

/**
 * The model for report Model.
 * @author JB Nizet
 */
public final class ReportModel {

    /**
     * The model of an Report. This model is used for interviews and articles.
     */
    public static final String REPORT_PAGE_NAME = "Media";
    public static final PageModel REPORT_PAGE_MODEL =
        PageModel.builder("report")
            .withPathFactory(slug -> "/report/" + slug)
            .section(SectionElement
                .builder("presentation")
                .describedAs("Présentation de l’article/entretien")
                .titleText("name", "Titre principal")
                .text("author", "Auteur")
                .text("publication", "Description de l'écogeste")
                .image("image", "Illustration de l'écogeste")
                .checkbox("shadowed", "Ajouter une ombre sur le coté gauche de la photo")
            )
            .section(SectionElement
                .builder("body")
                .describedAs("Corps de l'article")
                .text("introduction", " Introduction de l’article/entretien")
                .list(
                    ListElement.builder("titre_question").describedAs("Ajouter un titre/question")
                        .text("question", "Question")
                        .text("response", "Réponse")
                )
            )
            .build();

    private ReportModel() {
    }
}
