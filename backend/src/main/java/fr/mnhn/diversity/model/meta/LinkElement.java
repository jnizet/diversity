package fr.mnhn.diversity.model.meta;

/**
 * A link element of a page model
 * @author JB Nizet
 */
public final class LinkElement extends PageElement {

    public LinkElement(Builder builder) {
        super(builder.name, builder.description);
    }

    @Override
    public <T> T accept(PageElementVisitor<T> visitor) {
        return visitor.visitLink(this);
    }

    @Override
    public String toString() {
        return "LinkElement{} " + super.toString();
    }

    public static LinkElement.Builder builder(String name) {
        return new LinkElement.Builder(name);
    }

    public static class Builder extends BaseBuilder<Builder> implements PageElementBuilder {

        public Builder(String name) {
            super(name);
        }

        @Override
        public LinkElement build() {
            return new LinkElement(this);
        }
    }
}
