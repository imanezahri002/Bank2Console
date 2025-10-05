package org.example.repositories.interfaces;

import org.example.models.Account;
import org.example.models.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository {

    boolean save(Transaction transaction);
    boolean transactionTransferIn(Account accountS, Account accountD, BigDecimal amount);
    List<Transaction> findAllTransferOut();
    Optional<Transaction> findById(String transactionId);
    boolean validate(String transactionId);
    List<Transaction>findAllTransferExterne();
}
