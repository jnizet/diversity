package fr.mnhn.diversity.media;

import fr.mnhn.diversity.model.meta.PageModel;
import fr.mnhn.diversity.model.meta.SectionElement;

public class MediaModel {
    public static final String MEDIA_PAGE_NAME = "media";
    public static final PageModel MEDIA_PAGE_MODEL =
        PageModel.builder("Accueil médias")
            .withPath("/media")
            .section(SectionElement.builder("article").describedAs("Article à la une")
                .text("title", "Titre de l’article : input pour saisir le titre de l’article (peut être différent du titre sur la page de l’article)")
                .text("title", "Titre de l’article : input pour saisir le titre de l’article (peut être différent du titre sur la page de l’article)")
                .text("author", "Auteur")
                .text("date", "Date de publication")
                .link("button", "Bouton")
                .multiSizeImage("image", "Image de présentation")
            )
            .build();

    private MediaModel(){

    }
}
