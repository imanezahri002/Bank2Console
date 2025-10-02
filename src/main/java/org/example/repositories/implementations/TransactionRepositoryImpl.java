package org.example.repositories.implementations;

import org.example.database.DatabaseConnection;
import org.example.models.Account;
import org.example.models.Transaction;
import org.example.repositories.interfaces.AccountRepository;
import org.example.repositories.interfaces.TransactionRepository;

import java.math.BigDecimal;
import java.sql.*;
import java.util.UUID;

public class TransactionRepositoryImpl implements TransactionRepository {

    private final Connection connection;
    private final AccountRepository accountRepository;

    public TransactionRepositoryImpl(AccountRepository accountRepository) {
        this.connection = DatabaseConnection.getInstance();
        this.accountRepository=accountRepository;
    }

    @Override
    public boolean save(Transaction transaction) {
        String sql = "INSERT INTO \"Transaction\" (amount, type, status, created_at, account_id) " +
                "VALUES ( ?, ?, ?, ?, ?)";

        try (
             PreparedStatement stmt = connection.prepareStatement(sql)) {


            stmt.setBigDecimal(1, transaction.getAmount());
            stmt.setObject(2, transaction.getType().name(), java.sql.Types.OTHER);
            stmt.setObject(3, transaction.getStatus().name(),java.sql.Types.OTHER);
            stmt.setTimestamp(4, Timestamp.from(transaction.getCreated_at()));
            stmt.setObject(5, transaction.getAccount().getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean transactionTransferIn(Account accountS, Account accountD, BigDecimal amount){
        try {
            connection.setAutoCommit(false);

            accountS.setBalance(accountS.getBalance().subtract(amount));
            accountD.setBalance(accountD.getBalance().add(amount));

            boolean updateS = accountRepository.update(accountS);
            boolean updateD = accountRepository.update(accountD);

            if (updateS && updateD) {
                connection.commit();
                return true;
            } else {
                connection.rollback();
                return false;
            }

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }




}

