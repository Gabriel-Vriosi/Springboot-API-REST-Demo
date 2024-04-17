package com.prueba.springbootapp.services;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.prueba.springbootapp.dto.AuthenticationResponseDTO;
import com.prueba.springbootapp.dto.LoginDTO;
import com.prueba.springbootapp.exceptionsHandlers.PersonalizedException;
import com.prueba.springbootapp.models.Client;
import com.prueba.springbootapp.repositories.ClientRepository;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthenticationService {
    
    private final ClientRepository clientRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /* ****************************************************************************** */
    public AuthenticationResponseDTO register(@Valid final Client client) throws PersonalizedException {
        // check if client already exist
        boolean IsDocIdUsed = !clientRepository
                .findByDocumentNumberId(client
                        .getDocumentNumberId())
                .isEmpty();

        if (!IsDocIdUsed) {
            clientRepository.save(client);
            client.setPassword(passwordEncoder.encode(client.getPassword()));
            
            String jwt = jwtService.generateToken(client);
            return new AuthenticationResponseDTO(jwt, "User registration was successful");

        } else {
            String message = String.format(
                "This doc. ID: \"%s\" is already used",
                client.getDocumentNumberId());

            throw new PersonalizedException(message, HttpStatus.CONFLICT);
        }
    }

    /* ****************************************************************************** */
    public AuthenticationResponseDTO authenticate(LoginDTO loginDTO) throws NoSuchElementException{

        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password
                )
        );

        Client client = clientRepository
            .findByName(username).orElseThrow();
        String jwt = jwtService.generateToken(client);

        return new AuthenticationResponseDTO(jwt, "User login was successful");
    }
    /* ****************************************************************************** */
}
