package fr.rizomm.ramm.controller;

import fr.rizomm.ramm.form.SimpleJourneyForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class HomeController {

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("journeyForm", new SimpleJourneyForm());
        return "index";
    }

}