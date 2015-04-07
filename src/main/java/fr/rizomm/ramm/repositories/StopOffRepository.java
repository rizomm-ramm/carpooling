package fr.rizomm.ramm.repositories;

import fr.rizomm.ramm.model.StopOff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Maximilien on 20/03/2015.
 */
public interface StopOffRepository extends JpaRepository<StopOff, Long> {
    List<StopOff> findByStatus(StopOff.Status status);
}
