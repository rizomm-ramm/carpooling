package fr.rizomm.ramm.repositories;

import fr.rizomm.ramm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Maximilien on 19/03/2015.
 */
@org.springframework.stereotype.Repository
public interface UserRepository extends JpaRepository<User, String> {

}
