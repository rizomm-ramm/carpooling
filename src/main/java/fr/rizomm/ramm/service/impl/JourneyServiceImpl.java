package fr.rizomm.ramm.service.impl;

import fr.rizomm.ramm.model.Journey;
import fr.rizomm.ramm.repositories.JourneyRepository;
import fr.rizomm.ramm.service.JourneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component("journeyService")
@Transactional
public class JourneyServiceImpl implements JourneyService {
    private final JourneyRepository journeyRepository;

    @Autowired
    public JourneyServiceImpl(JourneyRepository journeyRepository) {
        this.journeyRepository = journeyRepository;
    }

    @Override
    public List<Journey> findAll() {
        return journeyRepository.findAll();
    }
}
