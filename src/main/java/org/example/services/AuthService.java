package org.example.services;
import java.util.Optional;

import org.example.models.Client;
import org.example.models.User;
import org.example.repositories.implementations.ClientRepositoryImpl;
import org.example.repositories.interfaces.ClientRepository;
import org.example.repositories.interfaces.UserRepository;
import org.example.repositories.implementations.UserRepositoryImpl;

public class AuthService {
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;

    public AuthService(UserRepository userRepository, ClientRepository clientRepository) {
        this.userRepository = userRepository;
        this.clientRepository=clientRepository;
    }
    public Optional<User> login(String email,String password){
        return userRepository.findByEmailAndPassword(email, password);
    }

    public boolean createUser(User user) {
        return userRepository.addUser(user);
    }
    public boolean createClient(Client client){
        return clientRepository.createClient(client);
    }
}

