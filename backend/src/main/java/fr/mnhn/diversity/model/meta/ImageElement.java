package fr.mnhn.diversity.model.meta;

/**
 * An image element of a page model
 * @author JB Nizet
 */
public final class ImageElement extends PageElement {

    /**
     * If this is true, then when uploaded, the image will be resized and stored in several
     * sizes, and when rendered the image will have an `srcset` attribute allowing the browser to
     * download the optimal one.
     */
    private final boolean multiSize;

    public ImageElement(Builder builder) {
        super(builder.name, builder.description);
        this.multiSize = builder.multiSize;
    }

    public boolean isMultiSize() {
        return multiSize;
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

        private boolean multiSize = false;

        public Builder(String name) {
            super(name);
        }

        public Builder multiSize() {
            this.multiSize = true;
            return this;
        }

        @Override
        public ImageElement build() {
            return new ImageElement(this);
        }
    }
}
