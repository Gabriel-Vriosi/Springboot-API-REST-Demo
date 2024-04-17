package com.prueba.springbootapp.dto;

import lombok.Data;

@Data
public class AuthenticationResponseDTO {
    
    private final String token;
    private final String message;

}
