package org.example.repositories.interfaces;

import org.example.models.User;

import javax.management.relation.Role;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmailAndPassword(String email, String password);
    boolean addUser(User user);
}
