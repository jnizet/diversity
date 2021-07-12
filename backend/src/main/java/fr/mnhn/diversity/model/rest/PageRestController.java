package fr.mnhn.diversity.model.rest;

import static fr.mnhn.diversity.common.PageModels.ALL_PAGE_MODELS_BY_NAME;

import fr.mnhn.diversity.model.Checkbox;
import fr.mnhn.diversity.model.Select;
import fr.mnhn.diversity.model.meta.CheckboxElement;
import fr.mnhn.diversity.model.meta.MultiListElement;
import fr.mnhn.diversity.model.meta.MultiListTemplateElement;
import fr.mnhn.diversity.model.meta.SelectElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import fr.mnhn.diversity.common.exception.BadRequestException;
import fr.mnhn.diversity.common.exception.NotFoundException;
import fr.mnhn.diversity.model.Element;
import fr.mnhn.diversity.model.ElementType;
import fr.mnhn.diversity.model.Image;
import fr.mnhn.diversity.model.Link;
import fr.mnhn.diversity.model.Page;
import fr.mnhn.diversity.model.PageRepository;
import fr.mnhn.diversity.model.PageService;
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
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    private final PageService pageService;
    private Map<String, PageModel> modelsByName;

    public PageRestController(PageRepository pageRepository, PageService pageService) {
        this.pageRepository = pageRepository;
        this.pageService = pageService;
        modelsByName = ALL_PAGE_MODELS_BY_NAME;
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
        return new PageValuesDTO(page.getId(), page.getTitle(), page.getName(), model.getName(), model.getDescription(), visitor.getElements());
    }

    @GetMapping("/{pageModel}/{pageName}")
    public PageValuesDTO getPageValues(@PathVariable("pageModel") String pageModel, @PathVariable("pageName") String pageName) {
        Page page = pageRepository.findByNameAndModel(pageName, pageModel)
            .orElseThrow(NotFoundException::new);
        PageModel model = modelsByName.get(pageModel);
        PageValuesPopulatorVisitor visitor = new PageValuesPopulatorVisitor(page, "");
        for (PageElement pageElement : model.getElements()) {
            pageElement.accept(visitor);
        }
        return new PageValuesDTO(page.getId(), page.getTitle(), page.getName(), model.getName(), model.getDescription(), visitor.getElements());
    }

    @GetMapping("import/{pageModel}/{pageName}")
    public String getImportedPageValues(@PathVariable("pageModel") String pageModel, @PathVariable("pageName") String pageName) {
        var pageValues = pageService.getPageDataFromImportDataSource(pageModel, pageName).block();
        return pageValues;
    }
    /**
     * Returns a DTO containing:
     * - the metadata of the specified page
     * - empty values of each element of the page
     * - the meta-model associated to the page
     */
    @GetMapping("/models/{modelName}")
    public PageValuesDTO getPageModel(@PathVariable("modelName") String modelName) {
        PageModel model = modelsByName.get(modelName);
        PageValuesPopulatorVisitor visitor = new PageValuesPopulatorVisitor(null, "");
        for (PageElement pageElement : model.getElements()) {
            pageElement.accept(visitor);
        }
        return new PageValuesDTO(null, "", "", modelName, model.getDescription(), visitor.getElements());
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
        public Void visitMultiListElement(MultiListElement section) {
            Map<Integer, List<PageElementDTO>> elementsByIndex =new HashMap<>();
            List<String> alreadyVisitIndex = new ArrayList<>();
            String name = section.getName();
            String key = prefix + name  + "." ;
            PageValuesPopulatorVisitor templateVisitor = new PageValuesPopulatorVisitor(null, "");

            if(page != null) {
                section.getTemplates().stream().forEach(t ->
                    page.getElements().keySet().stream().filter(e ->
                        e.contains(t.getName()) && e.contains(prefix)
                    ).forEach(e -> {
                        var index = e.substring(key.length(), key.length() + 1);
                        String elementsPrefix = key + index + ".";
                        if(!alreadyVisitIndex.contains(elementsPrefix)) {
                            alreadyVisitIndex.add(elementsPrefix);
                            PageValuesPopulatorVisitor listVisitor = new PageValuesPopulatorVisitor(
                                page, elementsPrefix);
                            t.accept(listVisitor);
                            elementsByIndex.put(Integer.parseInt(index), listVisitor.getElements());
                        }
                    }));
            }

            for (PageElement element : section.getTemplates()) {
                element.accept(templateVisitor);
            }

            Map<Integer, List<PageElementDTO>> elementsByIndexSorted = new TreeMap<>(elementsByIndex);

            MultiListElementDTO sectionElementDTO = new MultiListElementDTO(section, elementsByIndexSorted.values().stream().flatMap(List::stream).collect(
                Collectors.toList()), templateVisitor.getElements());
            elements.add(sectionElementDTO);
            return null;
        }

        @Override
        public Void visitMultiListTemplateElement(
            MultiListTemplateElement multiListTemplateElement) {
            String name = multiListTemplateElement.getName();
            String key = prefix + name;
            PageValuesPopulatorVisitor sectionVisitor = new PageValuesPopulatorVisitor(page, key + ".");
            for (PageElement element : multiListTemplateElement.getElements()) {
                element.accept(sectionVisitor);
            }
            MultiListTemplateElementDTO sectionElementDTO = new MultiListTemplateElementDTO(multiListTemplateElement, sectionVisitor.getElements());
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
            int maxListIndex = page != null ? PageUtils.findMaxListIndex(page, listKey + ".") : 0;
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
            if (page != null) {
                Text value = (Text) PageUtils.getElement(page, key, ElementType.TEXT);
                TextElementDTO textElementDTO = new TextElementDTO(text, value);
                elements.add(textElementDTO);
            }
            else {
                elements.add(new TextElementDTO(text, new Text(null, key, "")));
            }
            return null;
        }

        @Override
        public Void visitImage(ImageElement image) {
            String name = image.getName();
            String key = prefix + name;
            if (page != null) {
                Image value = (Image) PageUtils.getElement(page, key, ElementType.IMAGE);
                ImageElementDTO imageElementDTO = new ImageElementDTO(image, value);
                elements.add(imageElementDTO);
            }
            else {
                elements.add(new ImageElementDTO(image, new Image(null, key, null, "", image.isMultiSize())));
            }
            return null;
        }

        @Override
        public Void visitLink(LinkElement link) {
            String name = link.getName();
            String key = prefix + name;
            if (page != null) {
                Link value = (Link) PageUtils.getElement(page, key, ElementType.LINK);
                LinkElementDTO linkElementDTO = new LinkElementDTO(link, value);
                elements.add(linkElementDTO);
            }
            else {
                elements.add(new LinkElementDTO(link, new Link(null, key, "", "")));
            }
            return null;
        }

        @Override
        public Void visitSelect(SelectElement select) {
            String name = select.getName();
            String key = prefix + name;
            if (page != null) {
                Select value = (Select) PageUtils.getElement(page, key, ElementType.SELECT);
                SelectElementDTO selectElementDTO = new SelectElementDTO(select, value);
                elements.add(selectElementDTO);
            }
            else {
                elements.add(new SelectElementDTO(select, new Select(null, key, "")));
            }
            return null;
        }

        @Override
        public Void visitCheckbox(CheckboxElement checkbox) {
            String name = checkbox.getName();
            String key = prefix + name;
            if (page != null) {
                Checkbox value = (Checkbox) PageUtils.getElement(page, key, ElementType.CHECKBOX);
                CheckboxElementDTO checkboxElementDTO = new CheckboxElementDTO(checkbox, value);
                elements.add(checkboxElementDTO);
            }
            else {
                elements.add(new CheckboxElementDTO(checkbox, new Checkbox(null, key, false)));
            }
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

        ElementCommandVisitor<Element> commandVisitor = new CommandVisitor();
        List<Element> updateElements = command.getElements().stream().map(element -> element.accept(commandVisitor)).collect(Collectors.toList());
        Page updatedPage = new Page(pageId, page.getName(), page.getModelName(), command.getTitle(), updateElements);

        pageService.validatePageContent(modelsByName.get(page.getModelName()), updatedPage);

        pageRepository.update(updatedPage);
    }

    @PostMapping("/models/{modelName}")
    @ResponseStatus(HttpStatus.CREATED)
    public PageValuesDTO create(@PathVariable("modelName") String modelName, @Validated @RequestBody PageCommandDTO command) {
        if (!modelsByName.containsKey(modelName)) {
            throw new BadRequestException("invalid model name");
        }
        ElementCommandVisitor<Element> commandVisitor = new CommandVisitor();
        List<Element> elementsToCreate = command.getElements().stream().map(element -> element.accept(commandVisitor)).collect(Collectors.toList());
        Page page = new Page(null, command.getName(), modelName, command.getTitle(), elementsToCreate);

        pageService.validatePageContent(modelsByName.get(page.getModelName()), page);

        page = pageRepository.create(page);
        // rebuild the DTO
        PageModel model = modelsByName.get(modelName);
        PageValuesPopulatorVisitor visitor = new PageValuesPopulatorVisitor(page, "");
        for (PageElement pageElement : model.getElements()) {
            pageElement.accept(visitor);
        }
        return new PageValuesDTO(page.getId(), page.getTitle(), page.getName(), model.getName(), model.getDescription(), visitor.getElements());
    }

    private static class CommandVisitor implements ElementCommandVisitor<Element> {
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

        @Override
        public Element visitSelect(SelectCommandDTO select) {
            return new Select(null, select.getKey(), select.getValue());
        }

        @Override
        public Element visitCheckbox(CheckboxCommandDTO checkbox) {
            return new Checkbox(null, checkbox.getKey(), checkbox.getValue());
        }
    }
}
