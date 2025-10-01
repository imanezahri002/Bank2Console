package org.example.repositories.implementations;

import org.example.database.DatabaseConnection;
import org.example.models.Transaction;
import org.example.repositories.interfaces.TransactionRepository;

import java.sql.*;
import java.util.UUID;

public class TransactionRepositoryImpl implements TransactionRepository {

    private final Connection connection;

    public TransactionRepositoryImpl() {
        this.connection = DatabaseConnection.getInstance();
    }

    @Override
    public boolean save(Transaction transaction) {
        String sql = "INSERT INTO \"Transaction\" (id, amount, type, status, created_at, account_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setObject(1, transaction.getId());
            stmt.setBigDecimal(2, transaction.getAmount());
            stmt.setString(3, transaction.getType().name());
            stmt.setString(4, transaction.getStatus().name());
            stmt.setTimestamp(5, Timestamp.from(transaction.getCreated_at()));
            stmt.setObject(6, transaction.getAccount().getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'enregistrement de la transaction : " + e.getMessage());
            return false;
        }
    }
}

