package fr.mnhn.diversity.territory;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/map")
public class MapRestController {
    @GetMapping
    public MapDTO get() {
        return MapDTO.INSTANCE;
    }
}
