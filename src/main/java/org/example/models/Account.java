package org.example.models;
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

    enum AccountType{COURANT,EPARGNE,CREDIT};

    public Account(UUID id, String accountNumber, BigDecimal balance,AccountType type, Client client) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.type = type;
        this.client = client;
    }



}
