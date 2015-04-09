package fr.rizomm.ramm.service.impl;

import com.google.common.collect.ImmutableList;
import fr.rizomm.ramm.model.Role;
import fr.rizomm.ramm.model.User;
import fr.rizomm.ramm.model.UserRole;
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

    @Override
    public User saveAndFlush(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Override
    public User register(String username, String password) {
        if(username.length() < 5) {
            throw new IllegalArgumentException("Votre pseudo doit au moins contenir 5 caractères");
        }

        if(password.length() < 5) {
            throw new IllegalArgumentException("Votre mot de passe doit au moins contenir 5 caractères");
        }

        User testExisting = userRepository.findOne(username);
        if(testExisting != null) {
            throw new IllegalArgumentException("Un utilisateur avec le pseudo " + username + " existe déja");
        }

        User user = User.builder()
                .username(username)
                .password(password)
                .enabled(true)
                .build();

        user.setRoles(ImmutableList.of(UserRole.builder().user(user).role(Role.ROLE_USER).build()));


        return saveAndFlush(user);
    }

}
