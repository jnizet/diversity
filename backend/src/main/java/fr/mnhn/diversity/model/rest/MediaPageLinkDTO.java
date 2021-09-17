package fr.mnhn.diversity.model.rest;

import fr.mnhn.diversity.media.MediaCategory;
import fr.mnhn.diversity.model.BasicPage;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MediaPageLinkDTO extends PageLinkDTO{

    private final List<MediaCategory> categories;

    public MediaPageLinkDTO(Long id, String name, String modelName, String title) {
        super(id, name, modelName, title);
        categories = Collections.emptyList();
    }

    public MediaPageLinkDTO(BasicPage page) {
        super(page);
        categories = Collections.emptyList();
    }

    public MediaPageLinkDTO(String name, String modelName) {
        super(name, modelName);
        categories = Collections.emptyList();
    }

    public MediaPageLinkDTO(BasicPage page, List<MediaCategory> categories) {
        super(page);
        this.categories = categories;
    }

    public List<MediaCategory> getCategories() {
        return categories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MediaPageLinkDTO that = (MediaPageLinkDTO) o;
        return Objects.equals(getId(), that.getId()) &&
            Objects.equals(getName(), that.getName()) &&
            Objects.equals(getModelName(), that.getModelName()) &&
            Objects.equals(getTitle(), that.getTitle()) &&
            Objects.equals(getCategories(), that.getCategories());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getModelName(), getTitle(), getCategories());
    }

    @Override
    public String toString() {
        return "PageLinkDTO{" +
            "id=" + getId() +
            ", name='" + getName() + '\'' +
            ", modelName='" + getModelName() + '\'' +
            ", title='" + getTitle() + '\'' +
            ", categories='" + getCategories() + '\'' +
            '}';
    }
}
