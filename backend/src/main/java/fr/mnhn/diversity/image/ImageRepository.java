package fr.mnhn.diversity.image;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Repository for images
 * @author JB Nizet
 */
@Repository
public class ImageRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ImageRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Image> findById(Long id) {
        String sql = "select image.id, image.content_type, image.original_file_name from image where image.id = :id";
        List<Image> list = jdbcTemplate.query(sql, Map.of("id", id), (rs, rowNum) ->
            new Image(rs.getLong("id"), rs.getString("content_type"), rs.getString("original_file_name"))
        );
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }
}
