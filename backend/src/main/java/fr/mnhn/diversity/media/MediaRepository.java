package fr.mnhn.diversity.media;

import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MediaRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public MediaRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void updateCategories(Long mediaPageId, List<Long> mediaCategoriesId) {
        // update the categories
        removeCategoriesFromIndicator(mediaPageId);
        mediaCategoriesId.forEach(category -> {
            Map<String, Object> categoryParamMap = Map.of(
                "media_page_id", mediaPageId,
                "category_id", category
            );
            jdbcTemplate.update(
                "insert into media_category_relation (media_page_id, category_id) values (:media_page_id, :category_id)",
                categoryParamMap);
        });
    }

    private void removeCategoriesFromIndicator(Long mediaPageId) {
        Map<String, Object> paramMap = Map.of(
            "id", mediaPageId
        );
        jdbcTemplate.update("delete from media_category_relation where media_page_id = :id", paramMap);
    }

}
