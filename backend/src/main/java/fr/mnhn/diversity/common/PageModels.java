package fr.mnhn.diversity.common;

import fr.mnhn.diversity.media.article.ArticleModel;
import fr.mnhn.diversity.media.interview.InterviewModel;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import fr.mnhn.diversity.about.AboutModel;
import fr.mnhn.diversity.act.ActModel;
import fr.mnhn.diversity.ecogesture.EcogestureActSectionModel;
import fr.mnhn.diversity.ecogesture.EcogestureModel;
import fr.mnhn.diversity.home.HomeModel;
import fr.mnhn.diversity.indicator.IndicatorModel;
import fr.mnhn.diversity.legal.LegalTermsModel;
import fr.mnhn.diversity.model.meta.PageModel;
import fr.mnhn.diversity.territory.TerritoryModel;

/**
 * A utility class containing all page models, useful for global page controllers such as search,
 * page, etc.
 */
public final class PageModels {
    public static final List<PageModel> ALL_PAGE_MODELS =
        List.of(HomeModel.HOME_PAGE_MODEL,
                AboutModel.ABOUT_PAGE_MODEL,
                ActModel.ACT_PAGE_MODEL,
                ActModel.SCIENCE_PAGE_MODEL,
                LegalTermsModel.LEGAL_TERMS_PAGE_MODEL,
                TerritoryModel.TERRITORY_HOME_PAGE_MODEL,
                TerritoryModel.TERRITORY_PAGE_MODEL,
                TerritoryModel.ZONE_PAGE_MODEL,
                IndicatorModel.INDICATOR_HOME_PAGE_MODEL,
                IndicatorModel.INDICATOR_PAGE_MODEL,
                EcogestureModel.ECO_GESTURE_HOME_PAGE_MODEL,
                EcogestureModel.ECO_GESTURE_PAGE_MODEL,
                EcogestureActSectionModel.ECOGESTURE_ACT_SECTION_MODEL,
                InterviewModel.INTERVIEW_PAGE_MODEL,
                ArticleModel.ARTICLE_PAGE_MODEL
        );

    public static final Map<String, PageModel> ALL_PAGE_MODELS_BY_NAME =
        ALL_PAGE_MODELS.stream().collect(Collectors.toUnmodifiableMap(PageModel::getName, Function.identity()));

    private PageModels() {
    }
}
