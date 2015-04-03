package fr.rizomm.ramm.service;


import fr.rizomm.ramm.model.StopOffPoint;
import fr.rizomm.ramm.model.StopOffReservation;
import fr.rizomm.ramm.model.StopOffReservationId;

import java.util.List;

/**
 * Created by Maximilien on 19/03/2015.
 */
public interface StopOffReservationService {
    List<StopOffReservation> findAll();

    StopOffReservation getOne(StopOffReservationId stopOffReservationId);

    StopOffReservation saveAndFlush(StopOffReservation stopOff);
}
