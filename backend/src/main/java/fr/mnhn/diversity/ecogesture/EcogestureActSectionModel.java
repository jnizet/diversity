package fr.mnhn.diversity.ecogesture;

import fr.mnhn.diversity.model.meta.PageModel;

/**
 * The models for the Act section in the Ecogesture page.
 * It is extracted as a standalone page, as it is repeated in every ecogesture page with the same content.
 * It can then be configured only once.
 */
public class EcogestureActSectionModel {

    public static final String ECOGESTURE_ACT_SECTION_NAME = "ecogeste-agir";
    public static final PageModel ECOGESTURE_ACT_SECTION_MODEL =
        PageModel.builder("ecogesture-act")
                 .withPath(null)
                 .titleText("title", "Titre de la section 'Agir pour la science'")
                 .multiLineText("description", "Description de la section 'Agir pour la science'")
                 .text("firstActionName", "Nom de la première action")
                 .link("firstActionLink", "Lien de la première action")
                 .text("secondActionName", "Nom de la seconde action")
                 .link("secondActionLink", "Lien de la seconde action")
                 .build();
}
