package fr.rizomm.ramm.controller.rest;

import fr.rizomm.ramm.model.StopOffPoint;
import fr.rizomm.ramm.service.StopOffPointService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/stopoff/point")
@Slf4j
public class StopOffPointRestController {
    private final StopOffPointService stopOffPointService;

    @Autowired
    public StopOffPointRestController(StopOffPointService stopOffPointService) {
        this.stopOffPointService = stopOffPointService;
    }

    @RequestMapping("/")
    public List<StopOffPoint> findAll() {
        log.debug("Find all stop points");
        return stopOffPointService.findAll();
    }

    @RequestMapping("/type")
    public List<StopOffPoint> findByType(@RequestParam("value") StopOffPoint.Type type) {
        log.debug("Find all stop points");
        return stopOffPointService.findByType(type);
    }
}
