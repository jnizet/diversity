package fr.mnhn.diversity.model.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fr.mnhn.diversity.about.AboutModel;
import fr.mnhn.diversity.common.exception.NotFoundException;
import fr.mnhn.diversity.ecogesture.EcoGestureModel;
import fr.mnhn.diversity.home.HomeModel;
import fr.mnhn.diversity.indicator.IndicatorModel;
import fr.mnhn.diversity.model.Element;
import fr.mnhn.diversity.model.ElementType;
import fr.mnhn.diversity.model.Image;
import fr.mnhn.diversity.model.Link;
import fr.mnhn.diversity.model.Page;
import fr.mnhn.diversity.model.PageRepository;
import fr.mnhn.diversity.model.PageUtils;
import fr.mnhn.diversity.model.Text;
import fr.mnhn.diversity.model.meta.ImageElement;
import fr.mnhn.diversity.model.meta.LinkElement;
import fr.mnhn.diversity.model.meta.ListElement;
import fr.mnhn.diversity.model.meta.PageElement;
import fr.mnhn.diversity.model.meta.PageElementVisitor;
import fr.mnhn.diversity.model.meta.PageModel;
import fr.mnhn.diversity.model.meta.SectionElement;
import fr.mnhn.diversity.model.meta.TextElement;
import fr.mnhn.diversity.territory.TerritoryModel;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller used to handle the pages
 */
@RestController
@Transactional
@RequestMapping("/api/pages")
public class PageRestController {

    private final PageRepository pageRepository;
    private Map<String, PageModel> modelsByName = new HashMap<>();

    public PageRestController(PageRepository pageRepository) {
        this.pageRepository = pageRepository;
        modelsByName.put(HomeModel.HOME_PAGE_MODEL.getName(), HomeModel.HOME_PAGE_MODEL);
        modelsByName.put(AboutModel.ABOUT_PAGE_MODEL.getName(), AboutModel.ABOUT_PAGE_MODEL);
        modelsByName.put(EcoGestureModel.ECO_GESTURE_HOME_PAGE_MODEL.getName(), EcoGestureModel.ECO_GESTURE_HOME_PAGE_MODEL);
        modelsByName.put(EcoGestureModel.ECO_GESTURE_PAGE_MODEL.getName(), EcoGestureModel.ECO_GESTURE_PAGE_MODEL);
        modelsByName.put(TerritoryModel.TERRITORY_PAGE_MODEL.getName(), TerritoryModel.TERRITORY_PAGE_MODEL);
        modelsByName.put(IndicatorModel.INDICATOR_HOME_PAGE_MODEL.getName(), IndicatorModel.INDICATOR_HOME_PAGE_MODEL);
        modelsByName.put(IndicatorModel.INDICATOR_PAGE_MODEL.getName(), IndicatorModel.INDICATOR_PAGE_MODEL);
    }

    /**
     * For testing purpose
     */
    protected void setModelsByName(Map<String, PageModel> modelsByName) {
        this.modelsByName = modelsByName;
    }

    /**
     * Returns a DTO containing:
     * - the metadata of the specified page
     * - the values of each element of the page
     * - the meta-model associated to the page
     * <p>
     * It allows to display a form to update the metadata and the values of each element,
     * with a description of what this element does.
     * <p>
     * The DTO returned is a structure containing elements.
     * The elements can be "leaf" elements like texts, links and images,
     * or "collection" elements like sections or lists,
     * which themselves contains elements, etc.
     * <p>
     * Note that there is a difference between lists and sections,
     * compared to what we do elsewhere.
     * Here, to simplify the frontend job, we represent a list as a list of "list unit",
     * each "list unit" containing elements.
     * <p>
     * For example, the elements:
     * - carousel.slides.0.link
     * - carousel.slides.0.image
     * - carousel.slides.1.link
     * - carousel.slides.1.image
     * are represented as a list "slides", containing 2 list units, each one with 2 elements (link and image).
     */
    @GetMapping("/{pageId}")
    public PageValuesDTO getPageValues(@PathVariable("pageId") Long pageId) {
        Page page = pageRepository.findById(pageId).orElseThrow(NotFoundException::new);
        PageModel model = modelsByName.get(page.getModelName());
        PageValuesPopulatorVisitor visitor = new PageValuesPopulatorVisitor(page, "");
        for (PageElement pageElement : model.getElements()) {
            pageElement.accept(visitor);
        }
        return new PageValuesDTO(page.getId(), page.getTitle(), model.getName(), model.getDescription(), visitor.getElements());
    }

