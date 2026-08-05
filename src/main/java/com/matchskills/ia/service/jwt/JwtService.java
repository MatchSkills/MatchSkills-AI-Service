package com.matchskills.ia.service.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.matchskills.ia.service.domains.TokenDecoded;
import com.matchskills.ia.service.exceptions.customs.token.TokenExpiredException;
import com.matchskills.ia.service.exceptions.customs.token.TokenInvalidException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtService {

    private final Algorithm algorithm;
    private final String issuer;
    private final String audience;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.issuer}") String issuer,
            @Value("${jwt.audience}") String audience
    ) {
        this.algorithm = Algorithm.HMAC256(secret);
        this.issuer = issuer;
        this.audience = audience;
    }

    public TokenDecoded decodeToken(String token){

        try{

            DecodedJWT verifier = JWT.require(algorithm)
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .build()
                    .verify(token);


            return new TokenDecoded(verifier.getId(),verifier.getClaim("id").asLong(), verifier.getClaim("role").asString());

        } catch (com.auth0.jwt.exceptions.TokenExpiredException exception){
            throw new TokenExpiredException();
        } catch (JWTVerificationException exception){
            throw new TokenInvalidException();
        }

    }

    public String getTokenId(String token){

        try {

            DecodedJWT decoded = JWT.decode(token);

            return decoded.getId();

        } catch (JWTDecodeException exception){
            throw new TokenInvalidException();
        }


    }

    public String getToken(String rawToken){

        if (!rawToken.startsWith("Bearer ")){
            throw new TokenInvalidException();
        }

        return rawToken.replace("Bearer ", "");

    }

}
