package fr.mnhn.diversity.web.home.model;

/**
 * An image element of a page model
 * @author JB Nizet
 */
public final class ImageElement extends PageElement {

    public ImageElement(Builder builder) {
        super(builder.name, builder.description);
    }

    @Override
    public <T> T accept(PageElementVisitor<T> visitor) {
        return visitor.visitImage(this);
    }

    @Override
    public String toString() {
        return "ImageElement{} " + super.toString();
    }

    public static Builder builder(String name) {
        return new Builder(name);
    }

    public static class Builder extends BaseBuilder<Builder> implements PageElementBuilder {

        public Builder(String name) {
            super(name);
        }

        @Override
        public ImageElement build() {
            return new ImageElement(this);
        }
    }
}
