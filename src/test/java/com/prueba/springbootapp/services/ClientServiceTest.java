package com.prueba.springbootapp.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import com.prueba.springbootapp.exceptionsHandlers.PersonalizedException;
import com.prueba.springbootapp.models.Client;
import com.prueba.springbootapp.models.Role;
import com.prueba.springbootapp.repositories.ClientRepository;



public class ClientServiceTest {

    /* Mocking the repository layer to isolate the test of the service layer  */
    @Mock
    private ClientRepository clientRepository;
    private ClientService clientService;
    private Client client;
    /* Necessary to clear the repository resources after finishing */
    AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        client = new Client("45", "gervasio", "89", Role.ADMIN, "pass");
        autoCloseable = MockitoAnnotations.openMocks(this);
        clientService = new ClientService(clientRepository);
        
    }
    /* ****************************************************************************** */
    @AfterEach 
    void tearDown() throws Exception {
        autoCloseable.close();
        
    }
    /* ****************************************************************************** */
    @Test
    void testUpdateClient_success() {

        when(clientRepository.findByDocumentNumberId(anyString()))
        .thenReturn(Optional.of(new Client()));

        final String expectedMessage = "Client updated on the database";
        final String actualMessage = clientService.updateClient(client);

        assertEquals(expectedMessage, actualMessage);

        verify(clientRepository, times(1))
        .save(any());

    }
    /* ****************************************************************************** */
    @Test
    void testUpdateClient_fail_notFound() {

        when(clientRepository.findByDocumentNumberId(anyString()))
        .thenReturn(Optional.empty());

        final PersonalizedException ex = assertThrows(
            PersonalizedException.class,
            () -> clientService.updateClient(client));

        final String expectedMessage = "Error updating client, not found";
        final String actualMessage = ex.getMessage();

        assertEquals(expectedMessage, actualMessage);
        assertEquals(HttpStatus.NOT_FOUND, ex.getHttpStatus());

        verify(clientRepository, never())
        .save(any());

    }
    /* ****************************************************************************** */
    @Test
    void testUpdateClient_fail_nullInvoke() {

        assertThrows(NullPointerException.class,
            () -> clientService.updateClient(null));

        verify(clientRepository, never())
        .save(any());

    }
    /* ****************************************************************************** */
    @Test
    void testGetClient_success() {

        when(clientRepository.findById(any(BigInteger.class)))
        .thenReturn(Optional.of(client));
            
        final BigInteger expectedID = client.getId();
        final BigInteger actualID = clientService
            .getClient(BigInteger.valueOf(111))
            .getId();

        assertEquals(expectedID, actualID);

    }
    /* ****************************************************************************** */    
    @Test
    void testGetClient_fail_nullInvoke(){
        assertThrows(NoSuchElementException.class, 
            () -> clientService.getClient(null));
    
    }
    /* ****************************************************************************** */
    @Test
    void testGetClient_fail_notFound(){

        when(clientRepository.findById(any(BigInteger.class)))
        .thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> clientService.getClient(BigInteger.valueOf(1)));

    }
    /* ****************************************************************************** */
    @Test
    void testGetAllClients() {

        when(clientRepository.findAll())
        .thenReturn(
        new ArrayList<Client>(Collections.singleton(client)));

        /* assertj */
        assertThat(clientService
        .getAllClients()
        .get(0))
        .usingRecursiveComparison()
        .isEqualTo(client);

    }
    /* ****************************************************************************** */
    /*
     * deleteById from org.springframework.data.repository.CrudRepository
     * is ignored if the entity is not found, so that case will not be covered
     */
    @Test
    void testDeleteClient_success() {
        
        when(clientRepository.findById(any(BigInteger.class)))
        .thenReturn(Optional.of(client));

        doNothing()
        .when(clientRepository)
        .deleteById(any(BigInteger.class));

        final String expectedMessage = "Client deleted from database";
        final String actualMessage = clientService
            .deleteClient(new BigInteger("1"));

        assertEquals(expectedMessage, actualMessage);

        verify(clientRepository)
        .deleteById(new BigInteger("1"));

    }
    /* ****************************************************************************** */
    @Test
    void testDeleteClient_fail_nullInvoke() {

        assertThrows(NoSuchElementException.class,
            () -> clientService.deleteClient(null));

        verify(clientRepository, times(0))
        .deleteById(null);

    }
}
