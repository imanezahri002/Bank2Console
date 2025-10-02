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

    public boolean transferIn(String numCompteSource,String numCompteDestination ,BigDecimal amount) {
        Optional<Account> accountSource = accountRepository.findById(numCompteSource);
        Optional<Account> accountDestination = accountRepository.findById(numCompteDestination);

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

        accountD.setBalance(accountD.getBalance().add(amount));
        accountS.setBalance(accountS.getBalance().subtract(amount));
        accountRepository.update(accountD);
        accountRepository.update(accountS);
        return true;


    }

}
