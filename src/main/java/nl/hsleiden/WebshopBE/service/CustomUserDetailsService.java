package nl.hsleiden.WebshopBE.service;

import nl.hsleiden.WebshopBE.DAO.UserDAO;
import nl.hsleiden.WebshopBE.model.UserModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserDAO userDAO;

    public CustomUserDetailsService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserModel> userOpt = userDAO.getUserByEmail(email);
        
        if (!userOpt.isPresent()) {
            throw new UsernameNotFoundException("Gebruiker niet gevonden met email: " + email);
        }
        
        UserModel user = userOpt.get();
        
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (user.isAdmin()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        
        return new User(user.getEmail(), user.getPassword(), authorities);
    }
} 