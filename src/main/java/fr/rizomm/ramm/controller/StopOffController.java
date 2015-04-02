package fr.rizomm.ramm.controller;

import fr.rizomm.ramm.form.SimpleJourneyForm;
import fr.rizomm.ramm.model.StopOff;
import fr.rizomm.ramm.model.StopOffDistance;
import fr.rizomm.ramm.service.JourneyService;
import fr.rizomm.ramm.service.StopOffService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.security.Principal;
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


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ModelAndView viewStopOff(@PathVariable("id") Long id) {
        ModelAndView mNv = new ModelAndView("stopoff/item");
        mNv.addObject("stopOff", stopOffService.getOne(id));

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
            log.warn("Unable to update the backlog, there are some errors [{}]", results.getAllErrors());
            mNv.addAllObjects(results.getModel());

        } else {
            Map<StopOffDistance, StopOff> matchingStopOffs = stopOffService.findStopOffByLocation(simpleJourneyForm);

            log.info("Number of matching stopOffs [{}]", matchingStopOffs);

            mNv.addObject("matchingStopOffs", matchingStopOffs);
            mNv.addObject("journeyForm", simpleJourneyForm);
        }

        return mNv;
    }
}