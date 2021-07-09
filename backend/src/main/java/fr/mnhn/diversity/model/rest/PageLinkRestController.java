package fr.mnhn.diversity.model.rest;

import fr.mnhn.diversity.media.article.ArticleModel;
import fr.mnhn.diversity.media.interview.InterviewModel;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import fr.mnhn.diversity.about.AboutModel;
import fr.mnhn.diversity.act.ActModel;
import fr.mnhn.diversity.ecogesture.EcogestureActSectionModel;
import fr.mnhn.diversity.ecogesture.EcogestureModel;
import fr.mnhn.diversity.ecogesture.EcogestureRepository;
import fr.mnhn.diversity.home.HomeModel;
import fr.mnhn.diversity.indicator.IndicatorModel;
import fr.mnhn.diversity.indicator.IndicatorRepository;
import fr.mnhn.diversity.legal.LegalTermsModel;
import fr.mnhn.diversity.model.BasicPage;
import fr.mnhn.diversity.model.PageRepository;
import fr.mnhn.diversity.model.meta.PageModel;
import fr.mnhn.diversity.territory.Territory;
import fr.mnhn.diversity.territory.TerritoryModel;
import fr.mnhn.diversity.territory.Zone;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional
@RequestMapping("/api/pages/links")
public class PageLinkRestController {

    private final PageRepository pageRepository;
    private final IndicatorRepository indicatorRepository;
    private final EcogestureRepository ecogestureRepository;

    public PageLinkRestController(PageRepository pageRepository,
                                  IndicatorRepository indicatorRepository,
                                  EcogestureRepository ecogestureRepository) {
        this.pageRepository = pageRepository;
        this.indicatorRepository = indicatorRepository;
        this.ecogestureRepository = ecogestureRepository;
    }

    @GetMapping
    public PageLinksDTO pageLinks() {
        return new PageLinksDTO(
            getStaticLinks(),
            getIndicatorLinks(),
            getTerritoryLinks(),
            getZoneLinks(),
            getInterviewLinks(),
            getArticleLinks(),
            getEcogestureLinks()
        );
    }

    private List<PageLinkDTO> getStaticLinks() {
        return List.of(
            getStaticLink(HomeModel.HOME_PAGE_NAME, HomeModel.HOME_PAGE_MODEL),
            getStaticLink(AboutModel.ABOUT_PAGE_NAME, AboutModel.ABOUT_PAGE_MODEL),
            getStaticLink(TerritoryModel.TERRITORY_HOME_PAGE_NAME, TerritoryModel.TERRITORY_HOME_PAGE_MODEL),
            getStaticLink(IndicatorModel.INDICATOR_HOME_PAGE_NAME, IndicatorModel.INDICATOR_HOME_PAGE_MODEL),
            getStaticLink(EcogestureModel.ECO_GESTURE_HOME_PAGE_NAME, EcogestureModel.ECO_GESTURE_HOME_PAGE_MODEL),
            getStaticLink(EcogestureActSectionModel.ECOGESTURE_ACT_SECTION_NAME, EcogestureActSectionModel.ECOGESTURE_ACT_SECTION_MODEL),
            getStaticLink(ActModel.ACT_PAGE_NAME, ActModel.ACT_PAGE_MODEL),
            getStaticLink(ActModel.SCIENCE_PAGE_NAME, ActModel.SCIENCE_PAGE_MODEL),
            getStaticLink(LegalTermsModel.LEGAL_TERMS_PAGE_NAME, LegalTermsModel.LEGAL_TERMS_PAGE_MODEL)
        );
    }

    private List<PageLinkDTO> getIndicatorLinks() {
        PageModel pageModel = IndicatorModel.INDICATOR_PAGE_MODEL;
        Map<String, BasicPage> pagesByName = getPageByName(pageModel);
        return indicatorRepository.list()
            .stream()
            .map(indicator -> getLink(pagesByName.get(indicator.getSlug()),
                                      indicator.getSlug(),
                                      pageModel))
            .collect(Collectors.toList());
    }

    private List<PageLinkDTO> getTerritoryLinks() {
        PageModel pageModel = TerritoryModel.TERRITORY_PAGE_MODEL;
        Map<String, BasicPage> pagesByName = getPageByName(pageModel);
        return EnumSet.complementOf(EnumSet.of(Territory.OUTRE_MER))
                      .stream()
                      .map(territory -> getLink(pagesByName.get(territory.getSlug()),
                                                territory.getSlug(),
                                                pageModel))
                      .collect(Collectors.toList());
    }

    private List<PageLinkDTO> getInterviewLinks() {
        PageModel pageModel = InterviewModel.INTERVIEW_PAGE_MODEL;
        Map<String, BasicPage> pagesByName = getPageByName(pageModel);
        return pagesByName.entrySet().stream()
            .map(page -> getLink(page.getValue(),
                page.getKey(),
                pageModel))
            .collect(Collectors.toList());
    }

    private List<PageLinkDTO> getArticleLinks() {
        PageModel pageModel = ArticleModel.ARTICLE_PAGE_MODEL;
        Map<String, BasicPage> pagesByName = getPageByName(pageModel);
        return pagesByName.entrySet().stream()
            .map(page -> getLink(page.getValue(),
                page.getKey(),
                pageModel))
            .collect(Collectors.toList());
    }

    private List<PageLinkDTO> getZoneLinks() {
        PageModel pageModel = TerritoryModel.ZONE_PAGE_MODEL;
        Map<String, BasicPage> pagesByName = getPageByName(pageModel);
        return EnumSet.allOf(Zone.class)
                   .stream()
                   .map(zone -> getLink(pagesByName.get(zone.getSlug()),
                                        zone.getSlug(),
                                        pageModel))
            .collect(Collectors.toList());
    }

    private List<PageLinkDTO> getEcogestureLinks() {
        PageModel pageModel = EcogestureModel.ECO_GESTURE_PAGE_MODEL;
        Map<String, BasicPage> pagesByName = getPageByName(pageModel);
        return ecogestureRepository.list()
                                  .stream()
                                  .map(ecogesture -> getLink(pagesByName.get(ecogesture.getSlug()),
                                                             ecogesture.getSlug(),
                                                             pageModel))
                                  .collect(Collectors.toList());
    }

    private Map<String, BasicPage> getPageByName(PageModel pageModel) {
        return pageRepository.findBasicByModel(pageModel.getName())
                             .stream()
                             .collect(Collectors.toMap(BasicPage::getName, Function.identity()));
    }

    private PageLinkDTO getStaticLink(String pageName, PageModel pageModel) {
        BasicPage page = pageRepository.findBasicByNameAndModel(pageName, pageModel.getName()).orElse(null);
        return getLink(page, pageName, pageModel);
    }

    private PageLinkDTO getLink(BasicPage page, String pageName, PageModel pageModel) {
        if (page == null) {
            return new PageLinkDTO(pageName, pageModel.getName());
        } else {
            return new PageLinkDTO(page);
        }
    }
}
