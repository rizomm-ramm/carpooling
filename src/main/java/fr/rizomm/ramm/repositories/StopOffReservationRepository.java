package fr.rizomm.ramm.repositories;

import fr.rizomm.ramm.model.StopOff;
import fr.rizomm.ramm.model.StopOffReservation;
import fr.rizomm.ramm.model.StopOffReservationId;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Maximilien on 20/03/2015.
 */
public interface StopOffReservationRepository extends JpaRepository<StopOffReservation, StopOffReservationId> {

}
