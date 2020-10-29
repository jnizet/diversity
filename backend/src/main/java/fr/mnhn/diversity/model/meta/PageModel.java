package fr.mnhn.diversity.model.meta;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The model of a page (i.e. the structure that a page must have)
 * @author JB Nizet
 */
public class PageModel {
    private final String name;
    private final String description;
    private final List<PageElement> elements;
    private final PathFactory pathFactory;

    private PageModel(Builder builder) {
        this.name = Objects.requireNonNull(builder.name, "the page model name may not be null");
        this.description = builder.description;
        this.pathFactory = Objects.requireNonNull(builder.pathFactory, "the path factory may not be null");
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

    /**
     * Returns the path for this page, based on the given page name (typically the slug of the page)
     * @return empty if this model is for a shared sub-section of multiple actual pages, that
     * does not have a path. Otherwise, the path (starting with a /)
     */
    public Optional<String> toPath(String pageName) {
        return Optional.ofNullable(pathFactory.apply(pageName));
    }

    public static Builder builder(String name) {
        return new Builder(name);
    }

    public static final class Builder extends ContainerElementBuilder<Builder> {

        private PathFactory pathFactory;

        public Builder(String name) {
            super(name);
        }

        public Builder withPathFactory(PathFactory pathFactory) {
            this.pathFactory = pathFactory;
            return this;
        }

        public Builder withPath(String path) {
            return withPathFactory(name -> path);
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
