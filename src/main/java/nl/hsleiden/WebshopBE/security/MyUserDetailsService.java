package nl.hsleiden.WebshopBE.security;

import lombok.AllArgsConstructor;
import nl.hsleiden.WebshopBE.DAO.UserDAO;
import nl.hsleiden.WebshopBE.model.UserModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Component
@AllArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserDAO userDAO;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Optional<UserModel> foundUser = userDAO.getUser(userId);

        if(foundUser.isEmpty()){
            throw new UsernameNotFoundException("Could not find user with id = " + userId);
        }


        UserModel user = foundUser.get();
        List<GrantedAuthority> listAuthorities = new ArrayList<>();
        listAuthorities.add(new SimpleGrantedAuthority("ROLE_" + user.getUserRole()));

        return new org.springframework.security.core.userdetails.User(
                userId,
                user.getPassword(),
                listAuthorities
        );
    }
}