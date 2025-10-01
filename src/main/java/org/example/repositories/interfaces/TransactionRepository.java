package org.example.repositories.interfaces;

import org.example.models.Transaction;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository {

    boolean save(Transaction transaction);  // Pour insérer une transaction
//    Transaction findById(UUID id);          // Pour chercher une transaction
//    List<Transaction> findAll();
}
