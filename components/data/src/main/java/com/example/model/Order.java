// Order.java
package com.example.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Order {
    private int id;

    @JsonProperty("customer_id")
    private int customerId;

    @JsonProperty("order_date")
    private LocalDateTime orderDate;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    private String status;

    @JsonProperty("status_history")
    private List<StatusHistory> statusHistory;

    @JsonProperty("line_items")
    private List<LineItem> lineItems;

    private BigDecimal subtotal;
    private BigDecimal tax;

    @JsonProperty("total_price")
    private BigDecimal totalPrice;

    private Payment payment;
    private Fulfillment fulfillment;

    @JsonProperty("shipping_address")
    private Address shippingAddress;

    @JsonProperty("billing_address")
    private Address billingAddress;

    @JsonProperty("refund_status")
    private String refundStatus;

    @JsonProperty("refunded_amount")
    private BigDecimal refundedAmount;

    @JsonProperty("cancellation_reason")
    private String cancellationReason;

    @JsonProperty("cancelled_at")
    private LocalDateTime cancelledAt;

    @JsonProperty("return_eligible_until")
    private LocalDateTime returnEligibleUntil;

    @JsonProperty("support_notes")
    private String supportNotes;

    public int getId() {
        return id;
    }

    public Order setId(int id) {
        this.id = id;
        return this;
    }

    public int getCustomerId() {
        return customerId;
    }

    public Order setCustomerId(int customerId) {
        this.customerId = customerId;
        return this;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public Order setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Order setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Order setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public Order setStatus(String status) {
        this.status = status;
        return this;
    }

    public List<StatusHistory> getStatusHistory() {
        return statusHistory;
    }

    public Order setStatusHistory(List<StatusHistory> statusHistory) {
        this.statusHistory = statusHistory;
        return this;
    }

    public List<LineItem> getLineItems() {
        return lineItems;
    }

    public Order setLineItems(List<LineItem> lineItems) {
        this.lineItems = lineItems;
        return this;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public Order setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
        return this;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public Order setTax(BigDecimal tax) {
        this.tax = tax;
        return this;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public Order setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public Payment getPayment() {
        return payment;
    }

    public Order setPayment(Payment payment) {
        this.payment = payment;
        return this;
    }

    public Fulfillment getFulfillment() {
        return fulfillment;
    }

    public Order setFulfillment(Fulfillment fulfillment) {
        this.fulfillment = fulfillment;
        return this;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public Order setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
        return this;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public Order setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
        return this;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public Order setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
        return this;
    }

    public BigDecimal getRefundedAmount() {
        return refundedAmount;
    }

    public Order setRefundedAmount(BigDecimal refundedAmount) {
        this.refundedAmount = refundedAmount;
        return this;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public Order setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
        return this;
    }

    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }

    public Order setCancelledAt(LocalDateTime cancelledAt) {
        this.cancelledAt = cancelledAt;
        return this;
    }

    public LocalDateTime getReturnEligibleUntil() {
        return returnEligibleUntil;
    }

    public Order setReturnEligibleUntil(LocalDateTime returnEligibleUntil) {
        this.returnEligibleUntil = returnEligibleUntil;
        return this;
    }

    public String getSupportNotes() {
        return supportNotes;
    }

    public Order setSupportNotes(String supportNotes) {
        this.supportNotes = supportNotes;
        return this;
    }
}
