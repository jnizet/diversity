package fr.mnhn.diversity.media.article;

import fr.mnhn.diversity.model.meta.MultiListElement;
import fr.mnhn.diversity.model.meta.PageModel;
import fr.mnhn.diversity.model.meta.SectionElement;

/**
 * The model for article Model.
 * @author JB Nizet
 */
public final class ArticleModel {

    public static final String ARTICLE_PAGE_NAME = "article";

    /**
     * The model of an article.
     */
    public static final PageModel ARTICLE_PAGE_MODEL =
        PageModel.builder("article")
            .withPathFactory(slug -> "/article/" + slug)
            .section(SectionElement
                .builder("presentation")
                .describedAs("Présentation de l’article")
                .titleText("name", "Titre principal")
                .text("author", "Nom de l'auteur")
                .text("date", "Date de publication (format JJ/MM/YYYY)")
                .image("image", "Image d’illustration")
                .checkbox("shadowed", "Ajouter une ombre sur le coté gauche de la photo")
            )
            .section(SectionElement
                .builder("body")
                .describedAs("Corps de l'article")
                .multiLineText("introduction", " Introduction de l’article")
                .list(
                    MultiListElement
                        .builder("titre_question")
                        .describedAs("Ajouter un élément")
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

    private ArticleModel() {
    }
}
