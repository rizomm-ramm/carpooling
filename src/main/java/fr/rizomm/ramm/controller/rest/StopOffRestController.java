package fr.rizomm.ramm.controller.rest;

import fr.rizomm.ramm.model.StopOff;
import fr.rizomm.ramm.service.StopOffService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/stopoff")
@Slf4j
public class StopOffRestController {
    private final StopOffService stopOffService;

    @Autowired
    public StopOffRestController(StopOffService stopOffService) {
        this.stopOffService = stopOffService;
    }

    @RequestMapping("/")
    public List<StopOff> findAll() {
        log.debug("Find all stop");
        return stopOffService.findAll();
    }
}
