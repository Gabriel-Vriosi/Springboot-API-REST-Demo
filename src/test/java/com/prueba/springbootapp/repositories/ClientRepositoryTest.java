package com.prueba.springbootapp.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.prueba.springbootapp.models.Client;
import com.prueba.springbootapp.models.Role;

@DataJpaTest
public class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;
    private Client client;


    @BeforeEach
    void setUp() {
        client = new Client("xx", "Gabriel", "12345", Role.ADMIN, "pass");
        clientRepository.save(client);
        
    }

    @AfterEach
    void tearDown() {
        client = null;
        clientRepository.deleteAll();
        
    }

    /* ****************************************************************************** */

    @Test
    void testFindByDocumentNumberId_success() {
        Optional<Client> clientOptional = clientRepository.findByDocumentNumberId("xx");
        assertEquals(
            clientOptional.map(Client::getDocumentNumberId).orElse(""),
            client.getDocumentNumberId());
    
    }

    @Test
    void testFindByDocumentNumberId_fail() {
        Optional<Client> clientOptional = clientRepository.findByDocumentNumberId("12");
        assertNotEquals(
                clientOptional.map(Client::getDocumentNumberId).orElse(""),
                client.getDocumentNumberId());

    }

    /* ****************************************************************************** */
}
