package fr.mnhn.diversity.web.home.model;

/**
 * A list element of a page model. Such an element can be repeated 0 to N times inside the actual page instance,
 * and all the elements of the list must have the same elements, described in this model
 * @author JB Nizet
 */
public final class ListElement extends ContainerElement {
    private ListElement(ListElement.Builder builder) {
        super(builder.name, builder.description, builder.elements);
    }

    @Override
    public String toString() {
        return "ListElement{} " + super.toString();
    }

    public static Builder builder(String name) {
        return new Builder(name);
    }

    public static class Builder extends ContainerElementBuilder<ListElement.Builder> implements PageElementBuilder {
        public Builder(String name) {
            super(name);
        }

        @Override
        public ListElement build() {
            return new ListElement(this);
        }
    }
}
