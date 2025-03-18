package nl.hsleiden.WebshopBE.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import nl.hsleiden.WebshopBE.DAO.UserDAO;
import nl.hsleiden.WebshopBE.DTO.User.LoginDTO;
import nl.hsleiden.WebshopBE.model.UserModel;
import nl.hsleiden.WebshopBE.other.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SecurityService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;
    
    @Autowired
    public SecurityService(AuthenticationManager authenticationManager, 
                          UserDetailsService userDetailsService,
                          UserDAO userDAO,
                          PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
    }
    
    public ApiResponse login(LoginDTO loginDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
            );

            String token = generateToken(authentication.getName());
            return new ApiResponse(true, "Login successful");
        } catch (AuthenticationException e) {
            return new ApiResponse(false, "Invalid email or password");
        }
    }

    public String generateToken(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        byte[] signingKey = JwtProperties.SECRET.getBytes();

        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(signingKey))
                .setHeaderParam("typ", "JWT")
                .setSubject(userDetails.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                .claim("rol", userDetails.getAuthorities().stream()
                        .map(authority -> authority.getAuthority())
                        .findFirst()
                        .orElse(""))
                .compact();
    }

    public ApiResponse register(LoginDTO registerDTO) {
        if (userDAO.getUserByEmail(registerDTO.getEmail()).isPresent()) {
            return new ApiResponse(false, "Email already exists");
        }

        // Create new user and save to database
        UserModel newUser = new UserModel();
        newUser.setEmail(registerDTO.getEmail());
        newUser.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        
        userDAO.createUser(newUser);
        return new ApiResponse(true, "Registration successful");
    }
} 