package fr.rizomm.ramm.service;


import fr.rizomm.ramm.form.JourneyForm;
import fr.rizomm.ramm.model.Journey;

import java.util.List;

/**
 * Created by Maximilien on 19/03/2015.
 */
public interface JourneyService {
    List<Journey> findAll();

    void createJourney(JourneyForm form, String username) throws Exception;

}
