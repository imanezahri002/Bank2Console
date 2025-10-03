package org.example.services;

import org.example.models.Account;
import org.example.models.Transaction;
import org.example.repositories.implementations.TransactionRepositoryImpl;
import org.example.repositories.interfaces.AccountRepository;
import org.example.repositories.interfaces.TransactionRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public boolean deposit(String accountId, BigDecimal amount) {
        Optional<Account> optAccount = accountRepository.findById(accountId);

        if (optAccount.isEmpty()) {
            System.out.println("Compte introuvable !");
            return false;
        }

        Account account = optAccount.get();
        account.setBalance(account.getBalance().add(amount));
        Transaction transaction=new Transaction(null,
                amount,
                Transaction.TransactionType.DEPOSIT,
                Transaction.TransactionStatus.COMPLETED,
                Instant.now(),
                optAccount.get());

        transactionRepository.save(transaction);
        return accountRepository.update(account);
    }

    public boolean transfer(String idCompteSource,String idCompteDestination ,BigDecimal amount,Transaction.TransactionType type) {
        Optional<Account> accountSource = accountRepository.findById(idCompteSource);
        Optional<Account> accountDestination = accountRepository.findById(idCompteDestination);

        if (accountSource.isEmpty() || accountDestination.isEmpty()) {
            System.out.println("Compte introuvable !");
            return false;
        }


        Account accountS = accountSource.get();
        Account accountD = accountDestination.get();

        if (accountS.getBalance().compareTo(amount) < 0) {
            System.out.println("Fonds insuffisants sur le compte source !");
            return false;
        }
        Transaction.TransactionStatus status;

        if (type == Transaction.TransactionType.TRANSFER_OUT) {
            status = Transaction.TransactionStatus.PENDING;
        } else {
            status = Transaction.TransactionStatus.COMPLETED;
        }

        Transaction transaction=new Transaction(null,
                amount,
                type,
                status,
                Instant.now(),
                accountS);

        transactionRepository.save(transaction);

        return transactionRepository.transactionTransferIn(accountS, accountD, amount);
    }

//    public boolean transferOut(String nrCptExterne,String numCptClient,BigDecimal amount) {
//        Optional<Account> accountDestination = accountRepository.findByNumCpt(nrCptExterne);
//        Optional<Account> accountSource = accountRepository.findById(numCptClient);
//
//        if (accountSource.isEmpty() || accountDestination.isEmpty()) {
//            System.out.println("Compte introuvable !");
//            return false;
//        }
//
//
//        Account accountS = accountSource.get();
//        Account accountD = accountDestination.get();
//
//        if (accountS.getBalance().compareTo(amount) < 0) {
//            System.out.println("Fonds insuffisants sur le compte source !");
//            return false;
//        }
//        Transaction transaction = new Transaction(null,
//                amount,
//                Transaction.TransactionType.TRANSFER_OUT,
//                Transaction.TransactionStatus.PENDING,
//                Instant.now(),
//                accountS);
//
//        transactionRepository.save(transaction);
//        return true;
//
//    }

    public boolean validertransferOut(String transactionId){
        Optional<Transaction> transaction =transactionRepository.findById(transactionId);
        if ( transaction.isEmpty()) {
            System.out.println("Transaction introuvable !");
            return false;
        }
        return transactionRepository.validate(transactionId);
    }
}
