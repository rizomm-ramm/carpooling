package fr.rizomm.ramm.service.impl;

import fr.rizomm.ramm.model.StopOffPoint;
import fr.rizomm.ramm.repositories.StopOffPointRepository;
import fr.rizomm.ramm.service.StopOffPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component("stopOffPointService")
@Transactional
public class StopOffPointServiceImpl implements StopOffPointService {
    private final StopOffPointRepository stopOffPointRepository;

    @Autowired
    public StopOffPointServiceImpl(StopOffPointRepository stopOffPointRepository) {
        this.stopOffPointRepository = stopOffPointRepository;
    }

    @Override
    public List<StopOffPoint> findAll() {
        return stopOffPointRepository.findAll();
    }

    @Override
    public List<StopOffPoint> findByType(StopOffPoint.Type type) {
        return stopOffPointRepository.findByType(type);
    }

}
