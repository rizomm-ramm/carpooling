package fr.rizomm.ramm.form;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class BookSeatForm {

    @Min(0)
    private int seats;

    private long stopOffId;

    private String comment;
}
