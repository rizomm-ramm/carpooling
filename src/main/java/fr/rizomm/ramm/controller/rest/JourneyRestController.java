package fr.rizomm.ramm.controller.rest;

import fr.rizomm.ramm.model.Journey;
import fr.rizomm.ramm.service.JourneyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/journey")
@Slf4j
public class JourneyRestController {
    private final JourneyService journeyService;

    @Autowired
    public JourneyRestController(JourneyService journeyService) {
        this.journeyService = journeyService;
    }

    @RequestMapping("/")
    public List<Journey> findAll() {
        log.debug("Find all journeys");
        return journeyService.findAll();
    }
}
