package fr.mnhn.diversity.media;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Objects;

public class MediaCommandDTO {

    private final Long id;
    private final List<Long> categoriesId;

    @JsonCreator
    public MediaCommandDTO(
        @JsonProperty("id")  Long id,
        @JsonProperty("categoriesId")  List<Long> categoriesId
    ) {
        this.categoriesId = categoriesId;
        this.id = id;
    }


    public Long getId() {
        return id;
    }

    public List<Long> getCategoriesId() {
        return categoriesId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MediaCommandDTO that = (MediaCommandDTO) o;
        return Objects.equals(id, that.getId()) &&
            Objects.equals(categoriesId, that.getCategoriesId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, categoriesId);
    }

    @Override
    public String toString() {
        return "MediaCategoryCommandDTO{" +
            "id='" + id + '\'' +
            "categoriesId='" + categoriesId + '\'' +
            '}';
    }

}
