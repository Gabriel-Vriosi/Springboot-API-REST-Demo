package com.prueba.springbootapp.controllers;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.prueba.springbootapp.models.Client;
import com.prueba.springbootapp.services.ClientService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/Client")
public class ClientController {

    public ClientService clientService;
    
    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }
    /* ****************************************************************************** */
    @GetMapping("/{clientId}")
    public Client getClientDetails(@PathVariable BigInteger clientId) throws MethodArgumentTypeMismatchException {
        return clientService.getClient(clientId);

    }
    /* ****************************************************************************** */
    @GetMapping("/")
    public List<Client> getAllClients(){
        return clientService.getAllClients();
    }
    /* ****************************************************************************** */
    @PutMapping("/")
    @ResponseBody
    public String updateClientDetails(@Valid @RequestBody Client client){
        return clientService.updateClient(client);
    }
    /* ****************************************************************************** */
    @DeleteMapping("/{clientId}")
    @ResponseBody
    public String deleteClientDetails(@PathVariable BigInteger clientId) throws MethodArgumentTypeMismatchException {
        return clientService.deleteClient(clientId);
    }

}
