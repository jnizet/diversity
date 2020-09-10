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
    private final String url;

    public PageSearchResult(Long id, String name, String modelName, String title, String highlight, String url) {
        this.id = id;
        this.name = name;
        this.modelName = modelName;
        this.title = title;
        this.highlight = highlight;
        this.url = url;
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

    public String getUrl() {
        return url;
    }

    public PageSearchResult withUrl(String url) {
        return new PageSearchResult(id, name, modelName, title, highlight, url);
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
            Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, modelName, title, highlight, url);
    }

    @Override
    public String toString() {
        return "PageSearchResult{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", modelName='" + modelName + '\'' +
            ", title='" + title + '\'' +
            ", highlight='" + highlight + '\'' +
            ", url='" + url + '\'' +
            '}';
    }
}
