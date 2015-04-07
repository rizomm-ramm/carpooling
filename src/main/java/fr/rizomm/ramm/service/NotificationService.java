package fr.rizomm.ramm.service;


import fr.rizomm.ramm.model.Notification;

import java.util.List;

/**
 * Created by Maximilien on 19/03/2015.
 */
public interface NotificationService {
    List<Notification> findAllByUser(String username);

    Notification saveAndFlush(Notification notification);

    Notification saveAndFlush(String message, Notification.Type type, String username, String link);

    Notification getOne(Long id);

    List<Notification> getLastNotifications(String username);

    List<Notification> getUnreadNotifications(String username);

    void setAllReadNotifications(String username);
}
