package fr.rizomm.ramm.service;


import fr.rizomm.ramm.form.BookSeatForm;
import fr.rizomm.ramm.form.SimpleJourneyForm;
import fr.rizomm.ramm.model.Journey;
import fr.rizomm.ramm.model.StopOff;
import fr.rizomm.ramm.model.StopOffDistance;
import fr.rizomm.ramm.model.StopOffPoint;

import java.util.List;
import java.util.Map;

/**
 * Created by Maximilien on 19/03/2015.
 */
public interface StopOffService {
    List<StopOff> findAll();

    StopOff saveAndFlush(StopOff stopOff);

    StopOff createStopOff(Journey journey, StopOffPoint departure, StopOffPoint arrival) throws Exception;

    StopOff getOne(Long id);

    Map<Double, StopOff> findStopOffByLocation(double lat, double lng, double distanceMax);

    Map<StopOffDistance, StopOff> findStopOffByLocation(SimpleJourneyForm journeyForm);

    void book(BookSeatForm bookSeatForm, String username);
}
