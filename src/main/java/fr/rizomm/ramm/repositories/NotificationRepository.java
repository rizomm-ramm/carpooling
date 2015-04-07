package fr.rizomm.ramm.repositories;

import fr.rizomm.ramm.model.Notification;
import fr.rizomm.ramm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Maximilien on 20/03/2015.
 */
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findTop10AllByUserOrderByDateDesc(User user);

    List<Notification> findAllByUser(User user);

    List<Notification> findAllByUserAndStatus(User user, Notification.Status status);
}
