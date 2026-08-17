package com.matchskills.ia.service.configurations;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.matchskills.ia.service.exceptions.customs.token.TokenExpiredException;
import com.matchskills.ia.service.exceptions.customs.token.TokenInvalidException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InternalTokenValidator {

    final private String secret;
    final private Algorithm algorithm;

    public InternalTokenValidator(@Value("${jwt.internal.secret}") String secret){
        this.secret = secret;
        this.algorithm = Algorithm.HMAC256(this.secret);
    }

    public DecodedJWT validate(String token) {

        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();

            return verifier.verify(token);
        
        } catch (com.auth0.jwt.exceptions.TokenExpiredException exception){
            throw new TokenExpiredException();
        } catch (JWTVerificationException exception){
            throw new TokenInvalidException();
        }
    }
}
