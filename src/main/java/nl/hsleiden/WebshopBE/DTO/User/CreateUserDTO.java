package nl.hsleiden.WebshopBE.DTO.User;

import lombok.Getter;
import lombok.Setter;
import nl.hsleiden.WebshopBE.DTO.AddressDTO;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateUserDTO {

    @NotNull(message = "Gebruiker's voornaam is verplicht")
    private String firstName;

    private String middleName;

    @NotNull(message = "Gebruiker's achternaam is verplicht")
    private String lastName;

    @NotNull(message = "Gebruiker's email is verplicht")
    private String email;

    @NotNull(message = "Gebruiker's wachtwoord is verplicht")
    private String password;

}
