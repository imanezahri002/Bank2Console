package org.example.repositories.implementations;

import org.example.database.DatabaseConnection;
import org.example.models.FeeRule;
import org.example.repositories.interfaces.FeeRuleRepository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class FeeRuleRepositoryImpl implements FeeRuleRepository {
    private final Connection connection;


    public FeeRuleRepositoryImpl(){
        this.connection= DatabaseConnection.getInstance();
    }
    @Override
    public boolean save(FeeRule feeRule){
    String sql="INSERT INTO fee_rule (operation_type,mode,max_amount,min_amount,currency,fee_value)"+
            "VALUES (?,?,?,?,?,?)";
    try(PreparedStatement stmt = connection.prepareStatement(sql)) {

        stmt.setObject(1, feeRule.getOperationType().name(), java.sql.Types.OTHER);
        stmt.setObject(2, feeRule.getMode().name(), java.sql.Types.OTHER);
        stmt.setBigDecimal(3, feeRule.getMaxAmount());
        stmt.setBigDecimal(4, feeRule.getMinAmount());
        stmt.setString(5, feeRule.getCurrency());
        stmt.setBigDecimal(6, feeRule.getFeeValue());
        return stmt.executeUpdate() > 0;
    }catch(SQLException e){
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public Optional<FeeRule>findByAmount(BigDecimal amount,FeeRule.OperationType operationType){
        String sql="SELECT * FROM fee_rule WHERE ? BETWEEN min_amount AND max_amount AND operation_type = ? AND is_active = true";
        try(PreparedStatement stmt =connection.prepareStatement(sql)){
            stmt.setBigDecimal(1, amount);
            stmt.setObject(2, operationType.name(), java.sql.Types.OTHER);


            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                FeeRule feeRule = new FeeRule(
                        UUID.fromString(rs.getString("id")),
                        FeeRule.OperationType.valueOf(rs.getString("operation_type")),
                        FeeRule.FeeMode.valueOf(rs.getString("mode")),
                        rs.getBigDecimal("min_amount"),
                        rs.getBigDecimal("max_amount"),
                        rs.getBigDecimal("fee_value"),
                        rs.getTimestamp("created_at").toInstant(),
                        rs.getBoolean("is_active"),
                        rs.getString("currency")
                );
                return Optional.of(feeRule);
            }
            }catch(SQLException e){
                e.printStackTrace();
            }
            return Optional.empty();

    }

    @Override
    public Optional<FeeRule> findById(UUID id) {
        String sql = "SELECT * FROM fee_rule WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                FeeRule feeRule = new FeeRule(
                        rs.getObject("id", UUID.class),
                        FeeRule.OperationType.valueOf(rs.getString("operation_type")),
                        FeeRule.FeeMode.valueOf(rs.getString("mode")),
                        rs.getBigDecimal("min_amount"),
                        rs.getBigDecimal("max_amount"),
                        rs.getBigDecimal("fee_value"),
                        rs.getTimestamp("created_at").toInstant(),
                        rs.getBoolean("is_active"),
                        rs.getString("currency")
                );
                return Optional.of(feeRule);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }



}
