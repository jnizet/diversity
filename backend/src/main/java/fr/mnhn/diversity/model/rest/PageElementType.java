package fr.mnhn.diversity.model.rest;

/**
 * Types of the element used in {@link PageValuesDTO}.
 * Note the extra LIST_UNIT value, compared to {@link fr.mnhn.diversity.model.ElementType},
 * as a we want to represent a list unit that groups several repeated elements.
 */
public enum PageElementType {
    TEXT,
    LINK,
    IMAGE,
    SECTION,
    LIST_UNIT,
    MULTI_LIST,
    SELECT,
    CHECKBOX,
    LIST;
}
