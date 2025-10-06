package org.example;

import java.math.BigDecimal;
import java.sql.SQLOutput;
import java.time.Instant;
import java.util.*;

import org.example.controllers.AuthController;
import org.example.models.*;
import org.example.repositories.implementations.*;
import org.example.repositories.interfaces.*;
import org.example.services.*;


public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final UserRepository userRepository = new UserRepositoryImpl();
    private static final ClientRepository clientRepository=new ClientRepositoryImpl();
    private static final ClientService clientService=new ClientService(clientRepository);
    private static final AuthService authService = new AuthService(userRepository,clientRepository);
    private static final AuthController authController = new AuthController(authService);
    private static final AccountRepository accountRepository=new AccountRepositoryImpl();
    private static final AccountService accountService=new AccountService(accountRepository);
    private static final FeeRuleRepository feeRoleRepository=new FeeRuleRepositoryImpl();
    private static final TransactionRepository transactionRepository=new TransactionRepositoryImpl(accountRepository,feeRoleRepository);
    private static final FeeRuleService feeRuleService=new FeeRuleService(feeRoleRepository);
    private static final TransactionService transactionService=new TransactionService(transactionRepository,accountRepository,feeRoleRepository);
    private static final CreditRepository creditRepository=new CreditRepositoryImpl(accountRepository,feeRoleRepository);
    private static final CreditService creditService=new CreditService(creditRepository,accountRepository,feeRoleRepository);

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
                case MANAGER ->  managerMenu();
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
            System.out.println("2. Gérer les fee_rule");
            System.out.println("3. Modifier les rôles");
            System.out.println("4. Supprimer un compte utilisateur");
            System.out.println("5. Déconnexion");
            System.out.print("Votre choix: ");

            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> userManagementMenu();
                case 2 -> fee_ruleManagementMenu();
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
            System.out.println("3. deposer un montant");
            System.out.println("4. Retrait un montant");
            System.out.println("5. transfer IN");
            System.out.println("6. transfer OUT");
            System.out.println("7. passer un virement Externe");
            System.out.println("8. demande de credit");
            System.out.println("9. Déconnexion");

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

                case 3 -> {
                    System.out.println("Deposer un montant");
                    deposit();
                }
                case 4 -> System.out.println("Retrait un montant");

                case 5 -> {
                    System.out.println("Transfer IN");
                    transfererIn();
                }
                case 6 -> {
                    System.out.println("Transfer OUT");
                    transfererOut();
                }
                case 7 ->{
                    System.out.println("Virement Externe");
                    transferExterne();
                }
                case 8 ->{
                    System.out.println("Ajouter un Credit");
                    addCredit();
                }
                case 9 -> System.out.println("Deconnexion");
                default -> System.out.println("Choix invalide, essayez encore.");
            }
        } while (choice != 9);
        scanner.close();
    }

    private static void managerMenu(){
        int choice;
        do {
            System.out.println("\n===== MENU TELLER =====");

            System.out.println("1. Valider Transfer OUT");
            System.out.println("2. Valider Virement Externe");
            System.out.println("3. deposer un montant");
            System.out.println("4. Retrait un montant");
            System.out.println("5. transfer IN");
            System.out.println("6. transfer OUT");
            System.out.println("7. demande de crédit");
            System.out.println("8. demande de cloture");
            System.out.println("9. Déconnexion");

            choice = scanner.nextInt();
            scanner.nextLine();


            switch (choice){
            case 1 -> {
                System.out.println("Valider Transfer OUT");
                validerTransferOut();
            }

            case 2 -> {
                System.out.println("Valider virement Externe");
                validerTransferEtranger();
            }

            case 3 -> {
                System.out.println("Deposer un montant");

            }
            case 4 -> System.out.println("Retrait un montant");

            case 5 -> {
                System.out.println("Transfer IN");

            }
            case 6 -> {
                System.out.println("Transfer OUT");

            }
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
    private static void fee_ruleManagementMenu() {
        int choice;
        do {
            System.out.println("\n===== Gestion du fee_rule =====");
            System.out.println("1. Create fee_rule");
            System.out.println("2. update fee_rule");
            System.out.println("3. desactiver fee_rule");
            System.out.println("4. display fee_rule");
            System.out.println("5. Exit");
            choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> {
                    System.out.println("ajouter une fee_rule");
                    addFeeRule();
                }
                case 2 -> {
                    System.out.println("update fee_rule");
                }
                case 3 -> {
                    System.out.println("desactiver fee_rule");
                }
                case 4 -> System.out.println("display fee_rule");
                case 5 -> System.out.println("5. Exit");
                default -> System.out.println("Choix invalide, essayez encore.");
            }
        } while (choice != 5);
        scanner.close();
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

        System.out.print("Veuillez saisir balance\n");
        BigDecimal balance =scanner.nextBigDecimal();
        scanner.nextLine();

        System.out.print("Veuillez saisir Type de l'account (COURANT,EPARGNE,CREDIT)\n");
        String accountInput=scanner.nextLine();

        Account.AccountType typeAcount = Account.AccountType.valueOf(accountInput.toUpperCase());

        String accountNumber=Account.generateAccountNumber();
        Account account=new Account(null,accountNumber,balance,typeAcount,client,true, Instant.now(),Instant.now());

        boolean success=accountService.createAccount(account);

        if(success){
            System.out.println("le compte est ajoutée avec succes");
        }else{
            System.out.println("l'ajout est achouée");
        }


    }

    private static void deposit() {
        System.out.print("Veuillez saisir le CIN du client : ");
        String cin = scanner.nextLine();

        Optional<Client> clientOpt = clientService.findByCin(cin);

        if (clientOpt.isEmpty()) {
            System.out.println("Client introuvable !!");
            return;
        }
        Client client = clientOpt.get();
        List<Account> accounts = accountRepository.findAccountsByClient(client);

        if (accounts.isEmpty()) {
            System.out.println("Ce client n’a aucun compte !");
            return;
        }

        System.out.println("Comptes disponibles : ");
        for (int i = 0; i < accounts.size(); i++) {
            Account acc = accounts.get(i);
            System.out.println(acc);
        }

        System.out.print("Choisissez le numéro du compte : ");
        String numCompte = scanner.nextLine();

        System.out.print("Montant à déposer : ");
        BigDecimal amount = scanner.nextBigDecimal();
        scanner.nextLine();

        boolean success = transactionService.deposit(numCompte, amount);

        if (success) {
            System.out.println("Dépôt effectué avec succès !");
        } else {
            System.out.println("Échec du dépôt.");
        }
    }

    private static void transfererIn(){
        System.out.print("Veuillez saisir le CIN du client : ");
        String cin = scanner.nextLine();

        Optional<Client> clientOpt = clientService.findByCin(cin);

        if (clientOpt.isEmpty()) {
            System.out.println("Client introuvable !!");
            return;
        }
        Client client = clientOpt.get();
        List<Account> accounts = accountRepository.findAccountsByClient(client);

        if (accounts.isEmpty()) {
            System.out.println("Ce client n’a aucun compte !");
            return;
        }

        System.out.println("Comptes disponibles : ");
        for (int i = 0; i < accounts.size(); i++) {
            Account acc = accounts.get(i);
            System.out.println(acc);
        }

        System.out.print("Choisissez le numéro du compte source: ");
        String idCompteSource = scanner.nextLine();

        System.out.print("Choisissez le numéro du compte destination: ");
        String idCompteDestination = scanner.nextLine();

        System.out.print("Montant à déposer : ");
        BigDecimal amount = scanner.nextBigDecimal();
        scanner.nextLine();

        boolean success = transactionService.transfer(idCompteSource,idCompteDestination ,amount,Transaction.TransactionType.TRANSFER_IN);

        if (success) {
            System.out.println("Dépôt effectué avec succès !");
        } else {
            System.out.println("Échec du dépôt.");
        }
    }

    private static void transfererOut(){
        System.out.println("Transfer OUT ");
        System.out.println("veuiller entrer votre cin");
        String cin=scanner.nextLine();
        Optional<Client> clientOpt=clientService.findByCin(cin);
        if (clientOpt.isEmpty()) {
            System.out.println("Client introuvable !!");
            return;
        }
        Client client=clientOpt.get();
        System.out.println("veuiller choisir le compte depuis lequel vous vouler faire virement");
        List<Account> accounts=accountRepository.findAccountsByClient(client);
        for(int i=0;i<accounts.size();i++){
            Account acc=accounts.get(i);
            System.out.println(acc);
        }
        String numCptSource=scanner.nextLine();
        System.out.println("veuiller saisir le numéro du compte du destinataire");
        String numCptDestinataire=scanner.nextLine();
        Optional<Account> idCptDestinataire=accountRepository.findByNumCpt(numCptDestinataire);
        UUID idCptDes=idCptDestinataire.get().getId();
        String idCompteDes=String.valueOf(idCptDes);
        System.out.println("Veuiller saisir l'amount");
        BigDecimal amount=scanner.nextBigDecimal();
        scanner.nextLine();
        boolean success=transactionService.transfer(numCptSource,idCompteDes,amount,Transaction.TransactionType.TRANSFER_OUT);
        if (success) {
            System.out.println("transfer OUT effectué avec succès !");
        } else {
            System.out.println("Échec du transfer.");
        }

    }

    private static void validerTransferOut(){
        System.out.println("Voici les transfer out a valider:");

        List<Transaction> transactions=transactionRepository.findAllTransferOut();

        for(int i=0;i<transactions.size();i++){
            Transaction transaction=transactions.get(i);
            System.out.println(transaction);
        }
        System.out.println("Veuiller saisir l'id du transaction a valider");
        String transactionId=scanner.nextLine();
        boolean success=transactionService.validertransferOut(transactionId);
        if(success){
            System.out.println("la transaction OUT est validée");
        }else{
            System.out.println("Echec!");
        }
    }

    private static void addFeeRule(){
        System.out.println("=== Ajout d'une Fee Rule ===");
        System.out.println("Choisissez le type d'opération (CREDIT / TRANSACTION_EXTERNE) :");
        FeeRule.OperationType operationTypeInput = FeeRule.OperationType.valueOf(scanner.nextLine().toUpperCase());

        System.out.println("Choisissez le mode de frais (FIXE / PERCENT) :");
        FeeRule.FeeMode modeInput = FeeRule.FeeMode.valueOf(scanner.nextLine().toUpperCase());

        System.out.println("Entrer le minimum amount");
        BigDecimal minAmountInput = scanner.nextBigDecimal();
        scanner.nextLine();
        System.out.println("Entrer le maximum amount");
        BigDecimal maxAmountInput = scanner.nextBigDecimal();
        scanner.nextLine();
        System.out.println("Entrer currency");
        String currencyInput = scanner.nextLine();
        System.out.print("Entrez la valeur du frais : ");
        BigDecimal value = scanner.nextBigDecimal();
        scanner.nextLine();

        FeeRule feeRule=new FeeRule(null,operationTypeInput,modeInput,minAmountInput,maxAmountInput,value,Instant.now(),true,currencyInput);
        boolean success=feeRuleService.addFeeRule(feeRule);
        if (success){
            System.out.println("Fee_Rule créé avec succès !");
        }else System.out.println("Erreur lors de la création du Fee_Rule.");
    }

    private static void transferExterne(){
        System.out.print("Veuillez saisir le CIN du client : ");
        String cin = scanner.nextLine();

        Optional<Client> clientOpt = clientService.findByCin(cin);

        if (clientOpt.isEmpty()) {
            System.out.println("Client introuvable !!");
            return;
        }
        Client client = clientOpt.get();
        List<Account> accounts = accountRepository.findAccountsByClient(client);

        if (accounts.isEmpty()) {
            System.out.println("Ce client n’a aucun compte !");
            return;
        }

        System.out.println("Comptes disponibles : ");
        for (int i = 0; i < accounts.size(); i++) {
            Account acc = accounts.get(i);
            System.out.println(acc);
        }

        System.out.print("Choisissez l'id du compte Source: ");
        String idCompteSource = scanner.nextLine();

        System.out.print("Choisissez le numéro du compte destination: ");
        String idCompteDestination = scanner.nextLine();

        System.out.print("Montant à déposer : ");
        BigDecimal amount = scanner.nextBigDecimal();
        scanner.nextLine();

        boolean success = transactionService.transferExterne(idCompteSource,amount,Transaction.TransactionType.ETRANGER);

        if (success) {
            System.out.println("Dépôt effectué avec succès !");
        } else {
            System.out.println("Échec du dépôt.");
        }
    }

    private static void validerTransferEtranger(){
        System.out.println("Voici les transfer out a valider:");

        List<Transaction> transactions=transactionRepository.findAllTransferExterne();

        for(int i=0;i<transactions.size();i++){
            Transaction transaction=transactions.get(i);
            System.out.println(transaction);
        }
        System.out.println("Veuiller saisir l'id du transaction a valider");
        String transactionId=scanner.nextLine();
        boolean success=transactionService.validerTransactionExterne(transactionId);
        if(success){
            System.out.println("la transaction OUT est validée");
        }else{
            System.out.println("Echec!");
        }
    }

    private static void addCredit(){
        System.out.println("=== Création d'un crédit ===");


        System.out.println("Veuillez saisir l'ID du compte client :");
        String accountIdInput = scanner.nextLine();
        Account account = accountRepository.findById(accountIdInput).orElse(null);
        if (account == null) {
            System.out.println("Compte introuvable !");
            return;
        }


        System.out.println("Entrez le montant du crédit :");
        BigDecimal amount = scanner.nextBigDecimal();
        scanner.nextLine();

        Optional<FeeRule> feeRuleOp=feeRoleRepository.findByAmount(amount, FeeRule.OperationType.CREDIT);
        FeeRule feeRule=feeRuleOp.get();


        System.out.println("Entrez la durée du crédit (en mois) :");
        int duree = scanner.nextInt();
        scanner.nextLine();

        Credit credit = new Credit(
                null,
                amount,
                duree,
                Credit.CreditStatus.ACTIVE,
                feeRule,
                account,
                Instant.now(),
                Instant.now()
        );
        boolean success = creditService.addCredit(credit);
        if (success) {
            System.out.println("Crédit créé avec succès !");
        } else {
            System.out.println("Erreur lors de la création du crédit.");
        }


    }


}





