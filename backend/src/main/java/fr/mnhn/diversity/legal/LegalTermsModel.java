package fr.mnhn.diversity.legal;

import fr.mnhn.diversity.model.meta.ListElement;
import fr.mnhn.diversity.model.meta.PageModel;

public class LegalTermsModel {
    public static final String LEGAL_TERMS_PAGE_NAME = "mentions-legales";
    public static final PageModel LEGAL_TERMS_PAGE_MODEL =
        PageModel.builder("legal-terms")
                 .withPath("/mentions-legales")
                 .titleText("title", "Titre")
                 .list(ListElement.builder("paragraphs").describedAs("Paragraphes")
                                  .text("title", "Titre du paragraphe")
                                  .multiLineText("text", "Texte du paragraphe")
                 )
                 .build();

    private LegalTermsModel() {
    }
}
