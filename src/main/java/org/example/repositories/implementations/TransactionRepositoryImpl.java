package org.example.repositories.implementations;

import org.example.database.DatabaseConnection;
import org.example.models.Account;
import org.example.models.Transaction;
import org.example.repositories.interfaces.AccountRepository;
import org.example.repositories.interfaces.TransactionRepository;

import java.math.BigDecimal;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
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

    @Override
    public List<Transaction>  findAllTransferOut(){
        List<Transaction> transactions = new ArrayList<>();
        String sql="SELECT * FROM \"Transaction\" WHERE type=? AND status=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, Transaction.TransactionType.TRANSFER_OUT.name(),java.sql.Types.OTHER);
            stmt.setObject(2, Transaction.TransactionStatus.PENDING.name(),java.sql.Types.OTHER);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Transaction transaction = new Transaction(
                            rs.getObject("id", UUID.class),
                            rs.getBigDecimal("amount"),
                            Transaction.TransactionType.valueOf(rs.getString("type")),
                            Transaction.TransactionStatus.valueOf(rs.getString("status")),
                            rs.getTimestamp("created_at").toInstant(),
                            accountRepository.findById(rs.getString("account_id")).orElse(null)
                    );
                    transactions.add(transaction);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactions;


    }

    @Override
    public Optional<Transaction> findById(String transactionId){
        String sql="SELECT * FROM \"Transaction\" WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, UUID.fromString(transactionId)); // si id est bien un UUID

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Transaction transaction = new Transaction(
                            (UUID) rs.getObject("id"),
                            rs.getBigDecimal("amount"),
                            Transaction.TransactionType.valueOf(rs.getString("type")),
                            Transaction.TransactionStatus.valueOf(rs.getString("status")),
                            rs.getTimestamp("created_at").toInstant(),
                            accountRepository.findById(rs.getString("account_id")).orElse(null)
                    );

                    return Optional.of(transaction);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();

    }


    @Override
    public boolean validate(String transactionId){
        String sql = "UPDATE \"Transaction\" SET status = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, Transaction.TransactionStatus.COMPLETED.name(),java.sql.Types.OTHER); // "COMPLETED"
            stmt.setObject(2, UUID.fromString(transactionId)); // si ton id est de type UUID

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



}

