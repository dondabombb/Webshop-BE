package nl.hsleiden.WebshopBE.DTO.User;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
@Getter
@Setter
public class UpdateUserDTO {
    @NotNull(message = "Email is verpicht")
    private String email;

    @NotNull(message = "Voornaam is verpicht")
    private String firstName;

    @NotNull(message = "Achternaam is verpicht")
    private String lastName;
}
