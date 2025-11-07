package ru.rent.demo.entity;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Document(collection = "booking_contracts")
public class BookingContract {

    @Id
    private String id;
    private LocalDate startRentDate;
    private LocalDate endRentDate;
    private Integer quantity;
    private String productName;
    private Long productId;
    private BigDecimal rentalPrice;
    private LocalDate bookingDate;
    private Long customerId;
    private boolean paid = false;

    public BookingContract() {}

    public BookingContract(String id, LocalDate startRentDate, LocalDate endRentDate, Integer quantity, String productName, Long productId, BigDecimal rentalPrice, LocalDate bookingDate, Long customerId, boolean paid) {
        this.id = id;
        this.startRentDate = startRentDate;
        this.endRentDate = endRentDate;
        this.quantity = quantity;
        this.productName = productName;
        this.productId = productId;
        this.rentalPrice = rentalPrice;
        this.bookingDate = bookingDate;
        this.customerId = customerId;
        this.paid = paid;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public BigDecimal getRentalPrice() {
        return rentalPrice;
    }

    public void setRentalPrice(BigDecimal rentalPrice) {
        this.rentalPrice = rentalPrice;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
