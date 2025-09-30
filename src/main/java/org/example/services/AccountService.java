package org.example.services;

import org.example.models.Account;
import org.example.repositories.interfaces.AccountRepository;

import java.util.List;

public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public boolean createAccount(Account account) {

        List<Account> accounts = accountRepository.findAccountsByClient(account.getClient());

        boolean sameTypeExists = accounts.stream()
                .anyMatch(acc -> acc.getType().equals(account.getType()));

        if (sameTypeExists) {
            System.out.println("⚠️ Ce client a déjà un compte du type " + account.getType());
            return false;
        }

        if (accounts.size() >= 3) {
            System.out.println("⚠️ Ce client a déjà 3 comptes (COURANT, EPARGNE, CREDIT)");
            return false;
        }

        return accountRepository.create(account);
    }

}
