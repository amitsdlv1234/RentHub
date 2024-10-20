package com.AK.RentHub.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    // Gender enum for room restrictions
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private Long ownerId;
    private String roomType;
    private int noOfRoom;
    private int floor;

    private boolean isBooked=false;
    private int rent;
    private String description;

    // Cloudinary Image URL
    private String imageUrl;

    // Other amenities
    private boolean wifiAvailable;
    private boolean acAvailable;
    private boolean parkingAvailable;
    private boolean petFriendly;

    // Contact details
    private String ownerContact;

    // Availability details
    private String availabilityStartDate;
    private String availabilityEndDate;

    // Enum for gender restriction
    public enum Gender {
        MALE,
        FEMALE,
        UNISEX
    }
}
