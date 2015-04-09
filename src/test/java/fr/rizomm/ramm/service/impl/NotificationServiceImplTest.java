package fr.rizomm.ramm.service.impl;

import com.google.common.collect.ImmutableList;
import fr.rizomm.ramm.model.Notification;
import fr.rizomm.ramm.model.User;
import fr.rizomm.ramm.repositories.NotificationRepository;
import fr.rizomm.ramm.service.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserService userService;

    private NotificationServiceImpl notificationService;


    private User user = User.builder()
            .username("jeffrey")
            .build();

    private Notification notification1 = Notification.builder()
            .id(42L)
            .message("Nouveau trajet enregistré")
            .status(Notification.Status.UNREAD)
            .type(Notification.Type.INFO)
            .link("path/to/notification")
            .user(user)
            .build();

    private Notification notification2 = Notification.builder()
            .id(59L)
            .message("Demande d'enregistrement d'un utilisateur sur votre trajet")
            .status(Notification.Status.READ)
            .type(Notification.Type.INFO)
            .link("path/to/notification")
            .user(user)
            .user(user)
            .build();

    @Before
    public void setUp() throws Exception {
        notificationService = new NotificationServiceImpl(notificationRepository, userService);
    }

    @After
    public void tearDown() throws Exception {
        reset(notificationRepository, userService);
    }

    @Test
    public void testFindAllByUser() throws Exception {
        when(notificationRepository.findAllByUser(user)).thenReturn(ImmutableList.of(notification1,notification2));
        when(userService.getOne(user.getUsername())).thenReturn(user);

        List<Notification> notifications = notificationService.findAllByUser(user.getUsername());

        assertThat(notifications, hasItems(notification1, notification2));

        verify(notificationRepository, times(1)).findAllByUser(user);
        verify(userService, times(1)).getOne(user.getUsername());
    }

    @Test
    public void testSaveAndFlush() throws Exception {
        when(notificationRepository.saveAndFlush(notification1)).thenReturn(notification1);

        Notification notification = notificationService.saveAndFlush(notification1);

        assertEquals(notification, notification1);
        verify(notificationRepository, times(1)).saveAndFlush(notification1);
    }

    @Test
    public void testSaveAndFlush1() throws Exception {
        ArgumentCaptor<Notification> notificationArgumentCaptor = ArgumentCaptor.forClass(Notification.class);
        when(notificationRepository.saveAndFlush(notification1)).thenReturn(notification1);
        when(userService.getOne(user.getUsername())).thenReturn(user);

        Notification notification = notificationService.saveAndFlush(
                "Nouveau trajet enregistré", Notification.Type.INFO, user.getUsername(), "path/to/notification");

        verify(notificationRepository, times(1)).saveAndFlush(notificationArgumentCaptor.capture());

        Notification createdNotification = notificationArgumentCaptor.getValue();

        assertThat(createdNotification, allOf(
                        hasProperty("date", is(notNullValue())),
                        hasProperty("status", is(Notification.Status.UNREAD)),
                        hasProperty("type", is(Notification.Type.INFO)),
                        hasProperty("message", is("Nouveau trajet enregistré")),
                        hasProperty("link", is("path/to/notification")),
                        hasProperty("user", is(user))
                )
        );

        verify(userService, times(1)).getOne(user.getUsername());
    }

    @Test
    public void testGetLastNotifications() throws Exception {
        when(notificationRepository.findTop10AllByUserOrderByDateDesc(user)).thenReturn(ImmutableList.of(notification1,notification2));
        when(userService.getOne(user.getUsername())).thenReturn(user);

        List<Notification> lastNotifications = notificationService.getLastNotifications(user.getUsername());

        assertThat(lastNotifications, hasItems(notification1, notification2));

        verify(notificationRepository, times(1)).findTop10AllByUserOrderByDateDesc(user);
        verify(userService, times(1)).getOne(user.getUsername());
    }

    @Test
    public void testGetUnreadNotifications() throws Exception {
        when(notificationRepository.findAllByUserAndStatus(user, Notification.Status.UNREAD)).thenReturn(ImmutableList.of(notification1));
        when(userService.getOne(user.getUsername())).thenReturn(user);

        List<Notification> unreadNotifications = notificationService.getUnreadNotifications(user.getUsername());

        assertThat(unreadNotifications, hasItems(notification1));

        verify(notificationRepository, times(1)).findAllByUserAndStatus(user, Notification.Status.UNREAD);
        verify(userService, times(1)).getOne(user.getUsername());
    }

    @Test
    public void testSetAllReadNotifications() throws Exception {

    }

    @Test
    public void testGetOne() throws Exception {
        when(notificationRepository.getOne(notification1.getId())).thenReturn(notification1);

        Notification notification = notificationService.getOne(42L);

        assertEquals(notification, notification1);

        verify(notificationRepository, times(1)).getOne(notification1.getId());
    }
}