package fr.rizomm.ramm.controller;

import fr.rizomm.ramm.form.BookSeatForm;
import fr.rizomm.ramm.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.security.Principal;
import java.util.Collections;

@Controller
@Slf4j
public class AuthController {

    public static final String NOT_CONNECTED = "Vous n'êtes pas connecté.";
    private final UserService userService;
    private final AuthenticationManager authenticationManager;


    @Autowired
    public AuthController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(@RequestParam(value = "error", required = false) String error,
                              @RequestParam(value = "logout", required = false) String logout,
                              @ModelAttribute(value = "j_username") String username,
                              @ModelAttribute(value = "j_password") String password) {

        ModelAndView model = new ModelAndView();
        if (error != null) {
            model.addObject("error", "Invalid username and password!");
        }

        if (logout != null) {
            model.addObject("msg", "You've been logged out successfully.");
        }
        model.setViewName("login");
        model.addObject("j_username", username);
        model.addObject("j_password", password);

        return model;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@RequestParam("j_username") @NotEmpty @Min(5) String username,
                           @Valid @RequestParam("j_password") @NotEmpty @Min(5) String password,
                           HttpServletRequest request,
                           RedirectAttributes redirectAttributes) {

        try {
            userService.register(username, password);
            redirectAttributes.addFlashAttribute("j_username", username);
            redirectAttributes.addFlashAttribute("j_password", password);

            redirectAttributes.addFlashAttribute("notifications",
                    Collections.singletonList("Vous pouvez maintenant vous connecter avec le compte " + username));
        } catch (Exception e) {
            log.error("Unable to register the user {}", username, e);
            redirectAttributes.addFlashAttribute("errors", Collections.singletonList("Erreur survenue : " + e.getMessage()));
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
