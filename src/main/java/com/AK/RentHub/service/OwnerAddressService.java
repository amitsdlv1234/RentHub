package com.AK.RentHub.service;

import com.AK.RentHub.model.OwnerAddress;
import java.util.Optional;

public interface OwnerAddressService {

    OwnerAddress saveOwnerAddress(OwnerAddress ownerAddress);

    OwnerAddress updateOwnerAddress(Long ownerId, OwnerAddress newAddress);

    Optional<OwnerAddress> findByOwnerId(Long ownerId);
}
