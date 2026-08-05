package com.matchskills.ia.service.exceptions.customs.token;

public class TokenInvalidException extends RuntimeException {
    public TokenInvalidException() {
        super("Token is invalid");
    }
}
