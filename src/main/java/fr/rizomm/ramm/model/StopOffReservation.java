package fr.rizomm.ramm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Builder;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import java.beans.Transient;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
@Entity
@Table(name = "STOPOFF_RESERVATIONS")
@AssociationOverrides({
        @AssociationOverride(name = "pk.user",
                joinColumns = @JoinColumn(name = "USERNAME")),
        @AssociationOverride(name = "pk.stopOff",
                joinColumns = @JoinColumn(name = "STOPOFF_ID"))})
public class StopOffReservation {

    public enum Status {
        WAITING,
        VALIDATED,
        REFUSED
    }

    @EmbeddedId
    @JsonIgnore
    private StopOffReservationId pk;

    @Column(name = "seats", nullable = false)
    private int seats = 1;

    @Column(name = "payed", nullable = true)
    private boolean payed = false;

    @Column(name = "comment", nullable = true)
    private String comment = "";

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.WAITING;

    @Transient
    public StopOff getStopOff() {
        if (pk != null) {
            return pk.getStopOff();
        } else {
            return null;
        }
    }

    public void setStopOff(StopOff stopOff) {
        if (pk != null) {
            pk.setStopOff(stopOff);
        }
    }

    @Transient
    public User getUser() {
        if (pk != null) {
            return pk.getUser();
        } else {
            return null;
        }
    }

    public void setUser(User user) {
        if (pk != null) {
            pk.setUser(user);
        }
    }
}
