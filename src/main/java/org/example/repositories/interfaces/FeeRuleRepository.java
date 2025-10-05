package org.example.repositories.interfaces;

import org.example.models.Account;
import org.example.models.FeeRule;
import org.example.models.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FeeRuleRepository {

    boolean save(FeeRule feeRule);
    Optional<FeeRule> findByAmount(BigDecimal amount,FeeRule.OperationType operationType);
    Optional<FeeRule> findById(UUID id);

}
