package com.matchskills.ia.service.exceptions.handlers;

import com.matchskills.ia.service.exceptions.CustomErrorResponse;
import com.matchskills.ia.service.exceptions.customs.jobposting.JobPostingNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class JobPostingExceptionHandler {

    @ExceptionHandler(JobPostingNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> jobPostingNotFoundException(JobPostingNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorResponse(exception.getMessage(), 404));
    }

}
