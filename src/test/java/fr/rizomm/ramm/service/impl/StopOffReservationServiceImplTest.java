package fr.rizomm.ramm.service.impl;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.ImmutableList;

import fr.rizomm.ramm.model.StopOffReservation;
import fr.rizomm.ramm.model.StopOffReservation.Status;
import fr.rizomm.ramm.model.StopOffReservationId;
import fr.rizomm.ramm.repositories.StopOffReservationRepository;
import fr.rizomm.ramm.service.StopOffReservationService;

@RunWith(MockitoJUnitRunner.class)
public class StopOffReservationServiceImplTest {

	@Mock
	private StopOffReservationRepository stopOffReservationRepository;

	private StopOffReservationService stopOffReservationService;

	private StopOffReservation resa1 = StopOffReservation.builder()
			.payed(false)
			.seats(4)
			.status(Status.WAITING)
			.build();

	private StopOffReservation resa2 = StopOffReservation.builder()
			.payed(true)
			.seats(1)
			.status(Status.VALIDATED)
			.build();

    @Before
    public void setUp() throws Exception {
        stopOffReservationService= new StopOffReservationServiceImpl(stopOffReservationRepository);
    }

    @After
    public void tearDown() throws Exception {
        reset(stopOffReservationRepository);
    }
    
    @Test
    public void testFindAll() {
        // Quand on appelle la méthode findAll du repository, on retourne [resa1, resa2]
        when(stopOffReservationRepository.findAll()).thenReturn(ImmutableList.of(resa1, resa2));

        List<StopOffReservation> reservations = stopOffReservationService.findAll();

        // Vérification du retour du service
        assertThat(reservations, hasItems(resa1, resa2));

        // Vérification que le repository a bien été appelé
        verify(stopOffReservationRepository, times(1)).findAll();
    }

    @Test
    public void testGetOne() {
    	StopOffReservationId id = new StopOffReservationId();

        // Quand on appelle la méthode getOne() du repository, on retourne resa1
        when(stopOffReservationRepository.getOne(id)).thenReturn(resa1);

        StopOffReservation foundReservation = stopOffReservationService.getOne(id);

        // Vérification du retour du service
        assertThat(foundReservation, equalTo(resa1));

        // Vérification que le repository a bien été appelé
        verify(stopOffReservationRepository, times(1)).getOne(id);
    }

    @Test
    public void testSaveAndFlush() {
        when(stopOffReservationRepository.saveAndFlush(resa1)).thenReturn(resa1);

        StopOffReservation returnedResa = stopOffReservationService.saveAndFlush(resa1);

        assertThat(returnedResa, equalTo(resa1));
        verify(stopOffReservationRepository, times(1)).saveAndFlush(resa1);
    }

}
