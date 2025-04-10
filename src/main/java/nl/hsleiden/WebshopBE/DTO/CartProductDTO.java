package nl.hsleiden.WebshopBE.DTO;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CartProductDTO {

    @NotNull(message = "CartId is verplicht")
    private String cartId;

    @NotNull(message = "ProductId is verplicht")
    private String productId;

    @Range(min = 1, message = "Hoeveelheid moet minimaal 1 zijn")
    private int quantity;
}
