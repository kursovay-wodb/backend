package ru.rent.demo.dto.booking;

import ru.rent.demo.entity.BookingContract;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BookingContractDto {
    private String id;
    private LocalDate startRentDate;
    private LocalDate endRentDate;
    private Integer quantity;
    private String productName;
    private Long productId;
    private BigDecimal rentalPrice;
    private LocalDate bookingDate;
    private boolean paid;

    public BookingContractDto() {}

    public BookingContractDto(BookingContract contract) {
        this.id = contract.getId();
        this.startRentDate = contract.getStartRentDate();
        this.endRentDate = contract.getEndRentDate();
        this.quantity = contract.getQuantity();
        this.productName = contract.getProductName();
        this.productId = contract.getProductId();
        this.rentalPrice = contract.getRentalPrice();
        this.bookingDate = contract.getBookingDate();
        this.paid = contract.isPaid();
    }

    public BookingContractDto(String id, LocalDate startRentDate, LocalDate endRentDate, Integer quantity, String productName, Long productId, BigDecimal rentalPrice, LocalDate bookingDate, boolean paid) {
        this.id = id;
        this.startRentDate = startRentDate;
        this.endRentDate = endRentDate;
        this.quantity = quantity;
        this.productName = productName;
        this.productId = productId;
        this.rentalPrice = rentalPrice;
        this.bookingDate = bookingDate;
        this.paid = paid;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public LocalDate getStartRentDate() { return startRentDate; }
    public void setStartRentDate(LocalDate startRentDate) { this.startRentDate = startRentDate; }

    public LocalDate getEndRentDate() { return endRentDate; }
    public void setEndRentDate(LocalDate endRentDate) { this.endRentDate = endRentDate; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public BigDecimal getRentalPrice() { return rentalPrice; }
    public void setRentalPrice(BigDecimal rentalPrice) { this.rentalPrice = rentalPrice; }

    public LocalDate getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDate bookingDate) { this.bookingDate = bookingDate; }
}