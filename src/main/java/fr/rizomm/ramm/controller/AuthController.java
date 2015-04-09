package fr.rizomm.ramm.controller;

import fr.rizomm.ramm.form.BookSeatForm;
import fr.rizomm.ramm.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Collections;

@Controller
@Slf4j
public class AuthController {

    public static final String NOT_CONNECTED = "Vous n'êtes pas connecté.";
    public static final String INVALID_CREDENTIALS = "Username ou mot de passe invalide!";
    public static final String LOGOUT = "Vous avez été déconnecté.";
    public static final String REGISTRATION_OK = "Vous pouvez maintenant vous connecter avec le compte ";
    public static final String REGISTRATION_ERROR = "Erreur lors de l'enregistrement : ";

    private final UserService userService;


    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(@RequestParam(value = "error", required = false) String error,
                              @RequestParam(value = "logout", required = false) String logout,
                              @ModelAttribute(value = "j_username") String username,
                              @ModelAttribute(value = "j_password") String password) {

        ModelAndView model = new ModelAndView();
        if (error != null) {
            model.addObject("error", INVALID_CREDENTIALS);
        }

        if (logout != null) {
            model.addObject("msg", LOGOUT);
        }
        model.setViewName("login");
        model.addObject("j_username", username);
        model.addObject("j_password", password);

        return model;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@RequestParam("j_username") String username,
                           @RequestParam("j_password") String password,
                           RedirectAttributes redirectAttributes) {

        try {
            userService.register(username, password);
            redirectAttributes.addFlashAttribute("j_username", username);
            redirectAttributes.addFlashAttribute("j_password", password);

            redirectAttributes.addFlashAttribute("notifications",
                    Collections.singletonList(REGISTRATION_OK + username));
        } catch (Exception e) {
            log.error("Unable to register the user {}", username, e);
            redirectAttributes.addFlashAttribute("errors", Collections.singletonList(REGISTRATION_ERROR + e.getMessage()));
        }

        return "redirect:/login";
    }

    @RequestMapping(value = {"/profile", "/profile/informations"}, method = RequestMethod.GET)
    public ModelAndView profileInformations(Principal principal) {
        if(principal == null) {
            throw new IllegalStateException(NOT_CONNECTED);
        }

        ModelAndView mNv = new ModelAndView("profile/informations");
        mNv.addObject("user", userService.getOne(principal.getName()));

        return mNv;
    }

    @RequestMapping(value = "/profile/journeys", method = RequestMethod.GET)
    public ModelAndView profileJourneys(Principal principal) {
        if(principal == null) {
            throw new IllegalStateException(NOT_CONNECTED);
        }

        ModelAndView mNv = new ModelAndView("profile/journeys");
        mNv.addObject("user", userService.getOne(principal.getName()));
        mNv.addObject("bookSeatForm", new BookSeatForm());

        return mNv;
    }

    @RequestMapping(value = "/profile/messages", method = RequestMethod.GET)
    public ModelAndView profileMessages(Principal principal) {
        if(principal == null) {
            throw new IllegalStateException(NOT_CONNECTED);
        }

        ModelAndView mNv = new ModelAndView("profile/messages");
        mNv.addObject("user", userService.getOne(principal.getName()));

        return mNv;
    }

}
