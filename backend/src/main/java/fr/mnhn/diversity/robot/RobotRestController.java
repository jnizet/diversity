package fr.mnhn.diversity.robot;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

import fr.mnhn.diversity.ecogesture.EcogestureModel;
import fr.mnhn.diversity.indicator.IndicatorModel;
import fr.mnhn.diversity.model.PageRepository;
import fr.mnhn.diversity.territory.TerritoryModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<byte[]> robots(HttpServletRequest request) {
        String body =
            "User-agent: *\n" +
            "Disallow: /admin/\n" +
            "Disallow: /mentions-legales\n\n" +
            "Sitemap: " +
            request.getRequestURL().toString().replace("/robots.txt", "/sitemap.txt") +
            "\n";

        return ResponseEntity.ok(body.getBytes(StandardCharsets.UTF_8));
    }

    @GetMapping(value = "/sitemap.txt", produces = MediaType.TEXT_PLAIN_VALUE)
    @Transactional
    public ResponseEntity<byte[]> sitemap(HttpServletRequest request) {
        List<String> paths = new ArrayList<>();
        paths.add("/");
        paths.add("/apropos");
        paths.add("/ecogestes");
        paths.add("/territoires");
        paths.add("/indicateurs");
        paths.add("/agir-ensemble");
        paths.add("/sciences-participatives");

        paths.addAll(
            pageRepository.findBasicByModel(TerritoryModel.TERRITORY_PAGE_MODEL.getName())
                          .stream()
                          .map(page -> "/territoires/" + page.getName())
                          .collect(Collectors.toList())
        );
        paths.addAll(
            pageRepository.findBasicByModel(IndicatorModel.INDICATOR_PAGE_MODEL.getName())
                          .stream()
                          .map(page -> "/indicateurs/" + page.getName())
                          .collect(Collectors.toList())
        );
        paths.addAll(
            pageRepository.findBasicByModel(EcogestureModel.ECO_GESTURE_PAGE_MODEL.getName())
                          .stream()
                          .map(page -> "/ecogestes/" + page.getName())
                          .collect(Collectors.toList())
        );

        String prefix = request.getRequestURL().toString().replace("/sitemap.txt", "");
        String body = paths.stream()
            .map(path -> prefix + path)
            .collect(Collectors.joining("\n"));
        return ResponseEntity.ok(body.getBytes(StandardCharsets.UTF_8));
    }
}
