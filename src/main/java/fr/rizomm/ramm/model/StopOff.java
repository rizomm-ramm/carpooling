package fr.rizomm.ramm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Builder;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity(name = "stopoff")
public class StopOff {
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

    @Column(name = "distance", nullable = false)
    private Long distance;

    @Column(name = "available_seats", nullable = true)
    private Integer availableSeats;

    @Column(name = "price", nullable = true)
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
}
