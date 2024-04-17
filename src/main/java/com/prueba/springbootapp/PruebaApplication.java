package com.prueba.springbootapp;

import org.springdoc.webmvc.core.configuration.SpringDocWebMvcConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/* 
 * To prevent automatic documentation from being generated, 
 * because it is bugged in some cases and if I want to customize it 
 * I have to pollute the entire code with excessive annotations,
 * I prefer to use a manual set-up of the openapi documentation. 
 * If you want to use it anyways you can delete the exclude statement below
 * and comment all the "OpenApiController.java" file.
 * You can also download the autogenerated documentation file and use it
 * as a base for a manual set-up */
@SpringBootApplication(exclude = {SpringDocWebMvcConfiguration.class})
@ComponentScan(basePackages = "com.prueba.springbootapp")
public class PruebaApplication {

	public static void main(String[] args) {
		SpringApplication.run(PruebaApplication.class, args);
		
	}

}