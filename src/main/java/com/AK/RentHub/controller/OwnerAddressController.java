package com.AK.RentHub.controller;

import com.AK.RentHub.model.OwnerAddress;
import com.AK.RentHub.model.User;
import com.AK.RentHub.security.JwtFilter;
import com.AK.RentHub.service.OwnerAddressService;
import com.AK.RentHub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/address")
public class OwnerAddressController {

    @Autowired
    private OwnerAddressService ownerAddressService;

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private UserService userService;

    // Endpoint to add a new address for an owner
    @PostMapping("/")
    public ResponseEntity<String> addOwnerAddress(@RequestBody OwnerAddress ownerAddress) {
        try {
            String email = jwtFilter.getUsername();
            User user = userService.findByEmail(email);
            if (user == null) {
                return ResponseEntity.status(402).body("User not registered");
            }

            if (user.getRole() == User.Role.OWNER) {
                ownerAddressService.saveOwnerAddress(ownerAddress);
                return ResponseEntity.ok("Owner address added successfully");
            }
            return ResponseEntity.status(400).body("Invalid Owner");
        } catch (Exception e) {
            // Consider using a logging framework instead of System.out
            System.err.println("Error while adding owner address: " + e.getMessage());
            return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage());
        }
    }

    // Endpoint to update the address of an existing owner
    @PutMapping("/update/{ownerId}")
    public ResponseEntity<String> updateOwnerAddress(@PathVariable Long ownerId, @RequestBody OwnerAddress updatedAddress) {
        try {
            OwnerAddress updated = ownerAddressService.updateOwnerAddress(ownerId, updatedAddress);
            if (updated != null) {
                return ResponseEntity.ok("Owner address updated successfully");
            }
            return ResponseEntity.status(404).body("Owner address not found");
        } catch (Exception e) {
            // Consider using a logging framework instead of System.out
            System.err.println("Error while updating owner address: " + e.getMessage());
            return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage());
        }
    }

    // Endpoint to fetch the address of an owner
    @GetMapping("/{ownerId}")
    public ResponseEntity<OwnerAddress> getOwnerAddress(@PathVariable Long ownerId) {
        try {
            return ownerAddressService.findByOwnerId(ownerId)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(404).body(null));
        } catch (Exception e) {
            // Consider using a logging framework instead of System.out
            System.err.println("Error while fetching owner address: " + e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }
}
