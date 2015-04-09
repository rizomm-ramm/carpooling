package fr.rizomm.ramm.repositories;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.junit.Before;
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
import fr.rizomm.ramm.model.Notification;
import fr.rizomm.ramm.model.Notification.Status;
import fr.rizomm.ramm.model.Notification.Type;
import fr.rizomm.ramm.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
	DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class})
public class NotificationRepositoryTest {
	
	@Autowired
	private NotificationRepository notificationRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	private User u ;
	private User u2 ;
	private Notification n1;
	private Notification n2;
	private Notification n3;
	private Notification n4;
	private boolean firstTime = true;

	@Before
	public void clearNotificationTable() throws ParseException{
		if(firstTime){
			u = User.builder().username("user1").password("pwd").enabled(true).build();
			u2 = (User.builder().username("user2").password("pwd").enabled(false).build());
			userRepository.save(u);
			userRepository.save(u2);
			
	    	n1 = new Notification(Long.valueOf(0), u, Status.UNREAD, Type.DEFAULT, "notif1", new Date(), "path/to/link");
	    	n2 = new Notification(Long.valueOf(1), u, Status.READ, Type.DANGER, "notif2", new Date(), "path/to/link");
	    	n3 = new Notification(Long.valueOf(2), u, Status.UNREAD, Type.INFO, "notif3",new Date(), "path/to/link");
	    	n4 = new Notification(Long.valueOf(3), u, Status.READ, Type.DEFAULT, "notif4", new Date(), "path/to/link");
	    	notificationRepository.saveAndFlush(n1);
	    	notificationRepository.saveAndFlush(n2);
	    	notificationRepository.saveAndFlush(n3);
	    	notificationRepository.saveAndFlush(n4);
		}else{
			firstTime = false;
		}
	}
	
    @Test
	public void testFindAllByExistingUser(){
    	List<Notification> notificationList = notificationRepository.findAllByUser(u);
    	assertThat(notificationList.size(), is(4));
    }
	
    @Test
	public void testFindTop10AllByUserOrderByDateDesc(){    
    	List<Notification> notificationList = notificationRepository.findTop10AllByUserOrderByDateDesc(u);
    	assertThat(notificationList.size(),is(4));
    }
    
    @Test
	public void testFindTop10AllByInexistingUserOrderByDateDesc(){
    	List<Notification> notificationList = notificationRepository.findTop10AllByUserOrderByDateDesc(u2);
    	assertThat(notificationList.size(),is(0));
    }

    @Test
	public void testFindAllByInexistingUser(){
    	List<Notification> notificationList = notificationRepository.findAllByUser(u2);
    	assertThat(notificationList.size(), is(0));
    }

    @Test
	public void testFindAllByExistingUserAndStatus(){
    	List<Notification> notificationList = notificationRepository.findAllByUserAndStatus(u, Status.READ);
    	assertThat(notificationList.size(), is(2));
    }

    @Test
	public void testFindAllByInexistingUserAndStatus(){
    	List<Notification> notificationList = notificationRepository.findAllByUserAndStatus(u2, Status.READ);
    	assertThat(notificationList.size(), is(0));
    }

}
