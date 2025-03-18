package nl.hsleiden.WebshopBE.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
import java.util.stream.Collectors;

import io.jsonwebtoken.security.Keys;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, 
                                   @NonNull FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(JwtProperties.HEADER_STRING);
        
        if (header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }
        
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
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