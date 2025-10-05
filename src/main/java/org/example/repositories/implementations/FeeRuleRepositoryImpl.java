package org.example.repositories.implementations;

import org.example.database.DatabaseConnection;
import org.example.models.FeeRule;
import org.example.repositories.interfaces.FeeRuleRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FeeRuleRepositoryImpl implements FeeRuleRepository {
    private final Connection connection;


    public FeeRuleRepositoryImpl(){
        this.connection= DatabaseConnection.getInstance();
    }
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

}
