package org.example;

import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;
import java.util.List;

import org.example.models.User;
import org.example.repositories.interfaces.UserRepository;
import org.example.repositories.implementations.UserRepositoryImpl;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final UserRepository userRepository = new UserRepositoryImpl();

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

        Optional<User> userOpt = userRepository.findByEmailAndPassword(email, password);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            System.out.println("✅ Login réussi !");
            System.out.println("Bienvenue " + user.getName() + " (role: " + user.getRole() + ")");

            switch (user.getName().toUpperCase()) {
                case "ADMIN" -> adminMenu();
                case "MANAGER" -> System.out.println("➡️ Menu MANAGER (à implémenter)");
                case "TELLER" -> System.out.println("➡️ Menu TELLER (à implémenter)");
                case "AUDITOR" -> System.out.println("➡️ Menu AUDITOR (à implémenter)");
                default -> System.out.println("❌ Rôle inconnu.");
            }
        } else {
            System.out.println("❌ Login échoué, utilisateur introuvable.");
        }
    }

    // === MENU ADMIN (Super utilisateur) ===
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
                    System.out.println("👉 Création d’un compte (à implémenter)");
                    createUser();
                    }
                case 2 -> System.out.println("👉 Modification d’un compte (à implémenter)");
                case 3 -> System.out.println("👉 Suppression d’un compte (à implémenter)");
                case 4 -> System.out.println("👉 Liste des comptes par rôle (à implémenter)");
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


        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        try {
            User.Role role = User.Role.valueOf(roleInput); // conversion String → Enum
            user.setRole(role);
        } catch (IllegalArgumentException e) {
            System.out.println("⚠️ Rôle invalide. Veuillez choisir TELLER / MANAGER / AUDITOR.");
        }


        boolean success = userRepository.addUser(user);

        if (success) {
            System.out.println("Utilisateur créé avec succès !");
        } else {
            System.out.println("Erreur lors de la création de l’utilisateur.");
        }
    }
}

