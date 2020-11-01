package fr.mnhn.diversity.robot;

import static fr.mnhn.diversity.common.PageModels.ALL_PAGE_MODELS_BY_NAME;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

import fr.mnhn.diversity.model.BasicPage;
import fr.mnhn.diversity.model.PageRepository;
import fr.mnhn.diversity.model.meta.PageModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * REST controller used to generate the robots.txt file and the sitemap
 */
@RestController
public class RobotRestController {

    private final PageRepository pageRepository;

    public RobotRestController(PageRepository pageRepository) {
        this.pageRepository = pageRepository;
    }

    @GetMapping(value = "/robots.txt", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<byte[]> robots() {
        String body =
            "User-agent: *\n" +
            "Disallow: /admin/\n" +
            "Disallow: /mentions-legales\n\n" +
            "Sitemap: " +
                ServletUriComponentsBuilder.fromCurrentContextPath()
                                           .path("/sitemap.txt")
                                           .toUriString() +
            "\n";

        return ResponseEntity.ok(body.getBytes(StandardCharsets.UTF_8));
    }

    @GetMapping(value = "/sitemap.txt", produces = MediaType.TEXT_PLAIN_VALUE)
    @Transactional
    public ResponseEntity<byte[]> sitemap() {
        ServletUriComponentsBuilder uriBuilder =
            ServletUriComponentsBuilder.fromCurrentContextPath();
        String body = pageRepository.findAllBasic()
            .stream()
            .map(this::toPath)
            .filter(Objects::nonNull)
            .map(path -> uriBuilder.cloneBuilder().path(path).toUriString())
            .collect(Collectors.joining("\n"));
        return ResponseEntity.ok(body.getBytes(StandardCharsets.UTF_8));
    }

    private String toPath(BasicPage basicPage) {
        PageModel pageModel = ALL_PAGE_MODELS_BY_NAME.get(basicPage.getModelName());
        return pageModel == null ? null : pageModel.toPath(basicPage.getName()).orElse(null);
    }
}
