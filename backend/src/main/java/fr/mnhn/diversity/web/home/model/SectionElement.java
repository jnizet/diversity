package fr.mnhn.diversity.web.home.model;

/**
 * A section of a page, only useful to group some page elements together
 * @author JB Nizet
 */
public final class SectionElement extends ContainerElement {
    private SectionElement(Builder builder) {
        super(builder.name, builder.description, builder.elements);
    }

    @Override
    public String toString() {
        return "SectionElement{} " + super.toString();
    }

    public static Builder builder(String name) {
        return new Builder(name);
    }

    public static class Builder extends ContainerElementBuilder<Builder> implements PageElementBuilder {
        public Builder(String name) {
            super(name);
        }

        @Override
        public SectionElement build() {
            return new SectionElement(this);
        }
    }
}
