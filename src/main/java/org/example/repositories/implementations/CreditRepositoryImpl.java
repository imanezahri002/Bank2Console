package org.example.repositories.implementations;

import org.example.database.DatabaseConnection;
import org.example.models.Account;
import org.example.models.Credit;
import org.example.models.FeeRule;
import org.example.repositories.interfaces.AccountRepository;
import org.example.repositories.interfaces.CreditRepository;
import org.example.repositories.interfaces.FeeRuleRepository;

import java.math.BigDecimal;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
                INSERT INTO credit (id, amount, duree, status, fee_rule_id, account_id, created_at, updated_at,mensualite)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?,?)
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, UUID.randomUUID());
            stmt.setBigDecimal(2, credit.getAmount());
            stmt.setInt(3, credit.getDuree());
            stmt.setObject(4, credit.getStatus().name(),java.sql.Types.OTHER);
            stmt.setObject(5, credit.getFeeRule() != null ? credit.getFeeRule().getId() : null);
            stmt.setObject(6, credit.getAccount().getId());
            stmt.setTimestamp(7, Timestamp.from(credit.getCreatedAt()));
            stmt.setTimestamp(8, Timestamp.from(credit.getUpdatedAt()));
            stmt.setBigDecimal(9,credit.getMensualite());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Credit> findAllPendingCredits() {
        List<Credit> credits = new ArrayList<>();
        String sql = "SELECT * FROM credit WHERE status = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, Credit.CreditStatus.PENDING.name(), java.sql.Types.OTHER);

            try (var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Credit credit = new Credit(
                            rs.getObject("id", UUID.class),
                            rs.getBigDecimal("amount"),
                            rs.getInt("duree"),
                            Credit.CreditStatus.valueOf(rs.getString("status")),
                            feeRuleRepository.findById(rs.getObject("fee_rule_id", UUID.class)).orElse(null),
                            accountRepository.findById(rs.getObject("account_id").toString()).orElse(null),
                            rs.getTimestamp("created_at").toInstant(),
                            rs.getTimestamp("updated_at").toInstant(),
                            rs.getBigDecimal("mensualite")
                    );
                    credits.add(credit);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return credits;
    }

    @Override
    public Optional<Credit> findById(UUID id) {
        String sql = "SELECT * FROM credit WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, id, java.sql.Types.OTHER);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Credit credit = new Credit(
                            rs.getObject("id", UUID.class),
                            rs.getBigDecimal("amount"),
                            rs.getInt("duree"),
                            Credit.CreditStatus.valueOf(rs.getString("status")),
                            feeRuleRepository.findById(rs.getObject("fee_rule_id", UUID.class)).orElse(null),
                            accountRepository.findById(rs.getObject("account_id").toString()).orElse(null),
                            rs.getTimestamp("created_at").toInstant(),
                            rs.getTimestamp("updated_at").toInstant(),
                            rs.getBigDecimal("mensualite")
                    );
                    return Optional.of(credit);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public boolean validate(UUID creditId){
        String sql="UPDATE credit SET status=? WHERE id=?";
        try(PreparedStatement stmt=connection.prepareStatement(sql)){
            stmt.setObject(1, Credit.CreditStatus.ACTIVE.name(), java.sql.Types.OTHER);
            stmt.setObject(2,creditId);

            int rowsUpdated=stmt.executeUpdate();
            return rowsUpdated>0;

        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }





}
