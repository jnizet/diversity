package fr.mnhn.diversity.model.meta;

import java.util.Map;
import java.util.Objects;

/**
 * A select of a page model
 * @author JB Nizet
 */
public final class SelectElement extends PageElement {
    private final Map<String, String> options;
    private final String value;

    public SelectElement(Builder builder) {
        super(builder.name, builder.description);
        this.options = builder.options;
        this.value = builder.value;
    }

    public Map<String, String> getOptions() {
        return options;
    }

    @Override
    public <T> T accept(PageElementVisitor<T> visitor) {
        return visitor.visitSelect(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SelectElement)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        SelectElement that = (SelectElement) o;
        return options == that.options &&
            value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), options, value);
    }

    @Override
    public String toString() {
        return "SelectElement{" +
            "options=" + options +
            "value=" + value +
            "} " + super.toString();
    }

    public static Builder builder(String name, Map<String, String> options) {
        return new Builder(name, options);
    }

    public static class Builder extends BaseBuilder<Builder> implements PageElementBuilder {
        private Map<String, String> options;
        private String value = "";

        public Builder(String name, Map<String, String> options) {
            super(name);
            this.options = options;
        }

        public Builder value(String value) {
            this.value = value;
            return this;
        }

        @Override
        public SelectElement build() {
            return new SelectElement(this);
        }
    }
}
