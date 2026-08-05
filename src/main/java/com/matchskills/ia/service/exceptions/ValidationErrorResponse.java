package com.matchskills.ia.service.exceptions;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
public class ValidationErrorResponse {
    private Integer status;
    private String message;
    private Map<String, String> errors;
}