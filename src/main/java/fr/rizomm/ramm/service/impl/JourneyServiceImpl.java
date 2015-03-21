package fr.rizomm.ramm.service.impl;

import fr.rizomm.ramm.form.JourneyForm;
import fr.rizomm.ramm.model.Journey;
import fr.rizomm.ramm.model.StopOffPoint;
import fr.rizomm.ramm.model.User;
import fr.rizomm.ramm.repositories.JourneyRepository;
import fr.rizomm.ramm.service.JourneyService;
import fr.rizomm.ramm.service.StopOffService;
import fr.rizomm.ramm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component("journeyService")
@Transactional
public class JourneyServiceImpl implements JourneyService {
    private final JourneyRepository journeyRepository;
    private final UserService userService;
    private final StopOffService stopOffService;

    @Autowired
    public JourneyServiceImpl(JourneyRepository journeyRepository,
                              UserService userService,
                              StopOffService stopOffService) {
        this.journeyRepository = journeyRepository;
        this.userService = userService;
        this.stopOffService = stopOffService;
    }

    @Override
    public List<Journey> findAll() {
        return journeyRepository.findAll();
    }

    @Override
    public void createJourney(JourneyForm form, String username) throws Exception {
        User user = userService.getOne(username);

        Journey journey = Journey.builder().user(user).build();

        JourneyForm.Address departureAddress = form.getDeparture();
        StopOffPoint departurePoint = StopOffPoint.builder()
                .address(departureAddress.getAddress())
                .latitude(departureAddress.getLatitude())
                .longitude(departureAddress.getLongitude())
                .build();

        JourneyForm.Address arrivalAddress = form.getArrival();
        StopOffPoint arrivalPoint = StopOffPoint.builder()
                .address(arrivalAddress.getAddress())
                .latitude(arrivalAddress.getLatitude())
                .longitude(arrivalAddress.getLongitude())
                .build();

        stopOffService.createStopOff(journey, departurePoint, arrivalPoint);
    }
}
