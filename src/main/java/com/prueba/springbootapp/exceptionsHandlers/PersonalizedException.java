package com.prueba.springbootapp.exceptionsHandlers;

import org.springframework.http.HttpStatus;

public class PersonalizedException extends RuntimeException{
    private HttpStatus httpStatus;

    public PersonalizedException(String message, HttpStatus status) {
        super(message);
        this.httpStatus = status;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
