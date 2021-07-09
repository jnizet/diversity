package fr.mnhn.diversity.model.meta;

import fr.mnhn.diversity.model.meta.ListElement.Builder;
import java.util.Objects;

public class MultiListTemplateElement extends ContainerElement {

    private MultiListTemplateElement(MultiListTemplateElement.Builder builder) {
        super(builder.name, builder.description, builder.elements);
    }

    @Override
    public <T> T accept(PageElementVisitor<T> visitor) {
        return visitor.visitMultiListTemplateElement(this);
    }

    @Override
    public String toString() {
        return "ListElement{} " + super.toString();
    }

    public static MultiListTemplateElement.Builder builder(String name) {
        return new MultiListTemplateElement.Builder(name);
    }

    public static class Builder extends ContainerElementBuilder<MultiListTemplateElement.Builder> implements PageElementBuilder {
        public Builder(String name) {
            super(name);
        }
        @Override
        public MultiListTemplateElement build() {
            return new MultiListTemplateElement(this);
        }

    }

}
