package fr.rizomm.ramm.service.impl;

import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixElementStatus;
import com.google.maps.model.DistanceMatrixRow;
import fr.rizomm.ramm.form.BookSeatForm;
import fr.rizomm.ramm.form.SimpleJourneyForm;
import fr.rizomm.ramm.model.Journey;
import fr.rizomm.ramm.model.StopOff;
import fr.rizomm.ramm.model.StopOffDistance;
import fr.rizomm.ramm.model.StopOffPoint;
import fr.rizomm.ramm.model.StopOffReservation;
import fr.rizomm.ramm.model.StopOffReservationId;
import fr.rizomm.ramm.model.User;
import fr.rizomm.ramm.repositories.StopOffRepository;
import fr.rizomm.ramm.service.GeoService;
import fr.rizomm.ramm.service.StopOffReservationService;
import fr.rizomm.ramm.service.StopOffService;
import fr.rizomm.ramm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
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
    private final UserService userService;
    private final StopOffReservationService stopOffReservationService;

    @Autowired
    public StopOffServiceImpl(StopOffRepository stopOffRepository,
                              GeoService geoService,
                              UserService userService,
                              StopOffReservationService stopOffReservationService) {
        this.stopOffRepository = stopOffRepository;
        this.geoService = geoService;
        this.userService = userService;
        this.stopOffReservationService = stopOffReservationService;
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
            if(stopOff.getDeparturePoint().getDate().compareTo(journeyForm.getDate()) != -1) {

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
            }
        });

        List<Map.Entry<StopOffDistance, StopOff>> list = new LinkedList<>(matchingStopOffWithDistance.entrySet());
        Collections.sort(list, (o1, o2) ->
                o1.getValue().getDeparturePoint().getDate().compareTo(o2.getValue().getDeparturePoint().getDate())
        );

        Map<StopOffDistance, StopOff> sortedMatchingStopOffWithDistance = new LinkedHashMap<>();

        list.forEach( stopOffDistanceStopOffEntry ->
                sortedMatchingStopOffWithDistance.put(stopOffDistanceStopOffEntry.getKey(), stopOffDistanceStopOffEntry.getValue())
        );
        return sortedMatchingStopOffWithDistance;
    }

    @Override
    public void book(BookSeatForm bookSeatForm, String username) {
        StopOff stopOff = getOne(bookSeatForm.getStopOffId());
        User user = userService.getOne(username);

        if(stopOff.numberOfRemainingReservation() < bookSeatForm.getSeats()) {
            throw new IllegalStateException("Plus assez de places disponibles");
        }

        StopOffReservation stopOffReservation = StopOffReservation.builder().pk(new StopOffReservationId(stopOff, user))
                .seats(bookSeatForm.getSeats())
                .comment(bookSeatForm.getComment())
                .status(StopOffReservation.Status.WAITING)
                .payed(false)
                .build();

        stopOff.getReservations().add(stopOffReservation);

        stopOffRepository.saveAndFlush(stopOff);
    }

    @Override
    public StopOffReservation changeReservationStatus(Long stopOffId, String passengerId, String loggedUser, StopOffReservation.Status status) {
        StopOff stopOff = getOne(stopOffId);

        if(!stopOff.getJourney().getUser().getUsername().equals(loggedUser)) {
            throw new IllegalStateException("Vous n'avez pas cr�� ce trajet.");
        }

        User passenger = userService.getOne(passengerId);

        StopOffReservation reservation = stopOffReservationService.getOne(new StopOffReservationId(stopOff, passenger));

        if(!StopOffReservation.Status.WAITING.equals(reservation.getStatus())) {
            throw new IllegalStateException("Le statut de la r�servation n'est plus en attente et ne peut plus �tre modifi�");
        }

        reservation.setStatus(status);

        return stopOffReservationService.saveAndFlush(reservation);
    }

}
