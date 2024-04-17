package com.prueba.springbootapp.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.prueba.springbootapp.dto.AuthenticationResponseDTO;
import com.prueba.springbootapp.dto.LoginDTO;
import com.prueba.springbootapp.models.Client;
import com.prueba.springbootapp.services.AuthenticationService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authService;

    @PostMapping("/register")
    public AuthenticationResponseDTO register(@Valid @RequestBody Client request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthenticationResponseDTO login(@RequestBody LoginDTO loginDTO) {
        return authService.authenticate(loginDTO);
    }
}

