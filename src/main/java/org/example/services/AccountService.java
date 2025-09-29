package org.example.services;

import org.example.models.Account;
import org.example.repositories.interfaces.AccountRepository;

public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public boolean createAccount(Account account) {
        return accountRepository.create(account);
    }
}
