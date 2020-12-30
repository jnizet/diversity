package fr.mnhn.diversity.model;

/**
 * A visitor for elements
 */
public interface ElementVisitor<T> {
    T visitText(Text text);
    T visitImage(Image image);
    T visitLink(Link link);
    T visitSelect(Select select);
}
