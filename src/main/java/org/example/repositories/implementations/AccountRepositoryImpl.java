package org.example.repositories.implementations;

import org.example.database.DatabaseConnection;
import org.example.models.Account;
import org.example.repositories.interfaces.AccountRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

public class AccountRepositoryImpl implements AccountRepository {

    private final Connection connection;

    public AccountRepositoryImpl() {
        this.connection = DatabaseConnection.getInstance();
    }

    @Override
    public boolean create(Account account) {
        String sql = "INSERT INTO \"Account\" (id, accountnumber, balance, type, client_id, is_active, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {


            stmt.setObject(1, account.getId());
            stmt.setString(2, account.getAccountNumber());
            stmt.setBigDecimal(3, account.getBalance());
            stmt.setString(4, account.getType().name());
            stmt.setObject(5, account.getClient().getId());
            stmt.setBoolean(6, account.isActive());
            stmt.setTimestamp(7, Timestamp.from(account.getCreatedAt()));
            stmt.setTimestamp(8, Timestamp.from(account.getUpdatedAt()));

            int rowsInserted=stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
