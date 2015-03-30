package fr.rizomm.ramm.service.impl;

import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixElementStatus;
import com.google.maps.model.DistanceMatrixRow;
import fr.rizomm.ramm.form.SimpleJourneyForm;
import fr.rizomm.ramm.model.Journey;
import fr.rizomm.ramm.model.StopOff;
import fr.rizomm.ramm.model.StopOffDistance;
import fr.rizomm.ramm.model.StopOffPoint;
import fr.rizomm.ramm.repositories.StopOffRepository;
import fr.rizomm.ramm.service.GeoService;
import fr.rizomm.ramm.service.StopOffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

@Component("stopOffService")
@Transactional
public class StopOffServiceImpl implements StopOffService {
    private final StopOffRepository stopOffRepository;
    private final GeoService geoService;

    @Autowired
    public StopOffServiceImpl(StopOffRepository stopOffRepository,
                              GeoService geoService) {
        this.stopOffRepository = stopOffRepository;
        this.geoService = geoService;
    }

    @Override
    public List<StopOff> findAll() {
        return stopOffRepository.findAll();
    }

    @Override
    public StopOff saveAndFlush(StopOff stopOff) {
        return stopOffRepository.saveAndFlush(stopOff);
    }

    @Override
    public StopOff createStopOff(Journey journey, StopOffPoint departure, StopOffPoint arrival) throws Exception {

        DistanceMatrix distance = geoService.distance(departure.getAddress(), arrival.getAddress());

        Long minDistance = null;

        // Search for the minimum distance
        for (DistanceMatrixRow row : distance.rows) {
            for (DistanceMatrixElement element : row.elements) {
                if(element.status.equals(DistanceMatrixElementStatus.OK)) {
                    if(minDistance == null || minDistance > element.distance.inMeters) {
                        minDistance = element.distance.inMeters;
                    }
                }
            }
        }

        checkNotNull(minDistance, "Distance has not been found");

        StopOff stopOff = StopOff.builder()
                .departurePoint(departure)
                .arrivalPoint(arrival)
                .journey(journey)
                .distance(minDistance)
                .build();

        return stopOffRepository.saveAndFlush(stopOff);
    }

    @Override
    public StopOff getOne(Long id) {
        return stopOffRepository.getOne(id);
    }

    @Override
    public Map<Double, StopOff> findStopOffByLocation(double lat, double lng, double distanceMax) {
        List<StopOff> stopOffs = findAll();

        Map<Double, StopOff> matchingStopOffWithDistance = new TreeMap<>();

        stopOffs.forEach(stopOff -> {
            Double distanceToDeparture = geoService.calculateDistance(stopOff.getDeparturePoint().getLatitude(), stopOff.getDeparturePoint().getLongitude(),
                    lat, lng);

            if (distanceToDeparture <= distanceMax) {
                matchingStopOffWithDistance.put(distanceToDeparture, stopOff);
            }

        });

        return matchingStopOffWithDistance;
    }

    @Override
    public Map<StopOffDistance, StopOff> findStopOffByLocation(SimpleJourneyForm journeyForm) {
        List<StopOff> stopOffs = findAll();

        Map<StopOffDistance, StopOff> matchingStopOffWithDistance = new TreeMap<>();

        stopOffs.forEach(stopOff -> {
            Double distanceToDeparture = geoService.calculateDistance(stopOff.getDeparturePoint().getLatitude(), stopOff.getDeparturePoint().getLongitude(),
                    journeyForm.getDeparture().getLatitude(), journeyForm.getDeparture().getLongitude());

            Double distanceToArrival = geoService.calculateDistance(stopOff.getArrivalPoint().getLatitude(), stopOff.getArrivalPoint().getLongitude(),
                    journeyForm.getArrival().getLatitude(), journeyForm.getArrival().getLongitude());

            // Convert precision to meters
            if (distanceToDeparture <= (journeyForm.getDeparture().getPrecision() * 1000)
                    && distanceToArrival <= (journeyForm.getArrival().getPrecision() * 1000)) {
                matchingStopOffWithDistance.put(StopOffDistance.builder()
                                .departureDistance(distanceToDeparture)
                                .arrivalDistance(distanceToArrival)
                                .build(),
                        stopOff);
            }

        });

        return matchingStopOffWithDistance;
    }
}
