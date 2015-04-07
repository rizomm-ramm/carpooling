package fr.rizomm.ramm.repositories;

import fr.rizomm.ramm.model.Journey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Maximilien on 19/03/2015.
 */
@org.springframework.stereotype.Repository
public interface JourneyRepository extends JpaRepository<Journey, Long> {
}
