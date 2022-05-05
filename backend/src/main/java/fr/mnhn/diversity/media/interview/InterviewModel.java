package fr.mnhn.diversity.media.interview;

import fr.mnhn.diversity.model.meta.ListElement;
import fr.mnhn.diversity.model.meta.MultiListElement;
import fr.mnhn.diversity.model.meta.PageModel;
import fr.mnhn.diversity.model.meta.SectionElement;
import java.util.List;

/**
 * The model for interview Model.
 * @author JB Nizet
 */
public final class InterviewModel {

    public static final String INTERVIEW_PAGE_NAME = "interview";

    /**
     * The model of an Interview.
     */
    public static final PageModel INTERVIEW_PAGE_MODEL =
        PageModel.builder("interview")
            .withPathFactory(slug -> "/interview/" + slug)
            .section(SectionElement
                .builder("presentation")
                .describedAs("Présentation de l’entretien")
                .titleText("name", "Titre principal")
                .text("author", "Auteur")
                .text("date", "Date de publication (format JJ/MM/YYYY)")
                .image("image", "Image d’illustration")
                .checkbox("shadowed", "Ajouter une ombre sur le coté gauche de la photo")
            )
            .section(SectionElement
                .builder("interviewee")
                .describedAs("Présentation de l'interviewé")
                .image("photo", "Uploader la photo")
                .text("presentation", "Texte de présentation (sous la photo)")
            )
            .section(SectionElement
                .builder("body")
                .describedAs("Corps de l'interview")
                .multiLineText("introduction", " Introduction de l’entretien")
                .list(
                    MultiListElement
                        .builder("bodyElements")
                        .describedAs("Éléments de l'article")
                        .template(
                            SectionElement.builder("questionAnswer").describedAs("un bloc question/réponse")
                                .titleText("question", "Question")
                                .multiLineText("response", "Réponse")
                        )
                        .template(
                            SectionElement.builder("paragraph").describedAs("un paragraphe")
                                .multiLineText("paragraphText", "Paragraphe")
                        )
                        .template(
                            SectionElement.builder("image").describedAs("une image")
                                .multiSizeImage("imageElement", "Image")
                        )
                        .template(
                            SectionElement.builder("citation").describedAs("une citation")
                                .multiLineText("citationElement", "Citation")
                        ))
            )
            .build();

    private InterviewModel() {
    }
}
