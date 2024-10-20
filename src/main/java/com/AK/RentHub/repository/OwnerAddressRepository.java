package com.AK.RentHub.repository;

import com.AK.RentHub.model.OwnerAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerAddressRepository extends JpaRepository<OwnerAddress, Long> {
    OwnerAddress findByOwnerId(Long ownerId); // Custom query method if needed
}
