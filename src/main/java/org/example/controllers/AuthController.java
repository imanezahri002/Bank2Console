package org.example.controllers;

import org.example.models.User;
import org.example.services.AuthService;

import java.util.Optional;

public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService=authService;
    }

    public boolean validateEmail(String email){
        if(!email.contains("@")){
            System.out.println("ila faut que email aura @");
            return false;
        }
        return true;

    }

    public boolean validatePassword(String password){
        if(!(password.length()==6)){
            System.out.println("le password doit avoir 6 caractere");
            return false;
        }
        return true;

    }

    public boolean validateRole(String roleInput){
        try {
            User.Role.valueOf(roleInput.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("Rôle invalide. Veuillez choisir TELLER / MANAGER / AUDITOR.");
            return false;
        }
    }
    public boolean createUser(String name, String email, String password, String roleInput) {
        if (!validateEmail(email) || !validatePassword(password) || !validateRole(roleInput)) {
            System.out.println("❌ Données invalides");
            return false;
        }
        User.Role role = User.Role.valueOf(roleInput);
        User user = new User(null, name, email, password, role);
        return authService.createUser(user);
    }
    public Optional<User> login(String email, String password) {

        if (!validateEmail(email) || !validatePassword(password)) {
            System.out.println("Email ou mot de passe invalide");
            return Optional.empty();
        }
        Optional<User> userOpt = authService.login(email, password);

        if (userOpt.isEmpty()) {
            System.out.println("Login échoué, utilisateur introuvable.");
        }

        return userOpt;
    }
}
