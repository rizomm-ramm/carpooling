package fr.rizomm.ramm.form;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class SimpleJourneyForm {

    @Data
    public class Address {
        @NotNull
        private Double longitude; //NOSONAR

        @NotNull
        private Double latitude; //NOSONAR

        @Min(0)
        private int precision = 20; //NOSONAR

        @NotNull
        @NotEmpty
        private String address; //NOSONAR
    }

    @Valid
    private Address departure = new Address(); //NOSONAR

    @Valid
    private Address arrival = new Address(); //NOSONAR

    @NotNull
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    private Date date; //NOSONAR

}
