package fr.rizomm.ramm.controller;

import com.google.common.collect.ImmutableList;
import fr.rizomm.ramm.form.SimpleJourneyForm;
import fr.rizomm.ramm.model.Journey;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

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

    @RequestMapping(value = "/initialize", method = RequestMethod.POST)
    public ModelAndView initializeJourney(@Valid @ModelAttribute("journeyForm") SimpleJourneyForm simpleJourneyForm,
                                          BindingResult results,
                                          Principal principal,
                                          RedirectAttributes redirectAttributes) {
        ModelAndView mNv = new ModelAndView("index");
        if(results.hasErrors()) {
            log.warn("Unable to update the backlog, there are some errors [{}]", results.getAllErrors());
            mNv.addAllObjects(results.getModel());

        } else {
            log.info("New journey to create [{}]", simpleJourneyForm);
            try {
                Journey journey = journeyService.createJourney(simpleJourneyForm, principal.getName());

                List<String> notifications = ImmutableList.of("Votre trajet a été initialisé.",
                        "Remplissez le nombre de places disponibles et le prix pour pouvoir l'activer",
                        "Une fois activé, il apparaîtra dans la recherche");
                redirectAttributes.addFlashAttribute("notifications", notifications);

                String redirectUrl = String.format("/profile/journeys?type=driver&driverStoffOffid=%d", journey.getId());
                mNv = new ModelAndView(new RedirectView(redirectUrl, true));
            } catch (Exception e) {
                log.warn("Unable to create the jouney {}", simpleJourneyForm, e);
                mNv.addAllObjects(results.getModel());
                results.addError(new ObjectError("global", "Impossible de créer votre trajet"));
            }

        }

        return mNv;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ModelAndView journey(@PathVariable("id") Long id) {
        ModelAndView mNv = new ModelAndView("journey/item");
        mNv.addObject("journey", journeyService.getOne(id));

        return mNv;
    }
}
