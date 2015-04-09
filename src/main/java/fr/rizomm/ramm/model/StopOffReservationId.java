package fr.rizomm.ramm.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class StopOffReservationId implements Serializable {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "STOPOFF_ID")
    private StopOff stopOff; //NOSONAR

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "USERNAME")
    private User user; //NOSONAR
}
