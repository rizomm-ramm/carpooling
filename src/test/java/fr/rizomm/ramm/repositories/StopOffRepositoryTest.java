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
import fr.rizomm.ramm.model.StopOff;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
	DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class})
public class StopOffRepositoryTest {

	 @Autowired
	 private StopOffRepository stopOffRepository;

	 @Test
	 public void testFindActivatedStopOff() {
       List<StopOff> stopOffEntries = stopOffRepository.findByStatus(StopOff.Status.ACTIVATED);
       assertThat(stopOffEntries.size(), is(3));
     }

	 @Test
	 public void testFindUnusedStatusStopOff(){
		 List<StopOff> stopOffEntries = stopOffRepository.findByStatus(StopOff.Status.DONE);
		 assertThat(stopOffEntries.size(), is(0));
	 }
}