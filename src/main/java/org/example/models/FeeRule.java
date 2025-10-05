package org.example.models;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class FeeRule {
    private UUID id;
    private OperationType operationType;
    private FeeMode mode;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private BigDecimal feeValue;
    private Instant createdAt;
    private boolean is_active;
    private String currency;


    public FeeRule(UUID id, OperationType operationType, FeeMode mode, BigDecimal minAmount, BigDecimal maxAmount, BigDecimal feeValue, Instant createdAt,boolean is_active,String currency) {
        this.id = id;
        this.operationType = operationType;
        this.mode = mode;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.feeValue = feeValue;
        this.createdAt = createdAt;
        this.is_active=is_active;
        this.currency=currency;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public enum OperationType {
        CREDIT,
        TRANSACTION_EXTERNE
    }
    public enum FeeMode {
        FIX,
        PERCENT
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public FeeMode getMode() {
        return mode;
    }

    public void setMode(FeeMode mode) {
        this.mode = mode;
    }

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public BigDecimal getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(BigDecimal maxAmount) {
        this.maxAmount = maxAmount;
    }

    public BigDecimal getFeeValue() {
        return feeValue;
    }

    public void setFeeValue(BigDecimal feeValue) {
        this.feeValue = feeValue;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "FeeRule{" +
                "id=" + id +
                ", operationType=" + operationType +
                ", mode=" + mode +
                ", minAmount=" + minAmount +
                ", maxAmount=" + maxAmount +
                ", feeValue=" + feeValue +
                ", createdAt=" + createdAt +
                '}';
    }
}
