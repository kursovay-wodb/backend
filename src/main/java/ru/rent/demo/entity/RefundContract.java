package ru.rent.demo.entity;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "refund_contracts")
public class RefundContract {

    @Id
    private String id;

    private String rentalId;

    private String description;

    private Long userId;

    public RefundContract() {
    }

    public RefundContract(String id, String rentalId, String description, Long userId) {
        this.id = id;
        this.rentalId = rentalId;
        this.description = description;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRentalId() {
        return rentalId;
    }

    public void setRentalId(String rentalId) {
        this.rentalId = rentalId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
