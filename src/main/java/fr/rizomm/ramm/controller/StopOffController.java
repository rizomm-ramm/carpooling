package fr.rizomm.ramm.controller;

import fr.rizomm.ramm.form.BookSeatForm;
import fr.rizomm.ramm.form.SimpleJourneyForm;
import fr.rizomm.ramm.model.StopOff;
import fr.rizomm.ramm.model.StopOffDistance;
import fr.rizomm.ramm.model.StopOffReservation;
import fr.rizomm.ramm.service.StopOffService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Collections;
import java.util.Map;

/**
 * Created by Maximilien on 20/03/2015.
 */
@Controller
@RequestMapping("stopoff")
@Slf4j
public class StopOffController {

    private final StopOffService stopOffService;

    @Autowired
    public StopOffController(StopOffService stopOffService) {
        this.stopOffService = stopOffService;
    }


    @RequestMapping(value = "/item/{id}", method = RequestMethod.GET)
    public ModelAndView viewStopOff(@PathVariable("id") Long id) {
        ModelAndView mNv = new ModelAndView("stopoff/item");
        mNv.addObject("stopOff", stopOffService.getOne(id));
        mNv.addObject("bookSeatForm",  new BookSeatForm());
        return mNv;
    }

    @RequestMapping(value = "/search/byDeparture", method = RequestMethod.GET)
    public ModelAndView searchByDeparture(@RequestParam("lat") Double lat,
                               @RequestParam("lng") Double lng,
                               @RequestParam(value = "distanceMax", defaultValue = "10000") Double distanceMax) {
        Map<Double, StopOff> matchingStopOffs = stopOffService.findStopOffByLocation(lat, lng, distanceMax);

        log.info("Number of matching stopOffs [{}]", matchingStopOffs);

        ModelAndView mNv = new ModelAndView("stopoff/searchResult");
        mNv.addObject("matchingStopOffs", matchingStopOffs);

        return mNv;
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ModelAndView search(@Valid @ModelAttribute("journeyForm") SimpleJourneyForm simpleJourneyForm, BindingResult results) {
        ModelAndView mNv = new ModelAndView("stopoff/searchResult");

        if(results.hasErrors()) {
            log.warn("Impossible de rechercher le trajet, erreurs : [{}]", results.getAllErrors());
            mNv.addAllObjects(results.getModel());

        } else {
            Map<StopOffDistance, StopOff> matchingStopOffs = stopOffService.findStopOffByLocation(simpleJourneyForm);

            log.info("Number of matching stopOffs [{}]", matchingStopOffs);

            mNv.addObject("matchingStopOffs", matchingStopOffs);
            mNv.addObject("journeyForm", simpleJourneyForm);
            mNv.addObject("bookSeatForm", new BookSeatForm());
        }

        return mNv;
    }

    @RequestMapping(value = "/book", method = RequestMethod.POST)
    public String book(@Valid @ModelAttribute("bookSeatForm") BookSeatForm bookSeatForm,
                             BindingResult results,
                             Principal principal,
                            final RedirectAttributes redirectAttributes) {

        if(results.hasErrors()) {
            log.warn("Impossible de rechercher le trajet, erreurs : [{}]", results.getAllErrors());
            redirectAttributes.addFlashAttribute("errors", Collections.singletonList("Impossible d'effectuer la réservation"));
            return "redirect:/stopoff/item/"+bookSeatForm.getStopOffId();
        } else {
            StopOffReservation reservation = stopOffService.book(bookSeatForm, principal.getName());
            redirectAttributes.addFlashAttribute("notifications",
                    Collections.singletonList("Demande enregistrée, en attente de validation du conducteur"));

            return "redirect:/profile/journeys?type=passenger&passengerStoffOffid="+reservation.getStopOff().getJourney().getId();
        }
    }

    @RequestMapping(value = "/status/user", method = RequestMethod.GET)
    public String statusUser(@RequestParam("stopOffId") Long stopOffId,
                             @RequestParam("passengerId") String username,
                             @RequestParam("status") StopOffReservation.Status status,
                             Principal principal,
                             final RedirectAttributes redirectAttributes) {
        StopOffReservation reservation = stopOffService.changeReservationStatus(stopOffId, username, principal.getName(), status);

        redirectAttributes.addFlashAttribute("notifications",
                Collections.singletonList("Changement de statut de l'utilisateur " + username + " effectué."));

        return "redirect:/profile/journeys?type=driver&driverStoffOffid="+reservation.getStopOff().getJourney().getId();
    }

    @RequestMapping(value = "/edit/{stopOffId}", method = RequestMethod.GET)
    public ModelAndView editStopoff(@PathVariable("stopOffId") Long stopOffId) {

        ModelAndView mNv = new ModelAndView("stopoff/stopoff_edit");

        mNv.addObject("stopOff", stopOffService.getOne(stopOffId));

        return mNv;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ModelAndView updateStopoff(@ModelAttribute("stopOff") StopOff stopOff,
                                      BindingResult bindingResult) {

        ModelAndView mNv = new ModelAndView("stopoff/stopoff_edit");

        mNv.addObject("stopOff", stopOff);

        return mNv;
    }

}
