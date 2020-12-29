package fr.mnhn.diversity.model.meta;

import java.util.Objects;

/**
 * A text, single or multi line, of a page model
 * @author JB Nizet
 */
public final class TextElement extends PageElement {
    private final boolean multiLine;
    private final boolean title;

    public TextElement(Builder builder) {
        super(builder.name, builder.description);
        this.multiLine = builder.multiLine;
        this.title = builder.title;
    }

    public boolean isMultiLine() {
        return multiLine;
    }

    public boolean isTitle() {
        return this.title;
    }

    @Override
    public <T> T accept(PageElementVisitor<T> visitor) {
        return visitor.visitText(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TextElement)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        TextElement that = (TextElement) o;
        return multiLine == that.multiLine &&
            title == that.title;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), multiLine, title);
    }

    @Override
    public String toString() {
        return "TextElement{" +
            "multiLine=" + multiLine +
            ", title=" + title +
            "} " + super.toString();
    }

    public static Builder builder(String name) {
        return new Builder(name);
    }

    public static class Builder extends BaseBuilder<Builder> implements PageElementBuilder {
        private boolean multiLine = false;
        private boolean title = false;

        public Builder(String name) {
            super(name);
        }

        public Builder multiLine() {
            this.multiLine = true;
            return this;
        }

        public Builder title() {
            this.title = true;
            return this;
        }

        public Builder multiLineTitle() {
            this.title = true;
            this.multiLine = true;
            return this;
        }

        @Override
        public TextElement build() {
            return new TextElement(this);
        }
    }
}
