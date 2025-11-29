package ru.rent.demo.entity;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "payment_contracts")
public class PaymentContract {

    @Id
    private String id;
    private String bookingId;
    private BigDecimal fullPrice;
    private Long customerId;
    private Long ownerId;

    public PaymentContract() {}

    public PaymentContract(String bookingId, BigDecimal fullPrice, Long customerId, Long ownerId) {
        this.bookingId = bookingId;
        this.fullPrice = fullPrice;
        this.customerId = customerId;
        this.ownerId = ownerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public BigDecimal getFullPrice() {
        return fullPrice;
    }

    public void setFullPrice(BigDecimal fullPrice) {
        this.fullPrice = fullPrice;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
}
