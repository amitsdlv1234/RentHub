package com.AK.RentHub.service;

import com.AK.RentHub.model.UserCurrentLocation;
import com.AK.RentHub.repository.UserCurrentLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserCurrentLocationServiceImpl implements UserCurrentLocationService {

    @Autowired
    private UserCurrentLocationRepository userCurrentLocationRepository;

    @Override
    public UserCurrentLocation saveUserLocation(UserCurrentLocation location) {
        return userCurrentLocationRepository.save(location);
    }

    @Override
    public UserCurrentLocation getUserCurrentLocation(Long userId) {
        return userCurrentLocationRepository.findByUserId(userId).orElse(null);
    }
}
