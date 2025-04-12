package nl.hsleiden.WebshopBE.controller;

import lombok.AllArgsConstructor;
import nl.hsleiden.WebshopBE.DAO.UserDAO;
import nl.hsleiden.WebshopBE.DTO.User.CreateUserDTO;
import nl.hsleiden.WebshopBE.DTO.User.LoginDTO;
import nl.hsleiden.WebshopBE.DTO.User.UpdateUserDTO;
import nl.hsleiden.WebshopBE.mapper.UserMapper;
import nl.hsleiden.WebshopBE.model.CartModel;
import nl.hsleiden.WebshopBE.model.UserModel;
import nl.hsleiden.WebshopBE.other.ApiResponse;
import nl.hsleiden.WebshopBE.security.JWTUtil;
import nl.hsleiden.WebshopBE.service.ApiResponseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import nl.hsleiden.WebshopBE.constant.ApiConstant;
import nl.hsleiden.WebshopBE.model.AddressModel;
import nl.hsleiden.WebshopBE.DTO.AddressDTO;
import nl.hsleiden.WebshopBE.mapper.AddressMapper;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping()
@Validated
@AllArgsConstructor
public class UserController {

    private final UserDAO userDAO;
    private final AuthenticationManager authManager;
    private final JWTUtil jwtUtil;
    private final UserMapper userMapper;
    private final AddressMapper addressMapper;

    @PostMapping(value = ApiConstant.register)
    @ResponseBody
    public ApiResponseService registerUser(@Valid @RequestBody CreateUserDTO createUserDTO) {
        ApiResponse response = new ApiResponse();

        // Controleer of email al in gebruik is
        Optional<UserModel> existingUser = userDAO.getUserByEmail(createUserDTO.getEmail());
        if (existingUser.isPresent()) {
            response.setMessage("Email is al in gebruik");
            return new ApiResponseService(false, HttpStatus.CONFLICT, response);
        }

        // Maak nieuwe gebruiker
        UserModel user = userMapper.convertToModel(createUserDTO);

        // Maak een nieuwe winkelwagen voor de gebruiker
        CartModel cart = new CartModel();
        cart.setUser(user);
        user.setCart(cart);

        // Sla gebruiker op
        UserModel savedUser = userDAO.createUser(user);

        response.setMessage("Gebruiker succesvol geregistreerd");
        response.setResult(savedUser);
        return new ApiResponseService(true, HttpStatus.CREATED, response);
    }

    @PostMapping(value = ApiConstant.login)
    @ResponseBody
    public ApiResponseService login(@Valid @RequestBody LoginDTO loginDTO) {
        ApiResponse response = new ApiResponse();
    
        String userId;
        String userRole;
    
        Optional<UserModel> foundUser = userDAO.getUserByEmail(loginDTO.getEmail());
        if (!foundUser.isPresent()) {
            response.setMessage("De combinatie van email en wachtwoord is incorrect");
            return new ApiResponseService(false, HttpStatus.UNAUTHORIZED, response);
        }
        userId = foundUser.get().getId();
        userRole = foundUser.get().getUserRole();
    
        UsernamePasswordAuthenticationToken authInputToken = new UsernamePasswordAuthenticationToken(userId, loginDTO.getPassword());
        try {
            authManager.authenticate(authInputToken);
        } catch (Throwable $throwable) {
            response.setMessage("De combinatie van email en wachtwoord is incorrect");
            return new ApiResponseService(false, HttpStatus.UNAUTHORIZED, response);
        }
    
        String token = jwtUtil.generateToken(userId, userRole);
        System.out.println("Generated JWT token: " + token);
        response.addField("JWT", token);
        response.addField("userRole", userRole);
        response.addField("userId", userId);
    
        return new ApiResponseService(true, HttpStatus.ACCEPTED, response);
    }


    @GetMapping(value = ApiConstant.getUser)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @ResponseBody
    public ApiResponseService getUser(@PathVariable String userId) {
        ApiResponse response = new ApiResponse();

        Optional<UserModel> user = userDAO.getUser(userId);
        if (!user.isPresent()) {
            response.setMessage("Gebruiker niet gevonden");
            return new ApiResponseService(false, HttpStatus.NOT_FOUND, response);
        }

        // Check if the requesting user is either an ADMIN or the user themselves
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        boolean isSelf = authentication.getName().equals(userId);

        if (!isAdmin && !isSelf) {
            response.setMessage("Je hebt geen toegang tot deze gebruiker");
            return new ApiResponseService(false, HttpStatus.FORBIDDEN, response);
        }

        response.setResult(user.get());
        return new ApiResponseService(true, HttpStatus.OK, response);
    }

