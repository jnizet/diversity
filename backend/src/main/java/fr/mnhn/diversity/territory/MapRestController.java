package fr.mnhn.diversity.territory;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.mnhn.diversity.model.BasicPage;
import fr.mnhn.diversity.model.PageRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/map")
@Transactional
public class MapRestController {

    private final PageRepository pageRepository;

    public MapRestController(PageRepository pageRepository) {
        this.pageRepository = pageRepository;
    }

    @GetMapping
    public MapDTO get() {
        Set<String> activeTerritorySlugs =
            pageRepository.findBasicByModel(TerritoryModel.TERRITORY_PAGE_MODEL.getName())
                          .stream()
                          .map(BasicPage::getName)
                          .collect(Collectors.toSet());

        return new MapDTO(
            Stream.of(Zone.values()).map(ZoneDTO::new).collect(Collectors.toList()),
            Stream.of(Territory.values())
                  .filter(t -> t != Territory.OUTRE_MER)
                  .map(t -> new TerritoryDTO(t, activeTerritorySlugs.contains(t.getSlug())))
                  .collect(Collectors.toList())
        );
    }
}
