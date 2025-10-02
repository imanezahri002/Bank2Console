package org.example.repositories.implementations;

import org.example.database.DatabaseConnection;
import org.example.models.Account;
import org.example.models.Client;
import org.example.repositories.interfaces.AccountRepository;

import java.math.BigDecimal;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;



public class AccountRepositoryImpl implements AccountRepository {

    private final Connection connection;

    public AccountRepositoryImpl() {
        this.connection = DatabaseConnection.getInstance();
    }

    @Override
    public boolean create(Account account) {
        String sql = "INSERT INTO \"Account\" (accountnumber, balance, type, client_id, is_active, created_at, updated_at) " +
                "VALUES ( ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {


            stmt.setString(1, account.getAccountNumber());
            stmt.setBigDecimal(2, account.getBalance());
            stmt.setObject(3, account.getType().name(), java.sql.Types.OTHER);
            stmt.setObject(4, account.getClient().getId());
            stmt.setBoolean(5, account.isActive());
            stmt.setTimestamp(6, Timestamp.from(account.getCreatedAt()));
            stmt.setTimestamp(7, Timestamp.from(account.getUpdatedAt()));

            int rowsInserted=stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Account> findAccountsByClient(Client client) {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT id, accountnumber, balance, type, client_id, is_active, created_at, updated_at " +
                "FROM \"Account\" WHERE client_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, client.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Account account = new Account(
                        rs.getObject("id", UUID.class),
                        rs.getString("accountnumber"),
                        rs.getBigDecimal("balance"),
                        Account.AccountType.valueOf(rs.getString("type")),
                        client,
                        rs.getBoolean("is_active"),
                        rs.getTimestamp("created_at").toInstant(),
                        rs.getTimestamp("updated_at").toInstant()
                );
                accounts.add(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accounts;
    }

    @Override
    public boolean update(Account account) {
        String sql = "UPDATE \"Account\" SET " +
                "balance = ?, " +
                "type = ?, " +
                "client_id = ? " +
                "WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setBigDecimal(1, account.getBalance());
            stmt.setObject(2, account.getType().name(), java.sql.Types.OTHER);
            stmt.setObject(3, account.getClient().getId());
            stmt.setObject(4, account.getId());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public Optional<Account> findById(String accountId){
        String sql = "SELECT a.*, c.id AS client_id, c.firstname, c.lastname, c.email, c.cin, c.phonenumber, c.address, c.salaire " +
                "FROM \"Account\" a " +
                "JOIN \"Client\" c ON a.client_id = c.id " +
                "WHERE a.id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, UUID.fromString(accountId));
            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    Client client = new Client(
                            rs.getObject("client_id", UUID.class),
                            rs.getString("firstname"),
                            rs.getString("lastname"),
                            rs.getString("cin"),
                            rs.getString("phonenumber"),
                            rs.getString("address"),
                            rs.getString("email"),
                            rs.getDouble("salaire")
                    );

                    Account account = new Account(
                            rs.getObject("id", UUID.class),
                            rs.getString("accountnumber"),
                            rs.getBigDecimal("balance"),
                            Account.AccountType.valueOf(rs.getString("type")),
                            client,
                            rs.getBoolean("is_active"),
                            rs.getTimestamp("created_at").toInstant(),
                            rs.getTimestamp("updated_at").toInstant()
                    );
                    return Optional.of(account);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
    @Override
    public Optional<Account> findByNumCpt(String numAccount){
        String sql = "SELECT a.*, c.id AS client_id, c.firstname, c.lastname, c.email, c.cin, c.phonenumber, c.address, c.salaire " +
                "FROM \"Account\" a " +
                "JOIN \"Client\" c ON a.client_id = c.id " +
                "WHERE a.accountnumber = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, numAccount);
            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    Client client = new Client(
                            rs.getObject("client_id", UUID.class),
                            rs.getString("firstname"),
                            rs.getString("lastname"),
                            rs.getString("cin"),
                            rs.getString("phonenumber"),
                            rs.getString("address"),
                            rs.getString("email"),
                            rs.getDouble("salaire")
                    );

                    Account account = new Account(
                            rs.getObject("id", UUID.class),
                            rs.getString("accountnumber"),
                            rs.getBigDecimal("balance"),
                            Account.AccountType.valueOf(rs.getString("type")),
                            client,
                            rs.getBoolean("is_active"),
                            rs.getTimestamp("created_at").toInstant(),
                            rs.getTimestamp("updated_at").toInstant()
                    );
                    return Optional.of(account);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }



}
