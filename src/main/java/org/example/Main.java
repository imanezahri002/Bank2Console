package org.example;

import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;
import java.util.List;

import org.example.controllers.AuthController;
import org.example.models.Client;
import org.example.models.User;
import org.example.repositories.implementations.AccountRepositoryImpl;
import org.example.repositories.implementations.ClientRepositoryImpl;
import org.example.repositories.interfaces.AccountRepository;
import org.example.repositories.interfaces.ClientRepository;
import org.example.repositories.interfaces.UserRepository;
import org.example.repositories.implementations.UserRepositoryImpl;
import org.example.services.AccountService;
import org.example.services.AuthService;
import org.example.services.ClientService;


public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final UserRepository userRepository = new UserRepositoryImpl();
    private static final ClientRepository clientRepository=new ClientRepositoryImpl();
    private static final ClientService clientService=new ClientService(clientRepository);
    private static final AuthService authService = new AuthService(userRepository,clientRepository); // instance unique
    private static final AuthController authController = new AuthController(authService);
    private static final AccountRepository accountRepository=new AccountRepositoryImpl();
    private static final AccountService accountService=new AccountService(accountRepository);

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

            System.out.println("1. Create client");
            System.out.println("2. ajouter compte");
            System.out.println("3. retrait d'un montant");
            System.out.println("4. virement a un compte interne");
            System.out.println("5. demande de crédit");
            System.out.println("6. demande de cloture");
            System.out.println("7. Déconnexion");

            choice = scanner.nextInt();
            scanner.nextLine();


            switch (choice) {
                case 1 -> {
                    System.out.println("ajouter un client");
                    addClient();
                }
                case 2 -> {
                    System.out.println("ajouter un compte");
                    addAccount();
                }

                case 3 -> System.out.println("retrait");
                case 4 -> System.out.println("virement");
                case 5 -> System.out.println("credit");
                case 6 -> System.out.println("cloture");
                case 7 -> System.out.println("deconnexion");
                default -> System.out.println("Choix invalide, essayez encore.");
            }
        } while (choice != 7);
        scanner.close();
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

    private static void addClient(){
        System.out.println("===== CRÉATION D’UN CLIENT =====");

        System.out.print("Prenom: \n");
        String prenom =scanner.nextLine();

        System.out.print("Nom: \n");
        String nom=scanner.nextLine();

        System.out.print("CIN: \n");
        String cin=scanner.nextLine();

        System.out.print("Tel: \n");
        String tel=scanner.nextLine();

        System.out.print("Adresse: \n");
        String addresse=scanner.nextLine();

        System.out.print("Email: \n");
        String email=scanner.nextLine();

        System.out.print("Salaire: \n");
        double salaire =scanner.nextDouble();

        boolean success=authController.createClient(nom,prenom,cin,tel,addresse,email,salaire);
        if(success){
            System.out.println("Utilisateur créé avec succès !");
        }else System.out.println("Erreur lors de la création de l’utilisateur.");
    }

    private static void addAccount(){
        System.out.println("===== CRÉATION D’UN COMPTE =====");

        System.out.print("Veuiller saisir Cin: \n");
        String cin =scanner.nextLine();

        Optional<Client> clientOpt = clientService.findByCin(cin);

        if (clientOpt.isPresent()) {
            Client client = clientOpt.get();
            System.out.println("Client trouvé : " + client.getFirstName() + " " + client.getLastName());

            createBankAccountForClient(client);
        } else {
            System.out.println("Aucun client trouvé avec ce CIN. Veuillez d'abord créer le client.");
        }
    }
    private static void createBankAccountForClient(Client client){
        System.out.println("creation du compte");


    }

}





