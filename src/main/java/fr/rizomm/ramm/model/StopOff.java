package fr.rizomm.ramm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Builder;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.Min;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"reservations"})
@Builder
@EqualsAndHashCode(exclude = {"reservations", "distance"})
@Entity(name = "stopoff")
public class StopOff {

    public enum Status {
        INITIALIZED,
        ACTIVATED,
        CANCELLED,
        DONE
    }

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "journey_id")
    @JsonIgnore
    private Journey journey;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "departure_point_id")
    private StopOffPoint departurePoint;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "arrival_point_id")
    private StopOffPoint arrivalPoint;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.stopOff", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<StopOffReservation> reservations;

    @Column(name = "distance", nullable = false)
    @Min(0)
    private Long distance;

    @Column(name = "available_seats", nullable = true)
    @Min(0)
    private Integer availableSeats;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.INITIALIZED;

    @Column(name = "price", nullable = true)
    @Min(0)
    private Double price;

    @PreUpdate
    public void onUpdate() {
        departurePoint.setType(StopOffPoint.Type.DEPARTURE);
        arrivalPoint.setType(StopOffPoint.Type.ARRIVAL);
    }

    @PrePersist
    public void onInsert() {
        departurePoint.setType(StopOffPoint.Type.DEPARTURE);
        arrivalPoint.setType(StopOffPoint.Type.ARRIVAL);
    }

    public long numberOfValidatedReservation() {
        AtomicInteger validatedReservations = new AtomicInteger(0);
        reservations.stream()
                .filter(r -> StopOffReservation.Status.VALIDATED.equals(r.getStatus()))
                .forEach(r -> validatedReservations.set(validatedReservations.get() + r.getSeats()));

        return validatedReservations.get();
    }

    public long numberOfRemainingReservation() {
        return availableSeats - numberOfValidatedReservation();
    }

    public boolean isAlreadyRegistered(String username) {
        return reservations.stream()
                .filter(r -> r.getUser().getUsername().equals(username))
                .count() > 0;
    }
}
