package fr.mnhn.diversity.web.home.model;

/**
 * Base class for builders which have a name and a description
 * @author JB Nizet
 */
public abstract class BaseBuilder<B extends BaseBuilder<B>> {

    protected final String name;
    protected String description;

    public BaseBuilder(String name) {
        this.name = name;
    }

    @SuppressWarnings("unchecked")
    public B describedAs(String description) {
        this.description = description;
        return (B) this;
    }
}
