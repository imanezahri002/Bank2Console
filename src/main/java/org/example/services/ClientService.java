package org.example.services;

import org.example.models.Client;
import org.example.repositories.implementations.ClientRepositoryImpl;
import org.example.repositories.interfaces.ClientRepository;

import java.util.Optional;

public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository){
        this.clientRepository=clientRepository;
    }
    public Optional<Client>findByCin(String cin){
        return clientRepository.findByCin(cin);
    }



}
