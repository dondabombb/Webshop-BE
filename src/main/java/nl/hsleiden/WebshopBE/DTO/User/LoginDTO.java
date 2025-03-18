package nl.hsleiden.WebshopBE.DTO.User;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class LoginDTO {
    @NotNull(message = "Email is verplicht")
    private String email;

    @NotNull(message = "Wachtwoord is verplicht")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
} 