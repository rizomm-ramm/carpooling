package fr.rizomm.ramm.service.impl;

import fr.rizomm.ramm.model.StopOffReservation;
import fr.rizomm.ramm.model.StopOffReservationId;
import fr.rizomm.ramm.repositories.StopOffReservationRepository;
import fr.rizomm.ramm.service.StopOffReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component("stopOffReservationService")
@Transactional
public class StopOffReservationServiceImpl implements StopOffReservationService {
    private final StopOffReservationRepository stopOffReservationRepository;

    @Autowired
    public StopOffReservationServiceImpl(StopOffReservationRepository stopOffReservationRepository) {
        this.stopOffReservationRepository = stopOffReservationRepository;
    }


    @Override
    public List<StopOffReservation> findAll() {
        return stopOffReservationRepository.findAll();
    }

    @Override
    public StopOffReservation getOne(StopOffReservationId stopOffReservationId) {
        return stopOffReservationRepository.getOne(stopOffReservationId);
    }

    @Override
    public StopOffReservation saveAndFlush(StopOffReservation stopOff) {
        return stopOffReservationRepository.saveAndFlush(stopOff);
    }
}
