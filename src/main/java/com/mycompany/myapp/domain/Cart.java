package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Cart.
 */
@Entity
@Table(name = "cart")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Cart implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "added_to_cart_date")
    private LocalDate addedToCartDate;

    @Column(name = "priority")
    private Integer priority;

    @Column(name = "quantity_desired")
    private Integer quantityDesired;

    @Column(name = "last_modified")
    private LocalDate lastModified;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cart id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public Cart userId(Integer userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProductId() {
        return this.productId;
    }

    public Cart productId(Integer productId) {
        this.setProductId(productId);
        return this;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public LocalDate getAddedToCartDate() {
        return this.addedToCartDate;
    }

    public Cart addedToCartDate(LocalDate addedToCartDate) {
        this.setAddedToCartDate(addedToCartDate);
        return this;
    }

    public void setAddedToCartDate(LocalDate addedToCartDate) {
        this.addedToCartDate = addedToCartDate;
    }

    public Integer getPriority() {
        return this.priority;
    }

    public Cart priority(Integer priority) {
        this.setPriority(priority);
        return this;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getQuantityDesired() {
        return this.quantityDesired;
    }

    public Cart quantityDesired(Integer quantityDesired) {
        this.setQuantityDesired(quantityDesired);
        return this;
    }

    public void setQuantityDesired(Integer quantityDesired) {
        this.quantityDesired = quantityDesired;
    }

    public LocalDate getLastModified() {
        return this.lastModified;
    }

    public Cart lastModified(LocalDate lastModified) {
        this.setLastModified(lastModified);
        return this;
    }

    public void setLastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cart)) {
            return false;
        }
        return getId() != null && getId().equals(((Cart) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cart{" +
            "id=" + getId() +
            ", userId=" + getUserId() +
            ", productId=" + getProductId() +
            ", addedToCartDate='" + getAddedToCartDate() + "'" +
            ", priority=" + getPriority() +
            ", quantityDesired=" + getQuantityDesired() +
            ", lastModified='" + getLastModified() + "'" +
            "}";
    }
}
