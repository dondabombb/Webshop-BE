package nl.hsleiden.WebshopBE.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.hsleiden.WebshopBE.other.ApiResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
            return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String token = generateToken(authResult.getName());
        response.addHeader("Authorization", "Bearer " + token);
        
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        
        ApiResponse apiResponse = new ApiResponse(true, "Login successful");
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        ApiResponse apiResponse = new ApiResponse(false, "Invalid email or password");
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }

    private String generateToken(String username) {
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

    private static class LoginRequest {
        private String email;
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
} 