package fr.mnhn.diversity.model.rest;

import fr.mnhn.diversity.model.Link;
import fr.mnhn.diversity.model.meta.LinkElement;

/**
 * DTO representing a link element of the page, with its value
 */
public final class LinkElementDTO extends PageElementDTO {
    // value
    private final Long id;
    private final String text;
    private final String href;

    public LinkElementDTO(LinkElement link, Link value) {
        super(PageElementType.LINK, link);
        this.id = value.getId();
        this.text = value.getText();
        this.href = value.getHref();
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getHref() {
        return href;
    }
}
