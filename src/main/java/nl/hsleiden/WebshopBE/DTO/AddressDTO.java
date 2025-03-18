package nl.hsleiden.WebshopBE.DTO;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AddressDTO {

    @NotNull(message = "Straat is verplicht")
    private String street;

    @NotNull(message = "Huisnummer is verplicht")
    private String houseNumber;

    private String houseNumberAddition;

    @NotNull(message = "Postcode is verplicht")
    private String postalCode;

    @NotNull(message = "Stad is verplicht")
    private String city;

    @NotNull(message = "Land is verplicht")
    private String country;
} 