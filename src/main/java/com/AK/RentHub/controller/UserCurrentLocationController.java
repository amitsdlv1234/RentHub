package com.AK.RentHub.controller;

import com.AK.RentHub.model.UserCurrentLocation;
import com.AK.RentHub.security.GoogleMapsConfig;
import com.AK.RentHub.service.UserCurrentLocationService;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-location")
public class UserCurrentLocationController {

    @Autowired
    private UserCurrentLocationService userCurrentLocationService;
    @Autowired
    GoogleMapsConfig googleMapsConfig;
    // Save the user's current location (latitude, longitude)
    @PostMapping("/save")
    public ResponseEntity<UserCurrentLocation> saveUserLocation(
            @RequestBody UserCurrentLocation location) {

        UserCurrentLocation savedLocation = userCurrentLocationService.saveUserLocation(location);
        return ResponseEntity.ok(savedLocation);
    }

    // Fetch user's current location using Google Maps Geocoding API
    @GetMapping("/{userId}")
    public ResponseEntity<String> getUserLocationDetails(@PathVariable Long userId) {
        UserCurrentLocation userLocation = userCurrentLocationService.getUserCurrentLocation(userId);

        if (userLocation == null) {
            return ResponseEntity.badRequest().body("User location not found");
        }

        try {
            // Use Google Maps Geocoding API to get the location details from latitude and longitude
            GeocodingResult[] results = GeocodingApi.reverseGeocode(googleMapsConfig.geoApiContext(),
                    new com.google.maps.model.LatLng(userLocation.getLatitude(), userLocation.getLongitude())).await();

            if (results != null && results.length > 0) {
                String formattedAddress = results[0].formattedAddress;
                return ResponseEntity.ok("User's current location: " + formattedAddress);
            } else {
                return ResponseEntity.badRequest().body("No location details found");
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error while fetching location details: " + e.getMessage());
        }
    }
}
