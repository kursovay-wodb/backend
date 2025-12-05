package ru.rent.demo.dto;

import ru.rent.demo.entity.User;

import java.util.Set;

public class UserDto {

    private Long id;
    private String email;
    private String name;
    private Integer dealsAsOwner;
    private Integer dealsAsCustomer;
    private String provider;
    private String role;

    public UserDto() {}
    public UserDto(String email, String name,Integer dealsAsOwner, Integer dealsAsCustomer, String provider, String role) {
        this.email = email;
        this.name = name;
        this.dealsAsOwner = dealsAsOwner;
        this.dealsAsCustomer = dealsAsCustomer;
        this.provider = provider;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDealsAsOwner() {
        return dealsAsOwner;
    }

    public void setDealsAsOwner(Integer dealsAsOwner) {
        this.dealsAsOwner = dealsAsOwner;
    }

    public Integer getDealsAsCustomer() {
        return dealsAsCustomer;
    }

    public void setDealsAsCustomer(Integer dealsAsCustomer) {
        this.dealsAsCustomer = dealsAsCustomer;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
