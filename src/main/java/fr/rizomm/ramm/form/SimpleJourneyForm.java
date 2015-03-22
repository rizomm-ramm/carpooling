package fr.rizomm.ramm.form;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class SimpleJourneyForm {

    @Data
    public class Address {
        @NotNull
        private Double longitude;

        @NotNull
        private Double latitude;

        @NotNull
        @NotEmpty
        private String address;
    }

    @Valid
    private Address departure = new Address();

    @Valid
    private Address arrival = new Address();
}
