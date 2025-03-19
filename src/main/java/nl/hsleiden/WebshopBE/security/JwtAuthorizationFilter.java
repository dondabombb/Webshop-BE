package nl.hsleiden.WebshopBE.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import nl.hsleiden.WebshopBE.DAO.UserDAO;
import nl.hsleiden.WebshopBE.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.jsonwebtoken.security.Keys;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private UserDAO userDAO;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);

            if (jwt.isBlank()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT Token in Bearer Header");
            } else {

                try {
                    String userId = jwtUtil.validateTokenAndRetrieveUserId(jwt);
                    Optional<UserModel> userDetails = userDAO.getUser(userId);

                    if (userDetails.isPresent()) {
                        UserModel user = userDetails.get();
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userId,
                                user.getPassword()
                        );


                        if (SecurityContextHolder.getContext().getAuthentication() == null) {
                            SecurityContextHolder.getContext().setAuthentication(authToken);
                        }
                    }

                } catch (JWTVerificationException exc) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT Token");
                }

            }
        }

        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(JwtProperties.HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX, "");
        
        try {
            byte[] signingKey = JwtProperties.SECRET.getBytes();
            
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(signingKey))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            String username = claims.getSubject();
            
            if (username != null) {
                String roles = (String) claims.get("rol");
                List<SimpleGrantedAuthority> authorities = null;
                
                if (roles != null) {
                    authorities = Arrays.stream(roles.split(","))
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
                }
                
                return new UsernamePasswordAuthenticationToken(username, null, authorities);
            }
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | 
                SignatureException | IllegalArgumentException e) {
            logger.error("JWT token validation failed", e);
        }
        
        return null;
    }
} 