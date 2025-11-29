package ru.rent.demo.entity;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "rental_contracts")
public class RentalContract {
    @Id
    private String id;

    private String idBooking;
    private String idPayment;
    private String idRefund;
    private Long ownerId;
    private Long customerId;

    public RentalContract () { }

    public RentalContract(String idBooking, String idPayment, String idRefund, Long ownerId, Long customerId) {
        this.idBooking = idBooking;
        this.idPayment = idPayment;
        this.idRefund = idRefund;
        this.ownerId = ownerId;
        this.customerId = customerId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdBooking() {
        return idBooking;
    }

    public void setIdBooking(String idBooking) {
        this.idBooking = idBooking;
    }

    public String getIdPayment() {
        return idPayment;
    }

    public void setIdPayment(String idPayment) {
        this.idPayment = idPayment;
    }

    public String getIdRefund() {
        return idRefund;
    }

    public void setIdRefund(String idRefund) {
        this.idRefund = idRefund;
    }
}