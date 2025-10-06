package org.example.repositories.implementations;

import org.example.database.DatabaseConnection;
import org.example.models.Credit;
import org.example.models.FeeRule;
import org.example.repositories.interfaces.AccountRepository;
import org.example.repositories.interfaces.CreditRepository;
import org.example.repositories.interfaces.FeeRuleRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

public class CreditRepositoryImpl implements CreditRepository {
    private final Connection connection;
    private final AccountRepository accountRepository;
    private final FeeRuleRepository feeRuleRepository;

    public CreditRepositoryImpl(AccountRepository accountRepository, FeeRuleRepository feeRuleRepository){
        this.connection= DatabaseConnection.getInstance();
        this.accountRepository = accountRepository;
        this.feeRuleRepository = feeRuleRepository;
    }

    @Override

    public boolean save(Credit credit){
        String sql = """
                INSERT INTO credit (id, amount, duree, status, fee_rule_id, account_id, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, UUID.randomUUID());
            stmt.setBigDecimal(2, credit.getAmount());
            stmt.setInt(3, credit.getDuree());
            stmt.setString(4, credit.getStatus().name());
            stmt.setObject(5, credit.getFeeRule() != null ? credit.getFeeRule().getId() : null);
            stmt.setObject(6, credit.getAccount().getId());
            stmt.setTimestamp(7, Timestamp.from(credit.getCreatedAt()));
            stmt.setTimestamp(8, Timestamp.from(credit.getUpdatedAt()));

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }




}
