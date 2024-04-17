package com.prueba.springbootapp.exceptionsHandlers;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class GlobalExceptionHandler {

    /*
     * Detects errors for subsequent treatment
     */
    // @ExceptionHandler(Exception.class)
    // @ResponseBody
    // public ResponseEntity<String> errorDetector(Exception e) {
    //     System.out.println("\n***********************************************************************\n");
    //     System.out.println("Error class: " + e.getClass());
    //     System.out.println("\n-----------------------------------------------------------------------\n");
    //     System.out.println("Cause: " + e.getCause());
    //     System.out.println("\n***********************************************************************\n");
    //     // e.printStackTrace();
    //     System.out.println("Method: " + e.getStackTrace()[0].getMethodName());
    //     System.out.println("\n***********************************************************************\n");
    //     System.out.println("In class: " + e.getStackTrace()[0].getClassName());
    //     System.out.println("\n***********************************************************************\n");
    //     return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
    // }

    @ExceptionHandler(PersonalizedException.class)
    public ResponseEntity<String> PersonalizedExceptionHandler(PersonalizedException e) {
        return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String httpRequestMethodNotSupportedException(Exception e) {
        return "Invalid request";
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public String NoSuchElementExceptionHandler(Exception e){
        return "Value not found";
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, String> MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

}
