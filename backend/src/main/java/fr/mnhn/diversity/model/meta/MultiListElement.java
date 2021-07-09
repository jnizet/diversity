package fr.mnhn.diversity.model.meta;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MultiListElement extends ContainerElement {

    private final List<MultiListTemplateElement> templates;

    private MultiListElement(MultiListElement.Builder builder) {
        super(builder.name, builder.description, builder.elements);
        templates = builder.templates;
    }

    @Override
    public <T> T accept(PageElementVisitor<T> visitor) {
        return visitor.visitMultiListElement(this);
    }

    @Override
    public String toString() {
        return "MultiListElement{} " + super.toString();
    }

    public static MultiListElement.Builder builder(String name) {
        return new MultiListElement.Builder(name);
    }

    public List<MultiListTemplateElement> getTemplates() {
        return templates;
    }

    public static class Builder extends ContainerElementBuilder<MultiListElement.Builder> implements PageElementBuilder {
        public Builder(String name) {
            super(name);
        }
        private List<MultiListTemplateElement> templates = Collections.emptyList();

        @Override
        public MultiListElement build() {
            return new MultiListElement(this);
        }

        public MultiListElement.Builder templates(List<MultiListTemplateElement.Builder> templates) {
            this.templates = templates.stream().map(t -> t.build()).collect(Collectors.toList());
            return this;
        }

    }
}
