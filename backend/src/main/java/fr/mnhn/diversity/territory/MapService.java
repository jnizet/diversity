package fr.mnhn.diversity.territory;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.mnhn.diversity.model.Page;
import fr.mnhn.diversity.model.PageRepository;
import fr.mnhn.diversity.model.PageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MapService {

    private final PageRepository pageRepository;
    private final PageService pageService;

    public MapService(PageRepository pageRepository, PageService pageService) {
        this.pageRepository = pageRepository;
        this.pageService = pageService;
    }

    public List<MapTerritoryCard> getTerritoryCards() {
        Map<String, Page> territoryPagesByName =
            pageRepository.findByModel(TerritoryModel.TERRITORY_PAGE_MODEL.getName())
                          .stream()
                          .collect(Collectors.toMap(Page::getName, Function.identity()));

        return Stream.of(Territory.values())
                     .filter(territory -> territoryPagesByName.containsKey(territory.getSlug()))
                     .map(territory ->
                              new MapTerritoryCard(
                                  territory,
                                  pageService.buildPageContent(TerritoryModel.TERRITORY_PAGE_MODEL,
                                                               territoryPagesByName.get(territory.getSlug()))
                              )
                     )
                     .collect(Collectors.toList());
    }
}
