package fr.rizomm.ramm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Builder;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class StopOffDistance implements Comparable<StopOffDistance> {

    private double departureDistance;

    private double arrivalDistance;

    public Double getTotalDistance() {
        return departureDistance + arrivalDistance;
    }

    @Override
    public int compareTo(StopOffDistance o) {
        return this.getTotalDistance().compareTo(o.getTotalDistance());
    }
}
