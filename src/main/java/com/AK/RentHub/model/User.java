package com.AK.RentHub.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    private String email; // User can register with email or mobileNo
    private String mobileNo; // User can register with email or mobileNo

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "is_verified")
    private Boolean isVerified = false;

    public enum Role {
        USER,
        ADMIN,
        OWNER
    }

}
