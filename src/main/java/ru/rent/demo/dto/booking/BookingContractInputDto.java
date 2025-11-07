package ru.rent.demo.dto.booking;

import java.time.LocalDate;

public class BookingContractInputDto {
    private Long productId;
    private LocalDate startRentDate;
    private LocalDate endRentDate;
    private Integer quantity;
    private boolean paid;

    public BookingContractInputDto() {}

    public BookingContractInputDto(Long productId, LocalDate startRentDate, LocalDate endRentDate, Integer quantity, boolean paid) {
        this.productId = productId;
        this.startRentDate = startRentDate;
        this.endRentDate = endRentDate;
        this.quantity = quantity;
        this.paid = paid;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public LocalDate getStartRentDate() {
        return startRentDate;
    }

    public void setStartRentDate(LocalDate startRentDate) {
        this.startRentDate = startRentDate;
    }

    public LocalDate getEndRentDate() {
        return endRentDate;
    }

    public void setEndRentDate(LocalDate endRentDate) {
        this.endRentDate = endRentDate;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}