    @GetMapping(value = ApiConstant.getAllUsers)
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public ApiResponseService getAllUsers() {
        ApiResponse response = new ApiResponse();

        List<UserModel> users = userDAO.getAllUsers();

        response.setResult(users);
        return new ApiResponseService(true, HttpStatus.OK, response);
    }

    @PutMapping(value = ApiConstant.getUser)
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public ApiResponseService updateUser(@PathVariable String userId, @Valid @RequestBody UpdateUserDTO updateUserDTO) {
        ApiResponse response = new ApiResponse();

        Optional<UserModel> existingUser = userDAO.getUser(userId);
        if (existingUser.isEmpty()) {
            response.setMessage("Gebruiker niet gevonden");
            return new ApiResponseService(false, HttpStatus.NOT_FOUND, response);
        }

        // Controleer of email al in gebruik is door een andere gebruiker
        if (updateUserDTO.getEmail() != null) {
            Optional<UserModel> userWithEmail = userDAO.getUserByEmail(updateUserDTO.getEmail());
            if (userWithEmail.isPresent() && !userWithEmail.get().getId().equals(userId)) {
                response.setMessage("Email is al in gebruik");
                return new ApiResponseService(false, HttpStatus.CONFLICT, response);
            }
        }

        UserModel updatedUser = userMapper.updateUser(existingUser.get(), updateUserDTO);
        UserModel savedUser = userDAO.updateUser(updatedUser);

        response.setMessage("Gebruiker succesvol bijgewerkt");
        response.setResult(savedUser);
        return new ApiResponseService(true, HttpStatus.OK, response);
    }


    @DeleteMapping(value = ApiConstant.getUser)
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public ApiResponseService deleteUser(@PathVariable String userId) {
        ApiResponse response = new ApiResponse();

        Optional<UserModel> existingUser = userDAO.getUser(userId);
        if (existingUser.isEmpty()) {
            response.setMessage("Gebruiker niet gevonden");
            return new ApiResponseService(false, HttpStatus.NOT_FOUND, response);
        }

        userDAO.deleteUser(userId);

        response.setMessage("Gebruiker succesvol verwijderd");
        return new ApiResponseService(true, HttpStatus.OK, response);
    }

    @PutMapping(value = ApiConstant.addShippingAddressToUser, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public ApiResponseService updateShippingAddress(@PathVariable String userId, @Valid @RequestBody AddressDTO addressDTO) {
        ApiResponse response = new ApiResponse();

        // Check if user exists
        Optional<UserModel> existingUser = userDAO.getUser(userId);
        if (existingUser.isEmpty()) {
            response.setMessage("Gebruiker niet gevonden");
            return new ApiResponseService(false, HttpStatus.NOT_FOUND, response);
        }

        // Check if user is authorized (either admin or the user themselves)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        boolean isSelf = authentication.getName().equals(userId);

        if (!isAdmin && !isSelf) {
            response.setMessage("Je hebt geen toegang tot deze gebruiker");
            return new ApiResponseService(false, HttpStatus.FORBIDDEN, response);
        }

        UserModel user = existingUser.get();
        AddressModel addressModel = addressMapper.toModel(addressDTO);
        user.setShippingAddress(addressModel);
        UserModel updatedUser = userDAO.updateUser(user);

        response.setMessage("Verzendadres succesvol bijgewerkt");
        response.setResult(updatedUser);
        return new ApiResponseService(true, HttpStatus.OK, response);
    }

    @PutMapping(value = ApiConstant.addBillingAddressToUser, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public ApiResponseService updateBillingAddress(@PathVariable String userId, @Valid @RequestBody AddressDTO addressDTO) {
        ApiResponse response = new ApiResponse();

        // Check if user exists
        Optional<UserModel> existingUser = userDAO.getUser(userId);
        if (existingUser.isEmpty()) {
            response.setMessage("Gebruiker niet gevonden");
            return new ApiResponseService(false, HttpStatus.NOT_FOUND, response);
        }

        // Check if user is authorized (either admin or the user themselves)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        boolean isSelf = authentication.getName().equals(userId);

        if (!isAdmin && !isSelf) {
            response.setMessage("Je hebt geen toegang tot deze gebruiker");
            return new ApiResponseService(false, HttpStatus.FORBIDDEN, response);
        }

        UserModel user = existingUser.get();
        AddressModel addressModel = addressMapper.toModel(addressDTO);
        user.setBillingAddress(addressModel);
        UserModel updatedUser = userDAO.updateUser(user);

        response.setMessage("Factuuradres succesvol bijgewerkt");
        response.setResult(updatedUser);
        return new ApiResponseService(true, HttpStatus.OK, response);
    }
}