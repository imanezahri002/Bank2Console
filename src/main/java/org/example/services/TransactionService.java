package org.example.services;

import org.example.models.Account;
import org.example.models.Transaction;
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

        return accountRepository.update(account);
    }
}
