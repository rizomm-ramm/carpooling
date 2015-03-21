package fr.rizomm.ramm.controller;

import fr.rizomm.ramm.form.JourneyForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class HomeController {

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("journeyForm", new JourneyForm());
        return "index";
    }

    @RequestMapping("/private")
    public String privateIndex() {
        return "private";
    }

}