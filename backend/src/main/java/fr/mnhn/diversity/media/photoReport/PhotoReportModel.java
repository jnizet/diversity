package fr.mnhn.diversity.media.photoReport;

import fr.mnhn.diversity.model.meta.ImageElement;
import fr.mnhn.diversity.model.meta.ListElement;
import fr.mnhn.diversity.model.meta.MultiListElement;
import fr.mnhn.diversity.model.meta.MultiListTemplateElement;
import fr.mnhn.diversity.model.meta.PageModel;
import fr.mnhn.diversity.model.meta.SectionElement;
import java.util.List;

/**
 * The model for photo report Model.
 * @author JB Nizet
 */
public final class PhotoReportModel {
    /**
     * The model of a photo report.
     */
    public static final String PHOTO_REPORT_PAGE_NAME = "report";
    public static final PageModel PHOTO_REPORT_PAGE_MODEL =
        PageModel.builder("report")
            .withPathFactory(slug -> "/report/" + slug)
            .section(SectionElement
                .builder("presentation")
                .describedAs("Présentation du reportage photo")
                .titleText("name", "Titre principal")
                .text("date", "Date de publication (format: JJ/MM/YYYY)")
                .image("image", "Image d’illustration")
                .checkbox("shadowed", "Ajouter une ombre sur le coté gauche de la photo")
            )
            .section(SectionElement
                .builder("photograph")
                .describedAs("Présentation du photograph")
                .image("photo", "Uploader la photo")
                .text("presentation", "Texte de présentation (sous la photo)")
            )
            .section(SectionElement
                .builder("body")
                .describedAs("Corps du reportage photo")
                .multiLineText("introduction", "Introduction du reportage photo")
                .list( ListElement.builder("photos").describedAs("photos")
                    .image("photo", "La photo")
                    .multiLineText("description", "Brève description de la photo")
                )
            )
            .build();

    private PhotoReportModel() {
    }
}
