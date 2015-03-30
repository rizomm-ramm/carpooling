package fr.rizomm.ramm.service;


import fr.rizomm.ramm.model.Journey;
import fr.rizomm.ramm.model.StopOff;
import fr.rizomm.ramm.model.StopOffPoint;

import java.util.List;

/**
 * Created by Maximilien on 19/03/2015.
 */
public interface StopOffService {
    List<StopOff> findAll();

    StopOff saveAndFlush(StopOff stopOff);

    StopOff createStopOff(Journey journey, StopOffPoint departure, StopOffPoint arrival) throws Exception;

    StopOff getOne(Long id);
}
