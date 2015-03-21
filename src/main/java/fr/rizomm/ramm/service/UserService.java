package fr.rizomm.ramm.service;


import fr.rizomm.ramm.model.User;

import java.util.List;

/**
 * Created by Maximilien on 19/03/2015.
 */
public interface UserService {
    List<User> findAll();

    User getOne(String username);
}
