package fr.rizomm.ramm.controller;

import fr.rizomm.ramm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(@RequestParam(value = "error", required = false) String error,
                              @RequestParam(value = "logout", required = false) String logout) {

        ModelAndView model = new ModelAndView();
        if (error != null) {
            model.addObject("error", "Invalid username and password!");
        }

        if (logout != null) {
            model.addObject("msg", "You've been logged out successfully.");
        }
        model.setViewName("login");

        return model;
    }

    @RequestMapping(value = {"/profile", "/profile/informations"}, method = RequestMethod.GET)
    public ModelAndView profileInformations(Principal principal) {
        if(principal == null) {
            throw new IllegalStateException("Vous n'êtes pas connecté.");
        }

        ModelAndView mNv = new ModelAndView("profile/informations");
        mNv.addObject("user", userService.getOne(principal.getName()));

        return mNv;
    }

    @RequestMapping(value = "/profile/journeys", method = RequestMethod.GET)
    public ModelAndView profileJourneys(Principal principal) {
        if(principal == null) {
            throw new IllegalStateException("Vous n'êtes pas connecté.");
        }

        ModelAndView mNv = new ModelAndView("profile/journeys");
        mNv.addObject("user", userService.getOne(principal.getName()));

        return mNv;
    }

    @RequestMapping(value = "/profile/messages", method = RequestMethod.GET)
    public ModelAndView profileMessages(Principal principal) {
        if(principal == null) {
            throw new IllegalStateException("Vous n'êtes pas connecté.");
        }

        ModelAndView mNv = new ModelAndView("profile/messages");
        mNv.addObject("user", userService.getOne(principal.getName()));

        return mNv;
    }

}
