package fr.rizomm.ramm.service.impl;

import fr.rizomm.ramm.model.Notification;
import fr.rizomm.ramm.repositories.NotificationRepository;
import fr.rizomm.ramm.service.NotificationService;
import fr.rizomm.ramm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Component("notificationService")
@Transactional
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserService userService;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   UserService userService) {
        this.notificationRepository = notificationRepository;
        this.userService = userService;
    }

    @Override
    public List<Notification> findAllByUser(String username) {
        return notificationRepository.findAllByUser(userService.getOne(username));
    }

    @Override
    public Notification saveAndFlush(Notification notification) {
        return notificationRepository.saveAndFlush(notification);
    }

    @Override
    public Notification saveAndFlush(String message, Notification.Type type, String username, String link) {
        return saveAndFlush(Notification.builder()
                .date(new Date())
                .status(Notification.Status.UNREAD)
                .type(type)
                .message(message)
                .link(link)
                .user(userService.getOne(username))
                .build());
    }

    @Override
    public List<Notification> getLastNotifications(String username) {
        return notificationRepository.findTop10AllByUserOrderByDateDesc(userService.getOne(username));
    }

    @Override
    public List<Notification> getUnreadNotifications(String username) {
        return notificationRepository.findAllByUserAndStatus(userService.getOne(username), Notification.Status.UNREAD);
    }

    @Override
    public void setAllReadNotifications(String username) {

        getUnreadNotifications(username).forEach(notification -> {
            notification.setStatus(Notification.Status.READ);
            notificationRepository.saveAndFlush(notification);
        });
    }

    @Override
    public Notification getOne(Long id) {
        return notificationRepository.getOne(id);
    }

}
