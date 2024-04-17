package com.prueba.springbootapp.repositories;
import java.math.BigInteger;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prueba.springbootapp.models.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, BigInteger> {
    Optional<Client> findByDocumentNumberId(String id_document);
    
    Optional<Client> findByName(String name);
}


