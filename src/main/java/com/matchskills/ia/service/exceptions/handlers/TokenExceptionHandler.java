package com.matchskills.ia.service.exceptions.handlers;


import com.matchskills.ia.service.exceptions.CustomErrorResponse;
import com.matchskills.ia.service.exceptions.customs.token.TokenExpiredException;
import com.matchskills.ia.service.exceptions.customs.token.TokenInBlackListException;
import com.matchskills.ia.service.exceptions.customs.token.TokenInvalidException;
import com.matchskills.ia.service.exceptions.customs.token.TokenInvalidTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TokenExceptionHandler {

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<CustomErrorResponse> handlerTokenExpiredException(TokenExpiredException exception){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new CustomErrorResponse(exception.getMessage(),401));
    }

    @ExceptionHandler(TokenInBlackListException.class)
    public ResponseEntity<CustomErrorResponse> handlerTokenInBlackListException(TokenInBlackListException exception){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new CustomErrorResponse(exception.getMessage(),401));
    }

    @ExceptionHandler(TokenInvalidException.class)
    public ResponseEntity<CustomErrorResponse> handlerTokenInvalidException(TokenInvalidException exception){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new CustomErrorResponse(exception.getMessage(),401));
    }

    @ExceptionHandler(TokenInvalidTypeException.class)
    public ResponseEntity<CustomErrorResponse> handlerTokenInvalidTypeException(TokenInvalidTypeException exception){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new CustomErrorResponse(exception.getMessage(),401));
    }

}
