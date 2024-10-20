package com.AK.RentHub.repository;

import com.AK.RentHub.model.UserCurrentLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCurrentLocationRepository extends JpaRepository<UserCurrentLocation, Long> {
    Optional<UserCurrentLocation> findByUserId(Long userId);
}
