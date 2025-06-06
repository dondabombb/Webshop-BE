package nl.hsleiden.WebshopBE.service;

import nl.hsleiden.WebshopBE.DAO.UserDAO;
import nl.hsleiden.WebshopBE.model.UserModel;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserDAO userDAO;

    public AuthService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public UserModel getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Geen geauthenticeerde gebruiker gevonden");
        }
        
        String userId = authentication.getName();
        
        Optional<UserModel> user = userDAO.getUser(userId);
        
        if (!user.isPresent()) {
            throw new RuntimeException("Gebruiker niet gevonden in database");
        }
        
        return user.get();
    }
} 