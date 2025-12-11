package ru.rent.demo.dto.rental;

import ru.rent.demo.entity.RentalContract;

public class RentalContractDto {

    private String id;
    private String idBooking;
    private String idPayment;
    private String idRefund;
    private Long ownerId;
    private Long customerId;

    public RentalContractDto() {
    }

    public RentalContractDto(String id, String idBooking, String idPayment, String idRefund, Long ownerId, Long customerId) {
        this.id = id;
        this.idBooking = idBooking;
        this.idPayment = idPayment;
        this.idRefund = idRefund;
        this.ownerId = ownerId;
        this.customerId = customerId;
    }

    public RentalContractDto(RentalContract rentalContract) {
        this.id = rentalContract.getId();
        this.idBooking = rentalContract.getIdBooking();
        this.idPayment = rentalContract.getIdPayment();
        this.idRefund = rentalContract.getIdRefund();
        this.ownerId = rentalContract.getOwnerId();
        this.customerId = rentalContract.getCustomerId();
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
}
