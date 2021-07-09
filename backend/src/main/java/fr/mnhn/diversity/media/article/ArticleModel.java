package fr.mnhn.diversity.media.article;

import fr.mnhn.diversity.model.meta.MultiListElement;
import fr.mnhn.diversity.model.meta.MultiListTemplateElement;
import fr.mnhn.diversity.model.meta.PageModel;
import fr.mnhn.diversity.model.meta.SectionElement;
import java.util.List;

/**
 * The model for article Model.
 * @author JB Nizet
 */
public final class ArticleModel {

    /**
     * The model of an article. This model is used for interviews and articles.
     */
    public static final String ARTICLE_PAGE_NAME = "article";
    public static final PageModel ARTICLE_PAGE_MODEL =
        PageModel.builder("article")
            .withPathFactory(slug -> "/article/" + slug)
            .section(SectionElement
                .builder("presentation")
                .describedAs("Présentation de l’article")
                .titleText("name", "Titre principal")
                .text("author", "Date de publication")
                .text("publication", "Description de l'écogeste")
                .image("image", "Image d’illustration")
                .checkbox("shadowed", "Ajouter une ombre sur le coté gauche de la photo")
            )
            .section(SectionElement
                .builder("body")
                .describedAs("Corps de l'article")
                .text("introduction", " Introduction de l’article")
                .list(
                    MultiListElement.builder("titre_question")
                        .templates( List.of(
                            MultiListTemplateElement.builder("questionAnswer").describedAs("un bloc question/réponse")
                                .titleText("question", "Question")
                                .multiLineText("response", "Réponse"),
                            MultiListTemplateElement.builder("paragraph").describedAs("un paragraphe")
                                .multiLineText("paragraphText", "Paragraphe"),
                            MultiListTemplateElement.builder("image").describedAs("une image")
                                .multiSizeImage("imageElement", "image_article"),
                            MultiListTemplateElement.builder("citation").describedAs("une citation")
                                .multiLineText("citationElement", "Citation")
                        ))
                        .describedAs("Ajouter un élément")
                )
            )
            .build();

    private ArticleModel() {
    }
}
