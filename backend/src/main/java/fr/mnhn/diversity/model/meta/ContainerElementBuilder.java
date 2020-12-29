package fr.mnhn.diversity.model.meta;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for container element builders, allowing to add all kinds of page elements to a container
 * @author JB Nizet
 */
public class ContainerElementBuilder<B extends ContainerElementBuilder<B>> extends BaseBuilder<B> {
    protected final List<PageElementBuilder> elements = new ArrayList<>();

    protected ContainerElementBuilder(String name) {
        super(name);
    }

    @SuppressWarnings("unchecked")
    public B link(LinkElement.Builder linkBuilder) {
        this.elements.add(linkBuilder);
        return (B) this;
    }

    public B link(String name, String description) {
        return this.link(LinkElement.builder(name).describedAs(description));
    }

    @SuppressWarnings("unchecked")
    public B image(ImageElement.Builder imageBuilder) {
        this.elements.add(imageBuilder);
        return (B) this;
    }

    public B image(String name, String description) {
        return this.image(ImageElement.builder(name).describedAs(description));
    }

    public B multiSizeImage(String name, String description) {
        return this.image(ImageElement.builder(name).describedAs(description).multiSize());
    }

    public B document(String name, String description) {
        return this.image(ImageElement.builder(name).describedAs(description).document());
    }

    @SuppressWarnings("unchecked")
    public B text(TextElement.Builder textBuilder) {
        this.elements.add(textBuilder);
        return (B) this;
    }

    public B text(String name, String description) {
        return this.text(TextElement.builder(name).describedAs(description));
    }

    public B multiLineText(String name, String description) {
        return this.text(TextElement.builder(name).describedAs(description).multiLine());
    }

    public B titleText(String name, String description) {
        return this.text(TextElement.builder(name).describedAs(description).title());
    }

    public B multiLineTitleText(String name, String description) {
        return this.text(TextElement.builder(name).describedAs(description).multiLineTitle());
    }

    @SuppressWarnings("unchecked")
    public B section(SectionElement.Builder sectionBuilder) {
        this.elements.add(sectionBuilder);
        return (B) this;
    }

    @SuppressWarnings("unchecked")
    public B list(ListElement.Builder listBuilder) {
        this.elements.add(listBuilder);
        return (B) this;
    }
}
