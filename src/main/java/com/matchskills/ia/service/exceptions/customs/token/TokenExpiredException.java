package com.matchskills.ia.service.exceptions.customs.token;

public class TokenExpiredException extends RuntimeException {

    public TokenExpiredException() {
        super("Token is expired");
    }

}
