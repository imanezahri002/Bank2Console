package org.example.models;
import java.awt.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;


public class Account {

    private UUID id;
    private String accountNumber;
    private BigDecimal balance;
    private boolean is_active;
    private Instant created_at;
    private Instant updated_at;
    public AccountType type;
    private Client client;

    public enum AccountType{COURANT,EPARGNE,CREDIT};

    public Account(UUID id, String accountNumber, BigDecimal balance,AccountType type, Client client,boolean isActive,
                   Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.type = type;
        this.client = client;
        this.is_active=true;
        this.created_at=Instant.now();
        this.updated_at=Instant.now();

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public Client getClient() {
        return client;
    }

    public void setClientId(Client client) {
        this.client = client;
    }

    public boolean isActive() { return is_active; }
    public void setActive(boolean active) { this.is_active = active; }

    public Instant getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(Instant createdAt) {
        this.created_at = createdAt;
    }

    public Instant getUpdatedAt() {
        return updated_at;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updated_at = updatedAt;
    }



}
