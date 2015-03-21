package fr.rizomm.ramm.controller;

import fr.rizomm.ramm.form.JourneyForm;
import fr.rizomm.ramm.service.JourneyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.security.Principal;

/**
 * Created by Maximilien on 20/03/2015.
 */
@Controller
@RequestMapping("journey")
@Slf4j
public class JourneyController {

    private final JourneyService journeyService;

    @Autowired
    public JourneyController(JourneyService journeyService) {
        this.journeyService = journeyService;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView createJourney(@Valid @ModelAttribute JourneyForm journeyForm, BindingResult results, Principal principal) {
        ModelAndView mNv = new ModelAndView("index");
        if(results.hasErrors()) {
            log.warn("Unable to update the backlog, there are some errors [{}]", results.getAllErrors());
            mNv.addAllObjects(results.getModel());

        }
        else {
            log.info("New journey to create [{}]", journeyForm);
            try {
                journeyService.createJourney(journeyForm, principal.getName());
            } catch (Exception e) {
                mNv.addAllObjects(results.getModel());
                results.addError(new ObjectError("global", "Impossible de créer votre trajet"));
            }
            mNv = new ModelAndView(new RedirectView("/", true));
        }

        return mNv;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ModelAndView createJourney(@PathVariable("id") Long id) {
        ModelAndView mNv = new ModelAndView("journey/item");
        mNv.addObject("journey", journeyService.getOne(id));

        return mNv;
    }
}
