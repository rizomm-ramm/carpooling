package fr.rizomm.ramm.service.impl;

import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixElementStatus;
import com.google.maps.model.DistanceMatrixRow;
import fr.rizomm.ramm.model.Journey;
import fr.rizomm.ramm.model.StopOff;
import fr.rizomm.ramm.repositories.StopOffRepository;
import fr.rizomm.ramm.service.StopOffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@Component("stopOffService")
@Transactional
public class StopOffServiceImpl implements StopOffService {
    private final StopOffRepository stopOffRepository;

    @Autowired
    public StopOffServiceImpl(StopOffRepository stopOffRepository) {
        this.stopOffRepository = stopOffRepository;
    }

    @Override
    public List<StopOff> findAll() {
        return stopOffRepository.findAll();
    }
}
