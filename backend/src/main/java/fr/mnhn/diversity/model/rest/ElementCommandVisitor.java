package fr.mnhn.diversity.model.rest;

/**
 * A visitor for element commands
 */
public interface ElementCommandVisitor<T> {
    T visitText(TextCommandDTO text);

    T visitImage(ImageCommandDTO image);

    T visitLink(LinkCommandDTO link);
}
