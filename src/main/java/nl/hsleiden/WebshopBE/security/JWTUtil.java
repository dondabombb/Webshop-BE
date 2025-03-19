package nl.hsleiden.WebshopBE.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class JWTUtil {

    public String generateToken(String userId) throws IllegalArgumentException, JWTCreationException {
        return JWT.create()
                .withSubject("User Details")
                .withClaim("userId", userId)
                .withIssuedAt(new Date())
                .sign(Algorithm.HMAC256(JwtProperties.SECRET));
    }

    public String validateTokenAndRetrieveUserId(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(JwtProperties.SECRET))
                .withSubject("User Details")
                .build();
        DecodedJWT jwt = verifier.verify(token);

        return jwt.getClaim("userId").asString();
    }

}