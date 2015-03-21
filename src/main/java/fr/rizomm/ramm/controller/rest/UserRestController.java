package fr.rizomm.ramm.controller.rest;

import fr.rizomm.ramm.model.User;
import fr.rizomm.ramm.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserRestController {
    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/")
    public List<User> findAll() {
        log.debug("Find all users");
        return userService.findAll();
    }
}
