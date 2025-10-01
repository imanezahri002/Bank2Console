package org.example.repositories.interfaces;

import org.example.models.Account;
import org.example.models.Client;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {

    boolean create(Account account);
    List<Account> findAccountsByClient(Client client);
    boolean update(Account account);
    Optional<Account> findById(String accountId);



}
