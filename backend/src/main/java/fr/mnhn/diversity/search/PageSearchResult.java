package fr.mnhn.diversity.search;

import java.util.Objects;

/**
 * A result of a page full-text search
 * @author JB Nizet
 */
public final class PageSearchResult {
    private final Long id;
    private final String name;
    private final String modelName;
    private final String title;
    private final String highlight;
    private final String path;

    public PageSearchResult(Long id, String name, String modelName, String title, String highlight, String path) {
        this.id = id;
        this.name = name;
        this.modelName = modelName;
        this.title = title;
        this.highlight = highlight;
        this.path = path;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getModelName() {
        return modelName;
    }

    public String getTitle() {
        return title;
    }

    public String getHighlight() {
        return highlight;
    }

    public String getPath() {
        return path;
    }

    public PageSearchResult withPath(String path) {
        return new PageSearchResult(id, name, modelName, title, highlight, path);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PageSearchResult)) {
            return false;
        }
        PageSearchResult that = (PageSearchResult) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(modelName, that.modelName) &&
            Objects.equals(title, that.title) &&
            Objects.equals(highlight, that.highlight) &&
            Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, modelName, title, highlight, path);
    }

    @Override
    public String toString() {
        return "PageSearchResult{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", modelName='" + modelName + '\'' +
            ", title='" + title + '\'' +
            ", highlight='" + highlight + '\'' +
            ", url='" + path + '\'' +
            '}';
    }
}
