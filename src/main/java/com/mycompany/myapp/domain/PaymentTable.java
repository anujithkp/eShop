package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A PaymentTable.
 */
@Entity
@Table(name = "payment_table")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentTable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "order_id")
    private Integer orderId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "payment_method")
    private Integer paymentMethod;

    @Column(name = "transaction_amount")
    private Double transactionAmount;

    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "billing_address")
    private String billingAddress;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PaymentTable id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return this.orderId;
    }

    public PaymentTable orderId(Integer orderId) {
        this.setOrderId(orderId);
        return this;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public PaymentTable userId(Integer userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public LocalDate getPaymentDate() {
        return this.paymentDate;
    }

    public PaymentTable paymentDate(LocalDate paymentDate) {
        this.setPaymentDate(paymentDate);
        return this;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Integer getPaymentMethod() {
        return this.paymentMethod;
    }

    public PaymentTable paymentMethod(Integer updatedPaymentMethod) {
        this.setPaymentMethod(updatedPaymentMethod);
        return this;
    }

    public void setPaymentMethod(Integer updatedPaymentMethod) {
        this.paymentMethod = updatedPaymentMethod;
    }

    public Double getTransactionAmount() {
        return this.transactionAmount;
    }

    public PaymentTable transactionAmount(Double transactionAmount) {
        this.setTransactionAmount(transactionAmount);
        return this;
    }

    public void setTransactionAmount(Double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getPaymentStatus() {
        return this.paymentStatus;
    }

    public PaymentTable paymentStatus(String paymentStatus) {
        this.setPaymentStatus(paymentStatus);
        return this;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getBillingAddress() {
        return this.billingAddress;
    }

    public PaymentTable billingAddress(String billingAddress) {
        this.setBillingAddress(billingAddress);
        return this;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentTable)) {
            return false;
        }
        return getId() != null && getId().equals(((PaymentTable) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentTable{" +
            "id=" + getId() +
            ", orderId=" + getOrderId() +
            ", userId=" + getUserId() +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", paymentMethod=" + getPaymentMethod() +
            ", transactionAmount=" + getTransactionAmount() +
            ", paymentStatus='" + getPaymentStatus() + "'" +
            ", billingAddress='" + getBillingAddress() + "'" +
            "}";
    }
}
