package fr.rizomm.ramm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
//@Entity(name = "stopoff_reservation")
public class StopOffReservation {
    private StopOff stopOff;

    private User user;

    private boolean payed;
}
