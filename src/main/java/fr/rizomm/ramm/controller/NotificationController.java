package fr.rizomm.ramm.controller;

import fr.rizomm.ramm.service.NotificationService;
import fr.rizomm.ramm.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

/**
 * Created by Maximilien on 06/04/2015.
 */
@Controller
@RequestMapping("notification")
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RequestMapping(value = "/unread/count", method = RequestMethod.GET)
    @ResponseBody
    public Integer unreadNotifications(Principal principal) {
        return notificationService.getUnreadNotifications(principal.getName()).size();
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getLastNotifications(Principal principal) {
        ModelAndView mNv = new ModelAndView("notification");

        mNv.addObject("notifications", notificationService.getLastNotifications(principal.getName()));

        return mNv;
    }
}
