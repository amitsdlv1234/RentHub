package com.AK.RentHub.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "bookedRoom")
public class BookedRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long room_id;

    private Long userId;

    private LocalDateTime bookingDate;

    private String bookingMessage; // Custom message, e.g., "Necessary visit room location..."

    private double bookingAmount;  // Confirm money paid by the user
}
