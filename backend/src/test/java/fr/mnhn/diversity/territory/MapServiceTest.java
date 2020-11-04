package fr.mnhn.diversity.territory;

import static fr.mnhn.diversity.model.testing.ModelTestingUtil.text;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import fr.mnhn.diversity.model.Page;
import fr.mnhn.diversity.model.PageContent;
import fr.mnhn.diversity.model.PageRepository;
import fr.mnhn.diversity.model.PageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MapServiceTest {
    private PageRepository mockPageRepository;
    private PageService mockPageService;

    private MapService mapService;

    private PageContent reunionPageContent;

    @BeforeEach
    void prepare() {
        mockPageRepository = mock(PageRepository.class);
        mockPageService = mock(PageService.class);
        mapService = new MapService(mockPageRepository, mockPageService);

        String territoryModelName = TerritoryModel.TERRITORY_PAGE_MODEL.getName();
        Page reunionPage = new Page(43L, Territory.REUNION.getSlug(), territoryModelName, "", List.of());
        when(mockPageRepository.findByModel(TerritoryModel.TERRITORY_PAGE_MODEL.getName()))
            .thenReturn(List.of(reunionPage));

        reunionPageContent = new PageContent(
            reunionPage,
            Map.of(
                "identity", Map.of(
                    "title", text("La Réunion"),
                    "presentation", text("presentation of reunion")
                )
            )

        );
        when(mockPageService.buildPageContent(TerritoryModel.TERRITORY_PAGE_MODEL, reunionPage)).thenReturn(
            reunionPageContent
        );
    }

    @Test
    void shouldReturnTerritoryCards()  {
        List<MapTerritoryCard> territoryCards = mapService.getTerritoryCards();
        assertThat(territoryCards).containsExactly(
            new MapTerritoryCard(Territory.REUNION, reunionPageContent)
        );
    }
}
