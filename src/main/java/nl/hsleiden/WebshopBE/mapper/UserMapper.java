package nl.hsleiden.WebshopBE.mapper;

import nl.hsleiden.WebshopBE.DTO.User.CreateUserDTO;
import nl.hsleiden.WebshopBE.DTO.User.UpdateUserDTO;
import nl.hsleiden.WebshopBE.exceptions.EntryNotFoundException;
import nl.hsleiden.WebshopBE.model.AddressModel;
import nl.hsleiden.WebshopBE.model.CartModel;
import nl.hsleiden.WebshopBE.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    
    private final AddressMapper addressMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserMapper(AddressMapper addressMapper, PasswordEncoder passwordEncoder) {
        this.addressMapper = addressMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserModel convertToModel(CreateUserDTO createUserDTO) {
        String firstName = createUserDTO.getFirstName();
        String middleName = createUserDTO.getMiddleName();
        String lastName = createUserDTO.getLastName();
        String email = createUserDTO.getEmail();
        String password = createUserDTO.getPassword();

        UserModel user = new UserModel(firstName, middleName, lastName, email, passwordEncoder.encode(password));
        
        // Create a new empty cart for the user
        CartModel cart = new CartModel();
        cart.setUser(user);
        user.setCart(cart);

        return user;
    }


    public UserModel updateUser(UserModel user, UpdateUserDTO dto) {
        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }
        return user;
    }
}
