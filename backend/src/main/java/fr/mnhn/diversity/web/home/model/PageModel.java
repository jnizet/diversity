package fr.mnhn.diversity.web.home.model;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * The model of a page (i.e. the structure that a page must have)
 * @author JB Nizet
 */
public class PageModel {
    private final String name;
    private final String description;
    private final List<PageElement> elements;

    private PageModel(Builder builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.elements = builder.elements.stream().map(PageElementBuilder::build).collect(Collectors.toUnmodifiableList());
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<PageElement> getElements() {
        return elements;
    }

    public static Builder builder(String name) {
        return new Builder(name);
    }

    public static final class Builder extends ContainerElementBuilder<Builder> {

        public Builder(String name) {
            super(name);
        }

        public PageModel build() {
            return new PageModel(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PageModel)) {
            return false;
        }
        PageModel pageModel = (PageModel) o;
        return Objects.equals(name, pageModel.name) &&
            Objects.equals(elements, pageModel.elements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, elements);
    }

    @Override
    public String toString() {
        return "PageModel{" +
            "name='" + name + '\'' +
            ", elements=" + elements +
            '}';
    }
}
