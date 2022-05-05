package fr.mnhn.diversity.model.meta;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MultiListElement extends PageElement {

    private final List<SectionElement> templates;

    private MultiListElement(MultiListElement.Builder builder) {
        super(builder.name, builder.description);
        templates = List.copyOf(builder.templates);
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

    public List<SectionElement> getTemplates() {
        return templates;
    }

    public static class Builder extends ContainerElementBuilder<MultiListElement.Builder> implements PageElementBuilder {
        public Builder(String name) {
            super(name);
        }
        private List<SectionElement> templates = new ArrayList<>();

        @Override
        public MultiListElement build() {
            return new MultiListElement(this);
        }

        public MultiListElement.Builder template(SectionElement.Builder templateBuilder) {
            SectionElement template = templateBuilder.build();
            this.templates.add(template);
            return this;
        }

    }
}
