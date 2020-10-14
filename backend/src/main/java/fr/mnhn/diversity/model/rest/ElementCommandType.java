package fr.mnhn.diversity.model.rest;

/**
 * Types of the element used in {@link ElementCommandDTO}.
 * Note that is is a simplified version of {@link fr.mnhn.diversity.model.ElementType},
 * as we only receive the "leaf" elements, with their keys already computed
 * to reflect the list or section that they belong to
 * (so there are no lists or sections).
 */
public enum ElementCommandType {
    TEXT,
    LINK,
    IMAGE;
}
