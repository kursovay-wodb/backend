package ru.rent.demo.dto;

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

    public BookingContractDto() {}

    public BookingContractDto(String id, LocalDate startRentDate, LocalDate endRentDate,
                              Integer quantity, String productName, Long productId,
                              BigDecimal rentalPrice, LocalDate bookingDate) {
        this.id = id;
        this.startRentDate = startRentDate;
        this.endRentDate = endRentDate;
        this.quantity = quantity;
        this.productName = productName;
        this.productId = productId;
        this.rentalPrice = rentalPrice;
        this.bookingDate = bookingDate;
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