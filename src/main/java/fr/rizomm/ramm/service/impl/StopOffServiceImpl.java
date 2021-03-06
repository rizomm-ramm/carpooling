package fr.rizomm.ramm.service.impl;

import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixElementStatus;
import com.google.maps.model.DistanceMatrixRow;
import fr.rizomm.ramm.form.BookSeatForm;
import fr.rizomm.ramm.form.SimpleJourneyForm;
import fr.rizomm.ramm.model.Journey;
import fr.rizomm.ramm.model.Notification;
import fr.rizomm.ramm.model.StopOff;
import fr.rizomm.ramm.model.StopOffDistance;
import fr.rizomm.ramm.model.StopOffPoint;
import fr.rizomm.ramm.model.StopOffReservation;
import fr.rizomm.ramm.model.StopOffReservationId;
import fr.rizomm.ramm.model.User;
import fr.rizomm.ramm.repositories.StopOffRepository;
import fr.rizomm.ramm.service.GeoService;
import fr.rizomm.ramm.service.NotificationService;
import fr.rizomm.ramm.service.StopOffReservationService;
import fr.rizomm.ramm.service.StopOffService;
import fr.rizomm.ramm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.google.common.base.Preconditions.checkNotNull;

@Component("stopOffService")
@Transactional
public class StopOffServiceImpl implements StopOffService {
    private final StopOffRepository stopOffRepository;
    private final GeoService geoService;
    private final UserService userService;
    private final StopOffReservationService stopOffReservationService;
    private final NotificationService notificationService;

    @Autowired
    public StopOffServiceImpl(StopOffRepository stopOffRepository,
                              GeoService geoService,
                              UserService userService,
                              StopOffReservationService stopOffReservationService,
                              NotificationService notificationService) {
        this.stopOffRepository = stopOffRepository;
        this.geoService = geoService;
        this.userService = userService;
        this.stopOffReservationService = stopOffReservationService;
        this.notificationService = notificationService;
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
        Long duration = null;

        // Search for the minimum distance
        for (DistanceMatrixRow row : distance.rows) {
            for (DistanceMatrixElement element : row.elements) {
                if(element.status.equals(DistanceMatrixElementStatus.OK)
                    && (minDistance == null || minDistance > element.distance.inMeters)) {
                    minDistance = element.distance.inMeters;
                    duration = element.duration.inSeconds;
                }
            }
        }

        checkNotNull(minDistance, "Distance has not been found");
        checkNotNull(duration, "Duration has not been found");

        arrival.setDate(new Date(departure.getDate().getTime() + (duration * 1000)));

        StopOff stopOff = StopOff.builder()
                .departurePoint(departure)
                .arrivalPoint(arrival)
                .journey(journey)
                .distance(minDistance)
                .availableSeats(0)
                .price(0D)
                .status(StopOff.Status.INITIALIZED)
                .build();

        return stopOffRepository.saveAndFlush(stopOff);
    }

    @Override
    public StopOff updateStopOff(StopOff stopOffToUpdate) throws Exception {
        StopOff original = stopOffRepository.getOne(stopOffToUpdate.getId());

        if(stopOffToUpdate.getDeparturePoint().getDate().compareTo(stopOffToUpdate.getArrivalPoint().getDate()) > 0) {
            throw new IllegalArgumentException("Arrivée prévue avant le départ!");
        }

        original.getDeparturePoint().setDate(stopOffToUpdate.getDeparturePoint().getDate());
        original.getArrivalPoint().setDate(stopOffToUpdate.getArrivalPoint().getDate());

        if(!original.getDeparturePoint().getAddress().equals(stopOffToUpdate.getDeparturePoint().getAddress())
                || !original.getArrivalPoint().getAddress().equals(stopOffToUpdate.getArrivalPoint().getAddress())) {
            DistanceMatrix distance = geoService.distance(stopOffToUpdate.getDeparturePoint().getAddress(),
                    stopOffToUpdate.getArrivalPoint().getAddress());

            Long minDistance = null;

            // Search for the minimum distance
            for (DistanceMatrixRow row : distance.rows) {
                for (DistanceMatrixElement element : row.elements) {
                    if(element.status.equals(DistanceMatrixElementStatus.OK)
                        && (minDistance == null || minDistance > element.distance.inMeters)) {
                        minDistance = element.distance.inMeters;
                    }
                }
            }

            checkNotNull(minDistance, "Distance has not been found");

            original.setDistance(minDistance);

            original.getDeparturePoint().setAddress(stopOffToUpdate.getDeparturePoint().getAddress());
            original.getArrivalPoint().setAddress(stopOffToUpdate.getArrivalPoint().getAddress());
        }

        original.setPrice(stopOffToUpdate.getPrice());
        original.setAvailableSeats(stopOffToUpdate.getAvailableSeats());
        original.getDeparturePoint().setDescription(stopOffToUpdate.getDeparturePoint().getDescription());
        original.getArrivalPoint().setDescription(stopOffToUpdate.getArrivalPoint().getDescription());
        original.setStatus(stopOffToUpdate.getStatus());

        return stopOffRepository.saveAndFlush(original);
    }

    @Override
    public StopOff getOne(Long id) {
        return stopOffRepository.getOne(id);
    }

