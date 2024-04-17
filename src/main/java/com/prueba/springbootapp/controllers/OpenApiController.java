package com.prueba.springbootapp.controllers;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
public class OpenApiController {

    @GetMapping(value = "/api-docs", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getOpenApiJson() throws IOException {
        ClassPathResource resource = new ClassPathResource("openapi-config.yaml");
        Path path = resource.getFile().toPath();
        String content = new String(Files.readAllBytes(path));
        return ResponseEntity.ok(content);
    }
}
