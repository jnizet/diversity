package fr.mnhn.diversity.web.home.model;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Base class for page elements which contain other page elements
 * @author JB Nizet
 */
public abstract class ContainerElement extends PageElement {
    private final List<PageElement> elements;

    protected ContainerElement(String name,
                               String description,
                               List<PageElementBuilder> elementBuilders) {
        super(name, description);
        this.elements = elementBuilders.stream().map(PageElementBuilder::build).collect(Collectors.toUnmodifiableList());
    }

    public List<PageElement> getElements() {
        return elements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContainerElement)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        ContainerElement that = (ContainerElement) o;
        return Objects.equals(elements, that.elements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), elements);
    }

    @Override
    public String toString() {
        return "ContainerElement{" +
            "elements=" + elements +
            "} " + super.toString();
    }
}
