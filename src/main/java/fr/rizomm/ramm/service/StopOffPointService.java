package fr.rizomm.ramm.service;


import fr.rizomm.ramm.model.StopOffPoint;

import java.util.List;

/**
 * Created by Maximilien on 19/03/2015.
 */
public interface StopOffPointService {
    List<StopOffPoint> findAll();

    List<StopOffPoint> findByType(StopOffPoint.Type type);
}
