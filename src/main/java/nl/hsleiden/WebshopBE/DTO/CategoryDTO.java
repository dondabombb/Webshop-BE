package nl.hsleiden.WebshopBE.DTO;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDTO {
    @NotNull(message = "Category name is required")
    private String name;
    
}