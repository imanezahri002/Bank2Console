package org.example.models;


import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class Credit {

    private UUID id;
    private BigDecimal amount;
    private int duree;
    private CreditStatus status;
    private FeeRule feeRule;
    private Account account;
    private Instant createdAt;
    private Instant updatedAt;


    public enum CreditStatus {
        ACTIVE,
        LATE,
        CLOSED
    }


    public Credit(UUID id, BigDecimal amount, int duree, CreditStatus status,
                  FeeRule feeRule, Account account, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.amount = amount;
        this.duree = duree;
        this.status = status;
        this.feeRule = feeRule;
        this.account = account;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters et setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public CreditStatus getStatus() {
        return status;
    }

    public void setStatus(CreditStatus status) {
        this.status = status;
    }

    public FeeRule getFeeRule() {
        return feeRule;
    }

    public void setFeeRule(FeeRule feeRule) {
        this.feeRule = feeRule;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}

