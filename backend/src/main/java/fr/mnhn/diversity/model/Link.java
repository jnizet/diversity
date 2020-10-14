package fr.mnhn.diversity.model;

import java.util.Objects;

/**
 * A link of a Page instance
 * @author JB Nizet
 */
public final class Link extends Element {
    /**
     * The text of the element
     */
    private final String text;

    /**
     * The href of the element
     */
    private final String href;

    public Link(Long id, String key, String text, String href) {
        super(id, ElementType.LINK, key);
        this.text = text;
        this.href = href;
    }

    public String getText() {
        return text;
    }

    public String getHref() {
        return href;
    }

    @Override
    public <T> T accept(ElementVisitor<T> visitor) {
        return visitor.visitLink(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Link)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Link link = (Link) o;
        return Objects.equals(text, link.text) &&
            Objects.equals(href, link.href);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), text, href);
    }

    @Override
    public String toString() {
        return "Link{" +
            "text='" + text + '\'' +
            ", href='" + href + '\'' +
            "} " + super.toString();
    }
}
