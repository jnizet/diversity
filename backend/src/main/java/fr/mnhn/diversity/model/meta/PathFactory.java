package fr.mnhn.diversity.model.meta;

/**
 * A factory for a path. It's a function which takes a page name (a slug, typically), and returns a path,
 * starting with a /. For example, the path factory for a territory would be
 * <code>pageName -> "/territoires/" + pageName</code>.
 */
@FunctionalInterface
public interface PathFactory {

    /**
     * Returns the path for the given page name.
     * @return null if there is no actual path for the page, because the page is a shared sub-section
     * of actual pages.
     */
    String apply(String pageName);
}
