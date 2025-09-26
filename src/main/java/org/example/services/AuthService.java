package org.example.services;
import java.util.Optional;

import org.example.models.User;
import org.example.repositories.interfaces.UserRepository;
import org.example.repositories.implementations.UserRepositoryImpl;

public class AuthService {
    private final UserRepositoryImpl userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = new UserRepositoryImpl();
    }
    public Optional<User> login(String email,String password){
        return userRepository.findByEmailAndPassword(email, password);
    }
}

