package fr.mnhn.diversity.model.meta;

import java.util.Map;
import java.util.Objects;

/**
 * A select of a page model
 * @author JB Nizet
 */
public final class CheckboxElement extends PageElement {

    public CheckboxElement(Builder builder) {
        super(builder.name, builder.description);
    }


    @Override
    public <T> T accept(PageElementVisitor<T> visitor) {
        return visitor.visitCheckbox(this);
    }

    @Override
    public String toString() {
        return "CheckboxElement{} " + super.toString();
    }

    public static Builder builder(String name) {
        return new Builder(name);
    }

    public static class Builder extends BaseBuilder<Builder> implements PageElementBuilder {
        private String value = "";

        public Builder(String name) { super(name); }

        @Override
        public CheckboxElement build() {
            return new CheckboxElement(this);
        }
    }
}
