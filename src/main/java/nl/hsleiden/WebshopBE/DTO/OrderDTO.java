package nl.hsleiden.WebshopBE.DTO;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class OrderDTO {

    @NotNull(message = "Verzendadres is verplicht")
    @Valid
    private AddressDTO shippingAddress;

    @Valid
    private AddressDTO billingAddress; // If null, use shipping address
} 