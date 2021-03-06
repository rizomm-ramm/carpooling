package fr.rizomm.ramm.service;


import fr.rizomm.ramm.form.SimpleJourneyForm;
import fr.rizomm.ramm.model.Journey;

import java.util.List;

/**
 * Created by Maximilien on 19/03/2015.
 */
public interface JourneyService {
    List<Journey> findAll();

    Journey createJourney(SimpleJourneyForm form, String username) throws Exception;

    Journey getOne(Long id);
}
