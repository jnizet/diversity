package fr.mnhn.diversity.model.rest;

import fr.mnhn.diversity.model.Text;
import fr.mnhn.diversity.model.meta.TextElement;

/**
 * DTO representing a text element of the page, with its value
 */
public final class TextElementDTO extends PageElementDTO {
    // value
    private final Long id;
    private final String text;
    // model
    private final boolean multiLine;
    private final boolean isOptional;

    public TextElementDTO(TextElement textElement, Text textValue) {
        super(PageElementType.TEXT, textElement);
        this.id = textValue.getId();
        this.text = textValue.getText();
        this.multiLine = textElement.isMultiLine();
        this.isOptional = textElement.isOptional();
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public boolean isMultiLine() {
        return multiLine;
    }

    public boolean isOptional() {
        return isOptional;
    }
}
