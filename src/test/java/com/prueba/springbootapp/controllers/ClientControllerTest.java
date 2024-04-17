package com.prueba.springbootapp.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.prueba.springbootapp.exceptionsHandlers.PersonalizedException;
import com.prueba.springbootapp.models.Client;
import com.prueba.springbootapp.models.Role;
import com.prueba.springbootapp.services.ClientService;

@WebMvcTest(ClientController.class)
public class ClientControllerTest {

    /* Instead of using a controller layer bean we need to use this mockMvc */
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ClientService clientService;
    private Client client1;
    private Client client2; 

    /* ****************************************************************************** */
    @BeforeEach
    void setUp() {
        client1 = new Client("1", "ganimedes", "xxxx", Role.ADMIN, "pass");
        client2 = new Client("2", "gertrudis", "yyyy", Role.ADMIN, "pass");

    }
    /* ****************************************************************************** */
    @AfterEach
    void tearDown() {
        
    }
    /* ****************************************************************************** */
    @Test
    void testGetClientDetails_success() throws Exception {
        when(clientService.getClient(any(BigInteger.class)))
        .thenReturn(client1);

        mockMvc.perform(get("/Client/1"))
                .andDo(print())
                .andExpect(status()
                .isOk());

    }
    /* ****************************************************************************** */
    @Test
    void testGetClientDetails_fail() throws Exception {
        /* Invalid url */
        mockMvc.perform(get("/Client/invalidText"))
                .andDo(print())
                .andExpect(status()
                .isBadRequest());

    }
    /* ****************************************************************************** */
    @Test
    void testGetAllClients() throws Exception {
        ArrayList<Client> clients = new ArrayList<>(Arrays.asList(client1, client2));
        /* The list can be empty but not null */
        when(clientService.getAllClients())
        .thenReturn(clients);

        mockMvc.perform(get("/Client/"))
                .andDo(print())
                .andExpect(status()
                .isOk());

    }
    /* ****************************************************************************** */   
    @Test
    void testUpdateClientDetails_success() throws Exception {
        /* Map the client to JSON */
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(client1);

        String message = "Client updated on the database";
        when(clientService.updateClient(any(Client.class)))
        .thenReturn(message);

        mockMvc.perform(put("/Client/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk());
    }
    /* ****************************************************************************** */  
    @Test
    void testUpdateClientDetails_fail_notFound() throws Exception {
        /* Map the client to JSON */
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(client1);
        
        String message = "Error updating client, not found";
        when(clientService.updateClient(any(Client.class)))
        .thenThrow(new PersonalizedException(message, HttpStatus.NOT_FOUND));

        mockMvc.perform(put("/Client/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
    /* ****************************************************************************** */
    @Test
    void testUpdateClientDetails_fail_copyingProperties() throws Exception {
        /* Map the client to JSON */
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(client1);

        String message = "Error copying properties";
        when(clientService.updateClient(any(Client.class)))
        .thenThrow(new PersonalizedException(message, HttpStatus.BAD_REQUEST));

        mockMvc.perform(put("/Client/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
    /* ****************************************************************************** */
    /*
     * deleteById from org.springframework.data.repository.CrudRepository
     * is ignored if the entity is not found, so that case will not be covered
     */
    @Test
    void testDeleteClientDetails_success() throws Exception {
        String message = "Client deleted from database";
        when(clientService.deleteClient(any(BigInteger.class)))
        .thenReturn(message);

        mockMvc.perform(delete("/Client/1"))
                .andDo(print())
                .andExpect(status()
                .isOk());

    }
    /* ****************************************************************************** */
    @Test
    void testDeleteClientDetails_fail_nullID() throws Exception {
        String message = "Error deleting client, null ID";
        when(clientService.deleteClient(null))
        .thenThrow(new PersonalizedException(message, HttpStatus.BAD_REQUEST));

        mockMvc.perform(delete("/Client/null"))
                .andDo(print())
                .andExpect(status()
                .isBadRequest());

    }
    /* ****************************************************************************** */

}
