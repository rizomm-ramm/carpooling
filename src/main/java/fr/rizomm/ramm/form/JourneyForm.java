package fr.rizomm.ramm.form;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class JourneyForm {

    @Data
    public class Address {
        @NotNull
        private Double longitude;

        @NotNull
        private Double latitude;

        @NotNull
        private String address;
    }

    @Valid
    private Address departure = new Address();

    @Valid
    private Address arrival = new Address();
}
