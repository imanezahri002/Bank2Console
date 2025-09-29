package org.example.repositories.interfaces;

import org.example.models.Client;

import java.util.Optional;

public interface ClientRepository {

    boolean createClient(Client client);
    Optional<Client> findByCin(String cin);

}
