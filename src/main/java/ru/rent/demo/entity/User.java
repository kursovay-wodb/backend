package ru.rent.demo.entity;

import jakarta.persistence.*;
import ru.rent.demo.utils.auth.AuthProvider;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String name;

    private String password;

    private int dealsAsOwner;

    private int dealsAsCustomer;

    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<Role> roles = new HashSet<>();

    public enum Role {
        ROLE_USER,
        ROLE_ADMIN
    }


    public User() {}

    public User(String email, String name, AuthProvider provider) {
        this.email = email;
        this.name = name;
        this.provider = provider;
    }

    public User(String email, String name, AuthProvider provider, String password) {
        this.email = email;
        this.name = name;
        this.provider = provider;
        this.password = password;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public AuthProvider getProvider() { return provider; }
    public void setProvider(AuthProvider provider) { this.provider = provider; }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public int getDealsAsOwner() {
        return dealsAsOwner;
    }

    public void setDealsAsOwner(int dealsAsOwner) {
        this.dealsAsOwner = dealsAsOwner;
    }

    public int getDealsAsCustomer() {
        return dealsAsCustomer;
    }

    public void setDealsAsCustomer(int dealsAsCustomer) {
        this.dealsAsCustomer = dealsAsCustomer;
    }
}
