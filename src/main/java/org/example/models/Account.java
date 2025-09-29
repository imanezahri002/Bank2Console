package org.example.models;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;


public class Account {

    private UUID id;
    private BigDecimal solde;
    private boolean is_active;
    private Instant created_at;
    private Instant updated_at;
    public AccountType type;
    private Client client;

    enum AccountType{COURANT,EPARGNE,CREDIT};

    public Account(BigDecimal solde,Client client,AccountType type){


    }



}
