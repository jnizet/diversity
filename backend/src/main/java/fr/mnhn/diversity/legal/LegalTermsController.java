package fr.mnhn.diversity.legal;

import fr.mnhn.diversity.common.exception.NotFoundException;
import fr.mnhn.diversity.home.HomeModel;
import fr.mnhn.diversity.model.Page;
import fr.mnhn.diversity.model.PageRepository;
import fr.mnhn.diversity.model.PageService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * The Controller for the legal terms page
 * @author JB Nizet
 */
@Controller
@RequestMapping("/mentions-legales")
@Transactional
public class LegalTermsController {

    private final PageRepository pageRepository;
    private final PageService pageService;

    public LegalTermsController(PageRepository pageRepository, PageService pageService) {
        this.pageRepository = pageRepository;
        this.pageService = pageService;
    }

    @GetMapping
    public ModelAndView legalTerms() {
        Page page = pageRepository.findByNameAndModel(LegalTermsModel.LEGAL_TERMS_PAGE_NAME,
                                                      LegalTermsModel.LEGAL_TERMS_PAGE_MODEL.getName())
                                  .orElseThrow(NotFoundException::new);
        return new ModelAndView("legal-terms", "page", pageService.buildPageContent(LegalTermsModel.LEGAL_TERMS_PAGE_MODEL, page));
    }
}
