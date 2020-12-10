package fr.mnhn.diversity;

import fr.mnhn.diversity.common.thymeleaf.BiomDialect;
import fr.mnhn.diversity.common.thymeleaf.RequestDialect;
import fr.mnhn.diversity.matomo.MatomoConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;

@TestInstance(Lifecycle.PER_CLASS)
@Import({RequestDialect.class, MatomoConfig.class})
public class ControllerTest {

    @Autowired
    public ITemplateEngine templateEngine;

    @BeforeAll
    void initTemplateEngine() {
        ((SpringTemplateEngine)templateEngine).addDialect(new BiomDialect());
    }
}
