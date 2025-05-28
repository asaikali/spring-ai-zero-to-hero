// Payment.java
package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class Payment {
    @JsonProperty("payment_method")
    private String paymentMethod;
    private String provider;

    @JsonProperty("card_last4")
    private String cardLast4;

    @JsonProperty("payment_method_display")
    private String paymentMethodDisplay;

    @JsonProperty("billing_name")
    private String billingName;

    @JsonProperty("transaction_id")
    private String transactionId;

    private String status;

    @JsonProperty("paid_at")
    private LocalDateTime paidAt;

    @JsonProperty("refunded_at")
    private LocalDateTime refundedAt;

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public Payment setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public String getProvider() {
        return provider;
    }

    public Payment setProvider(String provider) {
        this.provider = provider;
        return this;
    }

    public String getCardLast4() {
        return cardLast4;
    }

    public Payment setCardLast4(String cardLast4) {
        this.cardLast4 = cardLast4;
        return this;
    }

    public String getPaymentMethodDisplay() {
        return paymentMethodDisplay;
    }

    public Payment setPaymentMethodDisplay(String paymentMethodDisplay) {
        this.paymentMethodDisplay = paymentMethodDisplay;
        return this;
    }

    public String getBillingName() {
        return billingName;
    }

    public Payment setBillingName(String billingName) {
        this.billingName = billingName;
        return this;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public Payment setTransactionId(String transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public Payment setStatus(String status) {
        this.status = status;
        return this;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public Payment setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
        return this;
    }

    public LocalDateTime getRefundedAt() {
        return refundedAt;
    }

    public Payment setRefundedAt(LocalDateTime refundedAt) {
        this.refundedAt = refundedAt;
        return this;
    }
}
