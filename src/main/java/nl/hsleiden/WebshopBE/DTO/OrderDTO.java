package nl.hsleiden.WebshopBE.DTO;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class OrderDTO {
    @NotBlank(message = "Betaalmethode is verplicht")
    private String paymentMethod;
} 