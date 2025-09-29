package org.example;

import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;
import java.util.List;

import org.example.controllers.AuthController;
import org.example.models.User;
import org.example.repositories.interfaces.UserRepository;
import org.example.repositories.implementations.UserRepositoryImpl;
import org.example.services.AuthService;


public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final UserRepository userRepository = new UserRepositoryImpl();
    private static final AuthService authService = new AuthService(userRepository); // instance unique
    private static final AuthController authController = new AuthController(authService);

    public static void main(String[] args) {
        int choice;

        do {
            System.out.println("===== BIENVENUE DANS Notre Systeme  =====");
            System.out.println("1. Login");
            System.out.println("2. Exit");
            System.out.print("Votre choix:  ");

            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> login();
                case 2 -> System.out.println("Au revoir 👋 ");
                default -> System.out.println("Choix invalide, essayez encore.");
            }
        } while (choice != 2);

        scanner.close();
    }

    private static void login() {
        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        Optional<User> userOpt = authController.login(email, password);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            System.out.println("Login réussi !");
            System.out.println("Bienvenue " + user.getName() + " (role: " + user.getRole() + ")");

            switch (user.getRole()) {
                case ADMIN -> adminMenu();
                case MANAGER -> System.out.println("➡️ Menu MANAGER ");
                case TELLER -> tellerMenu();
                case AUDITOR -> System.out.println("➡️ Menu AUDITOR ");
                default -> System.out.println("Rôle inconnu.");
            }
        } else {
            System.out.println("Login échoué, utilisateur introuvable.");
        }
    }


    private static void adminMenu() {
        int choice;
        do {
            System.out.println("\n===== MENU ADMIN (Super utilisateur) =====");
            System.out.println("1. Gérer les utilisateurs");
            System.out.println("2. Voir toutes les opérations");
            System.out.println("3. Modifier les rôles");
            System.out.println("4. Supprimer un compte utilisateur");
            System.out.println("5. Déconnexion");
            System.out.print("Votre choix: ");

            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> userManagementMenu();
                case 2 -> System.out.println("👉 Affichage de toutes les opérations (à implémenter)");
                case 3 -> System.out.println("👉 Modification des rôles (à implémenter)");
                case 4 -> System.out.println("👉 Suppression de compte utilisateur (à implémenter)");
                case 5 -> System.out.println("🔒 Déconnexion réussie !");
                default -> System.out.println("Choix invalide, essayez encore.");
            }
        } while (choice != 5);
    }

    private static void tellerMenu() {
        int choice;
        do {
            System.out.println("\n===== MENU TELLER =====");
            System.out.println("1. Create account");
            System.out.println("2. Depot d'un montant");
            System.out.println("3. retrait d'un montant");
            System.out.println("4. virement a un compte interne");
            System.out.println("5. demande de crédit");
            System.out.println("6. demande de cloture");
            System.out.println("7. Déconnexion");

            choice = scanner.nextInt();


            switch (choice) {
                case 1 -> {
                    System.out.println("ajouter un compte");
                    addClientAccount();


                }
                case 2 -> System.out.println("depot");
                case 3 -> System.out.println("retrait");
                case 4 -> System.out.println("virement");
                case 5 -> System.out.println("credit");
                case 6 -> System.out.println("cloture");
                case 7 -> System.out.println("deconnexion");
                default -> System.out.println("Choix invalide, essayez encore.");
            }
        } while (choice != 7);
    }

    private static void userManagementMenu() {
        int choice;
        do {
            System.out.println("\n===== GESTION DES UTILISATEURS =====");
            System.out.println("1. Créer un compte");
            System.out.println("2. Modifier un compte");
            System.out.println("3. Supprimer un compte");
            System.out.println("4. Lister les comptes selon le rôle");
            System.out.println("5. Retour au menu ADMIN");
            System.out.print("Votre choix: ");

            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.println("👉 Création d’un compte utilisateur");
                    createUser();
                }
                case 2 -> System.out.println("👉 Modification d’un compte utilisateur");

                case 3 -> System.out.println("👉 Suppression d’un compte");
                case 4 -> System.out.println("👉 Liste des comptes par rôle");
                case 5 -> System.out.println("↩️ Retour au menu ADMIN");
                default -> System.out.println("Choix invalide, essayez encore.");
            }
        } while (choice != 5);
    }

    private static void createUser() {
        System.out.println("\n===== CRÉATION D’UN UTILISATEUR =====");

        System.out.print("Nom: \n");
        String name = scanner.nextLine();

        System.out.print("Email: \n");
        String email = scanner.nextLine();

        System.out.print("Mot de passe: \n");
        String password = scanner.nextLine();

        System.out.print("Rôle (MANAGER / TELLER / AUDITOR): \n");
        String roleInput = scanner.nextLine().toUpperCase();
        boolean success = authController.createUser(name, email, password, roleInput);
        if (success) {
            System.out.println("Utilisateur créé avec succès !");
        } else {
            System.out.println("Erreur lors de la création de l’utilisateur.");
        }

    }

    private static void addClientAccount(){

    }

}





