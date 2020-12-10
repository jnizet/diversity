package fr.mnhn.diversity;

import fr.mnhn.diversity.model.PageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.AbstractTemplateResolver;
import org.thymeleaf.templateresolver.DefaultTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolution;

@SpringBootTest
@ActiveProfiles("test")
class DiversityApplicationTest {


    @Test
    void contextLoads() {

    }
}

