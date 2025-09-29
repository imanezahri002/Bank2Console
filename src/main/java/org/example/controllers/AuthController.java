package org.example.controllers;

import org.example.models.Client;
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
            System.out.println("il faut que email aura @");
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

    public static boolean validateTelephone(String telephone) {
        if (telephone == null || !telephone.matches("\\d{1,10}")) {
            System.out.println("Le numéro de téléphone doit contenir max 10 chiffres");
            return false;
        }
        return true;
    }
    public static boolean validateCIN(String cin) {
        if (cin == null || cin.trim().isEmpty()) {
            System.out.println("Le CIN est obligatoire");
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
            System.out.println("Données invalides");
            return false;
        }
        User.Role role = User.Role.valueOf(roleInput);
        User user = new User(null, name, email, password, role);
        return authService.createUser(user);
    }
    public boolean createClient(String nom, String prenom, String cin, String tel, String adresse, String email, double salaire){
        if(!validateEmail(email)||!validateCIN(cin)||!validateTelephone(tel)){
            System.out.println("Données invalides");
            return false;
        }
        else{
        Client client =new Client(null,nom,prenom,cin,tel,adresse,email,salaire);
        return authService.createClient(client);
        }

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
