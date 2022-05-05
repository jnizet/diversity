package fr.mnhn.diversity.media.photoreport;

import fr.mnhn.diversity.model.meta.ListElement;
import fr.mnhn.diversity.model.meta.PageModel;
import fr.mnhn.diversity.model.meta.SectionElement;

/**
 * The model for photo report Model.
 * @author JB Nizet
 */
public final class PhotoReportModel {
    public static final String PHOTO_REPORT_PAGE_NAME = "report";

    /**
     * The model of a photo report.
     */
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
                .describedAs("Présentation du photographe")
                .image("photo", "Photo du photographe")
                .text("presentation", "Présentation du photographe (sous la photo)")
            )
            .section(SectionElement
                .builder("body")
                .describedAs("Corps du reportage photo")
                .multiLineText("introduction", "Introduction du reportage photo")
                .list(ListElement.builder("photos").describedAs("photos")
                    .image("photo", "La photo")
                    .multiLineText("description", "Brève description de la photo")
                )
            )
            .build();

    private PhotoReportModel() {
    }
}
