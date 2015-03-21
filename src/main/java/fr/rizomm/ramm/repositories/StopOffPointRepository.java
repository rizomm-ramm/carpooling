package fr.rizomm.ramm.repositories;

import fr.rizomm.ramm.model.StopOffPoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Maximilien on 20/03/2015.
 */
public interface StopOffPointRepository extends JpaRepository<StopOffPoint, Long> {
    List<StopOffPoint> findByType(StopOffPoint.Type type);
}
