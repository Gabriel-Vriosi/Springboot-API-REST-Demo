package com.prueba.springbootapp.services;

import java.math.BigInteger;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prueba.springbootapp.exceptionsHandlers.PersonalizedException;
import com.prueba.springbootapp.models.Client;
import com.prueba.springbootapp.repositories.ClientRepository;

import jakarta.validation.Valid;

@Service
@Transactional
public class ClientService {

    public ClientRepository clientRepository;
    
    @Autowired
    public ClientService(final ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }
    /* ****************************************************************************** */
    // public String saveClient(@Valid final Client client) {

    //     boolean IsDocIdUsed =! clientRepository
    //     .findByDocumentNumberId(client
    //     .getDocumentNumberId())
    //     .isEmpty(); 

    //     if (!IsDocIdUsed) {
    //         clientRepository.save(client);

    //         return "Client saved to database";

    //     } else {
    //         String message = String
    //         .format("This doc. ID: \"%s\" is already used",
    //             client.getDocumentNumberId());

    //         throw new PersonalizedException(message, HttpStatus.CONFLICT);
    //     }
    // }
    /* ****************************************************************************** */
    public String updateClient(@Valid final Client clientMod) {

        final Optional<Client> clientExist = clientRepository
        .findByDocumentNumberId(clientMod.getDocumentNumberId());
        
        if (clientExist.isPresent()) {
            try {
                final Client clientOnDatabase = clientExist.get();
                BeanUtils.copyProperties(clientOnDatabase, clientMod);
                clientRepository.save(clientOnDatabase);
                return "Client updated on the database";

            } catch (Exception e) {
                throw new PersonalizedException
                    ("Error updating client", 
                    HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } else {
            throw new PersonalizedException(
                "Error updating client, not found",
                HttpStatus.NOT_FOUND);
        }
    };
    /* ****************************************************************************** */
    public String deleteClient(final BigInteger id) {
        Optional<Client> clientExist = clientRepository.findById(id);

        if (clientExist.isPresent()) {
            clientRepository.deleteById(id);
            return "Client deleted from database";
        } else {
            throw new NoSuchElementException();
        }
    };
    /* ****************************************************************************** */
    public Client getClient(final BigInteger id) {
        if (id != null) {
            /*
             * To use a local variable inside lambda context it must be final or effectively final
             */
            final Client[] client = new Client[1];
            clientRepository.findById(id).ifPresentOrElse(
                result -> client[0] = result,
                () -> {throw new NoSuchElementException();
                    });
                
            return client[0];
        } else {
            throw new NoSuchElementException();
        }
    };
    /* ****************************************************************************** */
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    };

}
