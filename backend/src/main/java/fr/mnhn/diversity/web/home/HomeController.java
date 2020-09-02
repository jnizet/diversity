package fr.mnhn.diversity.web.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * The Controller for the home page
 * @author JB Nizet
 */
@Controller
@RequestMapping("/")
public class HomeController {
    @GetMapping
    public ModelAndView home() {
        return new ModelAndView("home");
    }
}
