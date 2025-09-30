package org.example.repositories.interfaces;

import org.example.models.Account;
import org.example.models.Client;

import java.util.List;

public interface AccountRepository {

    boolean create(Account account);
    List<Account> findAccountsByClient(Client client);

}
