package org.example.models;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;


public class Transaction {
    private UUID id;
    private BigDecimal amount;
    private TransactionType type;
    private TransactionStatus status;
    private Instant created_at;
    private Account account;


    public enum TransactionType {
        DEPOSIT,
        WIDTHDRAW,
        TRANSFER_IN,
        TRANSFER_OUT,
        ETRANGER
    }


    public enum TransactionStatus {
        PENDING,
        COMPLETED,
        FAILED
    }

    public Transaction(UUID id, BigDecimal amount, TransactionType type, TransactionStatus status, Instant created_at, Account account) {
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.status = status;
        this.created_at = created_at;
        this.account = account;
    }

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

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public Instant getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Instant created_at) {
        this.created_at = created_at;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

}

