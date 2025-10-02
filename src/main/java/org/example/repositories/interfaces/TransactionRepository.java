package org.example.repositories.interfaces;

import org.example.models.Account;
import org.example.models.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface TransactionRepository {

    boolean save(Transaction transaction);

    boolean transactionTransferIn(Account accountS, Account accountD, BigDecimal amount);


//    Transaction findById(UUID id);
//    List<Transaction> findAll();
}
