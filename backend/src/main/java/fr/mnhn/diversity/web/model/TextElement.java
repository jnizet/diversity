package fr.mnhn.diversity.web.model;

import java.util.Objects;

/**
 * A text, single or multi line, of a page model
 * @author JB Nizet
 */
public final class TextElement extends PageElement {
    private final boolean multiLine;

    public TextElement(Builder builder) {
        super(builder.name, builder.description);
        this.multiLine = builder.multiLine;
    }

    public boolean isMultiLine() {
        return multiLine;
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
        return multiLine == that.multiLine;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), multiLine);
    }

    @Override
    public String toString() {
        return "TextElement{" +
            "multiLine=" + multiLine +
            "} " + super.toString();
    }

    public static Builder builder(String name) {
        return new Builder(name);
    }

    public static class Builder extends BaseBuilder<Builder> implements PageElementBuilder {
        private boolean multiLine = false;

        public Builder(String name) {
            super(name);
        }

        public Builder multiLine() {
            this.multiLine = true;
            return this;
        }

        @Override
        public TextElement build() {
            return new TextElement(this);
        }
    }
}
