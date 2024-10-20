package com.AK.RentHub.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "owner_addresses")
public class OwnerAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long ownerId;
    private String houseNo;
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;

    @Override
    public String toString() {
        return
                ", houseNo='" + houseNo + '\'' +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", country='" + country + '\'';
    }
}
