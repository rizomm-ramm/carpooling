package fr.rizomm.ramm.service.impl;

import fr.rizomm.ramm.model.User;
import fr.rizomm.ramm.repositories.UserRepository;
import fr.rizomm.ramm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component("userService")
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User getOne(String username) {
        return userRepository.getOne(username);
    }

}
