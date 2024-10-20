package com.AK.RentHub.service;

import com.AK.RentHub.model.UserCurrentLocation;

public interface UserCurrentLocationService {
    UserCurrentLocation saveUserLocation(UserCurrentLocation location);
    UserCurrentLocation getUserCurrentLocation(Long userId);
}
