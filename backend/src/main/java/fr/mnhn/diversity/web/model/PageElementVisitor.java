package fr.mnhn.diversity.web.model;

/**
 * A visitor for page elements
 * @author JB Nizet
 */
public interface PageElementVisitor<T> {
    T visitSection(SectionElement section);
    T visitList(ListElement list);
    T visitText(TextElement text);
    T visitImage(ImageElement image);
    T visitLink(LinkElement link);
}