    /**
     * Visitor in charge of creating the DTO for each element
     */
    private static class PageValuesPopulatorVisitor implements PageElementVisitor<Void> {
        private final Page page;
        private final String prefix;
        private final List<PageElementDTO> elements = new ArrayList<>();

        public PageValuesPopulatorVisitor(Page page, String prefix) {
            this.page = page;
            this.prefix = prefix;
        }

        @Override
        public Void visitSection(SectionElement section) {
            String name = section.getName();
            String key = prefix + name;
            PageValuesPopulatorVisitor sectionVisitor = new PageValuesPopulatorVisitor(page, key + ".");
            for (PageElement element : section.getElements()) {
                element.accept(sectionVisitor);
            }
            SectionElementDTO sectionElementDTO = new SectionElementDTO(section, sectionVisitor.getElements());
            elements.add(sectionElementDTO);
            return null;
        }

        @Override
        public Void visitList(ListElement list) {
            List<ListUnitElementDTO> theList = new ArrayList<>();

            // if the list model has no element (which should never happen), there is nothing to add to the list
            if (list.getElements().isEmpty()) {
                return null;
            }

            // here we find what the max index is
            // then build a list unit for each index, and add the elements to the unit
            String listKey = prefix + list.getName();
            int maxListIndex = PageUtils.findMaxListIndex(page, listKey + ".");
            for (int i = 0; i <= maxListIndex; i++) {
                String elementsPrefix = listKey + "." + i + ".";
                PageValuesPopulatorVisitor listVisitor = new PageValuesPopulatorVisitor(page, elementsPrefix);
                for (PageElement listElement : list.getElements()) {
                    listElement.accept(listVisitor);
                }
                theList.add(new ListUnitElementDTO(list, listVisitor.getElements()));
            }
            elements.add(new ListElementDTO(list, theList));
            return null;
        }

        @Override
        public Void visitText(TextElement text) {
            String name = text.getName();
            String key = prefix + name;
            Text value = (Text) PageUtils.getElement(page, key, ElementType.TEXT);
            TextElementDTO textElementDTO = new TextElementDTO(text, value);
            elements.add(textElementDTO);
            return null;
        }

        @Override
        public Void visitImage(ImageElement image) {
            String name = image.getName();
            String key = prefix + name;
            Image value = (Image) PageUtils.getElement(page, key, ElementType.IMAGE);
            ImageElementDTO imageElementDTO = new ImageElementDTO(image, value);
            elements.add(imageElementDTO);
            return null;
        }

        @Override
        public Void visitLink(LinkElement link) {
            String name = link.getName();
            String key = prefix + name;
            Link value = (Link) PageUtils.getElement(page, key, ElementType.LINK);
            LinkElementDTO linkElementDTO = new LinkElementDTO(link, value);
            elements.add(linkElementDTO);
            return null;
        }

        public List<PageElementDTO> getElements() {
            return elements;
        }
    }

    @PutMapping("/{pageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable("pageId") Long pageId,
                       @Validated @RequestBody PageCommandDTO command) {
        Page page = pageRepository.findById(pageId).orElseThrow(NotFoundException::new);
        ElementCommandVisitor<Element> visitor = new ElementCommandVisitor<>() {
            @Override
            public Element visitText(TextCommandDTO text) {
                return new Text(null, text.getKey(), text.getText());
            }

            @Override
            public Element visitImage(ImageCommandDTO image) {
                return new Image(null, image.getKey(), image.getImageId(), image.getAlt(), image.isMultiSize());
            }

            @Override
            public Element visitLink(LinkCommandDTO link) {
                return new Link(null, link.getKey(), link.getText(), link.getHref());
            }
        };
        List<Element> updateElements = command.getElements().stream().map(element -> element.accept(visitor)).collect(Collectors.toList());
        Page updatedPage = new Page(pageId, page.getName(), page.getModelName(), command.getTitle(), updateElements);
        // TODO validate the page
        pageRepository.update(updatedPage);
    }
}