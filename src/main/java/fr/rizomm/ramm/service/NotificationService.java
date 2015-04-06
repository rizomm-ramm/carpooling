package fr.rizomm.ramm.service;


import fr.rizomm.ramm.form.BookSeatForm;
import fr.rizomm.ramm.form.SimpleJourneyForm;
import fr.rizomm.ramm.model.Journey;
import fr.rizomm.ramm.model.Notification;
import fr.rizomm.ramm.model.StopOff;
import fr.rizomm.ramm.model.StopOffDistance;
import fr.rizomm.ramm.model.StopOffPoint;
import fr.rizomm.ramm.model.StopOffReservation;

import java.util.List;
import java.util.Map;

/**
 * Created by Maximilien on 19/03/2015.
 */
public interface NotificationService {
    List<Notification> findAllByUser(String username);

    Notification saveAndFlush(Notification notification);

    Notification getOne(Long id);

    List<Notification> getLastNotifications(String username);

    List<Notification> getUnreadNotifications(String username);
}
