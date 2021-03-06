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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by Maximilien on 20/03/2015.
 */
@Controller
@RequestMapping("stopoff")
@Slf4j
public class StopOffController {

    private final StopOffService stopOffService;

    private ModelAndView processStopOff(@Valid @ModelAttribute("stopOff") StopOff stopOff,
                                        BindingResult bindingResult,
                                        List<String> errors,
                                        List<String> notifications) {
        ModelAndView mNv = new ModelAndView("stopoff/stopoff_edit");

        if(bindingResult.hasErrors()) {
            errors.add("Impossible de mettre à jour le trajet");
        } else {
            try {
                stopOffService.updateStopOff(stopOff);
                notifications.add("Trajet mis à jour.");
            } catch (Exception e) {
                log.warn("Impossible de mettre à jour le trajet", e);

                errors.add(e.getMessage());
            }
        }

        mNv.addAllObjects(bindingResult.getModel());
        return mNv;
    }

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

    @RequestMapping(value = "/status/paiement", method = RequestMethod.GET)
    public String statusPaiement(@RequestParam("stopOffId") Long stopOffId,
                             @RequestParam("passengerId") String username,
                             @RequestParam("status") StopOffReservation.Status status,
                             Principal principal,
                             final RedirectAttributes redirectAttributes) {
        StopOffReservation reservation = stopOffService.changePaiementStatus(stopOffId, username, principal.getName(), status);

        redirectAttributes.addFlashAttribute("notifications",
                Collections.singletonList("Changement de statut du paiement par l'utilisateur " + username + " effectué."));

        return "redirect:/profile/journeys?type=driver&driverStoffOffid="+reservation.getStopOff().getJourney().getId();
    }

    @RequestMapping(value = "/edit/{stopOffId}", method = RequestMethod.GET)
    public ModelAndView editStopoff(@PathVariable("stopOffId") Long stopOffId) {

        ModelAndView mNv = new ModelAndView("stopoff/stopoff_edit");

        mNv.addObject("stopOff", stopOffService.getOne(stopOffId));

        return mNv;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ModelAndView updateStopoff(@Valid @ModelAttribute("stopOff") StopOff stopOff,
                                      BindingResult bindingResult) throws IllegalStateException {

        List<String> errors = new ArrayList<>();
        List<String> notifications = new ArrayList<>();
        ModelAndView modelAndView = processStopOff(stopOff, bindingResult, errors, notifications);
        modelAndView.addObject("errors", errors);
        modelAndView.addObject("notifications", notifications);
        return modelAndView;
    }

    @RequestMapping(value = "/validate", method = RequestMethod.POST)
    public ModelAndView validateStopoff(@Valid @ModelAttribute("stopOff") StopOff stopOff,
                                      BindingResult bindingResult) throws IllegalStateException {

        List<String> errors = new ArrayList<>();
        List<String> notifications = new ArrayList<>();

        if(stopOff.getPrice() > 0 && stopOff.getAvailableSeats() > 0) {
            stopOff.setStatus(StopOff.Status.ACTIVATED);
            notifications.add("Statut du trajet mis à jour");
        } else  {
            errors.add("Le prix ou le nombre de place disponible est inférieur ou égal à 0");
        }

        ModelAndView modelAndView = processStopOff(stopOff, bindingResult, errors, notifications);
        modelAndView.addObject("errors", errors);
        modelAndView.addObject("notifications", notifications);
        return modelAndView;
    }

}