    @Override
    public Map<Double, StopOff> findStopOffByLocation(double lat, double lng, double distanceMax) {
        List<StopOff> stopOffs = stopOffRepository.findByStatus(StopOff.Status.ACTIVATED);

        Map<Double, StopOff> matchingStopOffWithDistance = new TreeMap<>();

        stopOffs.forEach(stopOff -> {
                Double distanceToDeparture = geoService.calculateDistance(stopOff.getDeparturePoint().getLatitude(),
                        stopOff.getDeparturePoint().getLongitude(),
                        lat, lng);
                if (distanceToDeparture <= distanceMax) {
                    matchingStopOffWithDistance.put(distanceToDeparture, stopOff);
                }
            });

        return matchingStopOffWithDistance;
    }

    @Override
    public Map<StopOffDistance, StopOff> findStopOffByLocation(SimpleJourneyForm journeyForm) {
        List<StopOff> stopOffs = stopOffRepository.findByStatus(StopOff.Status.ACTIVATED);

        Map<StopOffDistance, StopOff> matchingStopOffWithDistance = new TreeMap<>();

        stopOffs.forEach(stopOff -> {
                if(stopOff.getDeparturePoint().getDate().compareTo(journeyForm.getDate()) != -1) {

                    Double distanceToDeparture = geoService.calculateDistance(stopOff.getDeparturePoint().getLatitude(),
                            stopOff.getDeparturePoint().getLongitude(),
                            journeyForm.getDeparture().getLatitude(),
                            journeyForm.getDeparture().getLongitude());

                    Double distanceToArrival = geoService.calculateDistance(stopOff.getArrivalPoint().getLatitude(),
                            stopOff.getArrivalPoint().getLongitude(),
                            journeyForm.getArrival().getLatitude(),
                            journeyForm.getArrival().getLongitude());

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
    public StopOffReservation book(BookSeatForm bookSeatForm, String username) {
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

        String notificationMessage = String.format("%s a effectué une demande de participation au trajet <br /> %s - %s ",
                username, stopOff.getDeparturePoint().getAddress(), stopOff.getArrivalPoint().getAddress());

        String link = String.format("/profile/journeys?type=driver&driverStoffOffid=%d", + stopOff.getJourney().getId());

        notificationService.saveAndFlush(notificationMessage, Notification.Type.INFO, stopOff.getJourney().getUser().getUsername(), link);

        return stopOffReservation;
    }

    @Override
    public StopOffReservation changeReservationStatus(Long stopOffId,
                                                      String passengerId,
                                                      String loggedUser,
                                                      StopOffReservation.Status status) {
        StopOff stopOff = getOne(stopOffId);

        if(!stopOff.getJourney().getUser().getUsername().equals(loggedUser)) {
            throw new IllegalStateException("Vous n'avez pas créé ce trajet.");
        }

        User passenger = userService.getOne(passengerId);

        StopOffReservation reservation = stopOffReservationService.getOne(new StopOffReservationId(stopOff, passenger));

        if(!StopOffReservation.Status.WAITING.equals(reservation.getStatus())) {
            throw new IllegalStateException("Le statut de la réservation n'est plus en attente et ne peut plus �tre modifi�");
        }

        reservation.setStatus(status);

        String notificationMessage = loggedUser + " a %s votre demande de participation au trajet <br /> "
                + stopOff.getDeparturePoint().getAddress() + " - " + stopOff.getArrivalPoint().getAddress();

        String link = String.format("/profile/journeys?type=passenger&passengerStoffOffid=%d", + stopOff.getJourney().getId());

        if(status.equals(StopOffReservation.Status.VALIDATED)) {
            notificationService.saveAndFlush(String.format(notificationMessage, "validé"), Notification.Type.SUCCESS, passengerId, link);
        } else {
            notificationService.saveAndFlush(String.format(notificationMessage, "refusé"), Notification.Type.DANGER, passengerId, link);
        }


        return stopOffReservationService.saveAndFlush(reservation);
    }

    public StopOffReservation changePaiementStatus(Long stopOffId,
										           String passengerId,
										           String loggedUser,
										           StopOffReservation.Status status){
    	StopOff stopOff = getOne(stopOffId);

        if(!stopOff.getJourney().getUser().getUsername().equals(loggedUser)) {
            throw new IllegalStateException("Vous n'avez pas créé ce trajet.");
        }

        User passenger = userService.getOne(passengerId);

        StopOffReservation reservation = stopOffReservationService.getOne(new StopOffReservationId(stopOff, passenger));

        if(StopOffReservation.Status.VALIDATED.equals(status)) {
            reservation.setPayed(true);

            String notificationMessage = loggedUser + " a %s votre paiement pour le trajet <br /> "
                    + stopOff.getDeparturePoint().getAddress() + " - " + stopOff.getArrivalPoint().getAddress();

            String link = String.format("/profile/journeys?type=passenger&passengerStoffOffid=%d", + stopOff.getJourney().getId());

            notificationService.saveAndFlush(String.format(notificationMessage, "validé"), Notification.Type.SUCCESS, passengerId, link);
        }

        return stopOffReservationService.saveAndFlush(reservation);
    }
}
