package fr.rizomm.ramm.repositories;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import fr.rizomm.ramm.Application;
import fr.rizomm.ramm.model.StopOffPoint;
import fr.rizomm.ramm.model.StopOffPoint.Type;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
	DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class})
public class StopOffPointRepositoryTest {

	 @Autowired
	 private StopOffPointRepository stopOffPointRepository;

	 @Test
	 public void testFindByTypeDeparture() {
       List<StopOffPoint> stopOffPointEntries = stopOffPointRepository.findByType(Type.DEPARTURE);
       assertThat(stopOffPointEntries.size(), is(3));
     }
}
