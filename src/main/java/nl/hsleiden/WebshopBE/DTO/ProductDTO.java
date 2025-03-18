package nl.hsleiden.WebshopBE.DTO;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
@Getter
@Setter
public class ProductDTO {
    @NotNull(message = "Product naam is verplicht")
    private String name;

    @NotNull(message = "Product beschrijving is verplicht")
    private String description;

    @NotNull(message = "Product afbeelding URL is verplicht")
    private String imageUrl;

    @NotNull(message = "Product prijs is verplicht")
    private Double price;

    private String[] listIds;
}
