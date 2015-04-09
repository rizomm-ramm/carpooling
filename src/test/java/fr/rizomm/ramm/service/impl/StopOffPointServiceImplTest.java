package fr.rizomm.ramm.service.impl;

import com.google.common.collect.ImmutableList;
import fr.rizomm.ramm.model.StopOffPoint;
import fr.rizomm.ramm.repositories.StopOffPointRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.*;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Maximilien on 09/04/2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class StopOffPointServiceImplTest {

    @Mock
    private StopOffPointRepository stopOffPointRepository;

    private StopOffPointServiceImpl stopOffPointService;

    private StopOffPoint lille = StopOffPoint.builder()
            .address("41 rue du port")
            .type(StopOffPoint.Type.ARRIVAL)
            .build();

    private StopOffPoint paris = StopOffPoint.builder()
            .address("Paris")
            .type(StopOffPoint.Type.DEPARTURE)
            .build();

    @Before
    public void setUp() throws Exception {
        stopOffPointService = new StopOffPointServiceImpl(stopOffPointRepository);
    }

    @After
    public void tearDown() throws Exception {
        reset(stopOffPointRepository);
    }

    @Test
    public void testFindAll() throws Exception {
        // Quand on appelle la méthode findAll du repository, on retourne [lille, paris]
        when(stopOffPointRepository.findAll()).thenReturn(ImmutableList.of(lille, paris));

        List<StopOffPoint> stopOffPoints = stopOffPointService.findAll();

        // Vérification du retour du service
        assertThat(stopOffPoints, hasItems(lille, paris));

        // Vérification que le repository a bien été appelé
        verify(stopOffPointRepository, times(1)).findAll();
    }

    @Test
    public void testFindByType() throws Exception {
        // Quand on appelle la méthode findByType du repository, on retourne [paris]
        when(stopOffPointRepository.findByType(StopOffPoint.Type.DEPARTURE)).thenReturn(ImmutableList.of(paris));

        List<StopOffPoint> stopOffPoints = stopOffPointService.findByType(StopOffPoint.Type.DEPARTURE);

        // Vérification du retour du service
        assertThat(stopOffPoints, hasItems(paris));

        // Vérification que le repository a bien été appelé
        verify(stopOffPointRepository, times(1)).findByType(StopOffPoint.Type.DEPARTURE);
    }


}