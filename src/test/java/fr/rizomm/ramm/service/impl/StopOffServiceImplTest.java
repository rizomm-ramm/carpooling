package fr.rizomm.ramm.service.impl;

import com.google.common.collect.ImmutableList;
import com.google.maps.model.Distance;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixElementStatus;
import com.google.maps.model.DistanceMatrixRow;
import com.google.maps.model.Duration;

import fr.rizomm.ramm.model.Journey;
import fr.rizomm.ramm.model.StopOff;
import fr.rizomm.ramm.model.StopOff.Status;
import fr.rizomm.ramm.model.StopOffPoint;
import fr.rizomm.ramm.repositories.StopOffRepository;
import fr.rizomm.ramm.service.GeoService;
import fr.rizomm.ramm.service.NotificationService;
import fr.rizomm.ramm.service.StopOffReservationService;
import fr.rizomm.ramm.service.UserService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Maximilien on 09/04/2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class StopOffServiceImplTest {

    @Mock
    private StopOffRepository stopOffRepository;
    @Mock
    private GeoService geoService;
    @Mock
    private UserService userService;
    @Mock
    private StopOffReservationService stopOffReservationService;
    @Mock
    private NotificationService notificationService;

    private StopOffServiceImpl stopOffService;
    
    private StopOff stopOff1 = StopOff.builder()
    		.distance(Long.valueOf("6520"))
    		.availableSeats(4)
    		.price(3.0)
    		.status(Status.ACTIVATED)
    		.build();

    private StopOff stopOff2 = StopOff.builder()
    		.distance(Long.valueOf("9222"))
    		.availableSeats(2)
    		.price(5.0)
    		.status(Status.ACTIVATED)
    		.build();

    private StopOff stopOff3 = StopOff.builder()
    		.distance(Long.valueOf("1270879"))
    		.availableSeats(2)
    		.price(100.0)
    		.status(Status.ACTIVATED)
    		.build();


    @Before
    public void setUp() throws Exception {
        stopOffService = new StopOffServiceImpl(stopOffRepository, geoService, userService, stopOffReservationService, notificationService);
    }

    @After
    public void tearDown() throws Exception {
        reset(stopOffRepository, geoService, userService, stopOffReservationService, notificationService);
    }

    @Test
    public void testFindAll() throws Exception {
        // Quand on appelle la méthode findAll du repository, on retourne [stopOff1, stopOff2, stopOff3]
        when(stopOffRepository.findAll()).thenReturn(ImmutableList.of(stopOff1, stopOff2, stopOff3 ));

        List<StopOff> stopOffList = stopOffService.findAll();

        // Vérification du retour du service
        assertThat(stopOffList, hasItems(stopOff1, stopOff2, stopOff3));

        // Vérification que le repository a bien été appelé
        verify(stopOffRepository, times(1)).findAll();
    }

    @Test
    public void testSaveAndFlush() throws Exception {
        // When saveAndFlush is called we return the spotOff1
        when(stopOffService.saveAndFlush(stopOff1)).thenReturn(stopOff1);

        StopOff stopOff4 = stopOffService.saveAndFlush(stopOff1);
        
        // Vérification que le repository a bien été appelé
        verify(stopOffRepository, times(1)).saveAndFlush(stopOff1);

        assertThat(stopOff4, allOf(
                        hasProperty("distance", is((Long.valueOf("6520")))),
                        hasProperty("availableSeats", is(4)),
                        hasProperty("price", is(3.0)),
                        hasProperty("status", is(StopOff.Status.ACTIVATED))
                )
        );
    }

    @Test
    public void testCreateStopOff_StatusOk() throws Exception {
        ArgumentCaptor<StopOff> stopOffArgumentCaptor = ArgumentCaptor.forClass(StopOff.class);

        Journey journey = Journey.builder().build();

        Date dateDeparture = new Date();
        StopOffPoint departure = StopOffPoint.builder().address("Lille").date(dateDeparture).build();
        StopOffPoint arrival = StopOffPoint.builder().address("Paris").build();

        // Set the matrix element
        DistanceMatrixElement distanceMatrixElement = new DistanceMatrixElement();
        // Duration
        Duration duration = new Duration();
        duration.inSeconds = 3600;
        distanceMatrixElement.duration = duration;
        // Distance
        Distance distance = new Distance();
        distance.inMeters = 10000;
        distanceMatrixElement.distance = distance;
        distanceMatrixElement.status = DistanceMatrixElementStatus.OK;

        DistanceMatrixRow distanceMatrixRow = new DistanceMatrixRow();
        distanceMatrixRow.elements = new DistanceMatrixElement[] {distanceMatrixElement};

        DistanceMatrix distanceMatrix = new DistanceMatrix(new String[]{departure.getAddress()},
                new String[]{arrival.getAddress()},
                new DistanceMatrixRow[]{distanceMatrixRow});

        // When geoService is called we return our distance matrix
        when(geoService.distance(departure.getAddress(), arrival.getAddress())).thenReturn(distanceMatrix);

        stopOffService.createStopOff(journey, departure, arrival);

        // Capture the created stopOff
        verify(stopOffRepository, times(1)).saveAndFlush(stopOffArgumentCaptor.capture());

        StopOff createdStopOff = stopOffArgumentCaptor.getValue();

        assertThat(createdStopOff, allOf(
                        hasProperty("distance", is(distance.inMeters)),
                        hasProperty("availableSeats", is(0)),
                        hasProperty("price", is(0D)),
                        hasProperty("status", is(StopOff.Status.INITIALIZED)),
                        hasProperty("departurePoint", is(departure)),
                        hasProperty("arrivalPoint", is(arrival)),
                        hasProperty("journey", is(journey)),
                        // Arrival date is updated
                        hasProperty("arrivalPoint", hasProperty("date", is(new Date(departure.getDate().getTime() + (duration.inSeconds * 1000)))))
                )
        );


    }
}