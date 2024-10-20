package com.AK.RentHub.service;

import com.AK.RentHub.model.OwnerAddress;
import com.AK.RentHub.repository.OwnerAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class OwnerAddressServiceImpl implements OwnerAddressService {

    @Autowired
    private OwnerAddressRepository ownerAddressRepository;

    @Override
    public OwnerAddress saveOwnerAddress(OwnerAddress ownerAddress) {
        // Validate fields if necessary
        validateOwnerAddress(ownerAddress);
        return ownerAddressRepository.save(ownerAddress);
    }

    @Override
    public OwnerAddress updateOwnerAddress(Long ownerId, OwnerAddress newAddress) {
        Optional<OwnerAddress> existingAddressOptional = Optional.ofNullable(ownerAddressRepository.findByOwnerId(ownerId));

        if (existingAddressOptional.isPresent()) {
            OwnerAddress existingAddress = existingAddressOptional.get();
            existingAddress.setStreet(newAddress.getStreet());
            existingAddress.setCity(newAddress.getCity());
            existingAddress.setState(newAddress.getState());
            existingAddress.setPostalCode(newAddress.getPostalCode());
            existingAddress.setCountry(newAddress.getCountry());
            return ownerAddressRepository.save(existingAddress);
        }
        throw new IllegalArgumentException("Owner with ID " + ownerId + " not found.");
    }

    @Override
    public Optional<OwnerAddress> findByOwnerId(Long ownerId) {
        return Optional.ofNullable(ownerAddressRepository.findByOwnerId(ownerId));
    }

    // Optional validation method
    private void validateOwnerAddress(OwnerAddress ownerAddress) {
        // Implement your validation logic here
        if (ownerAddress.getStreet() == null || ownerAddress.getStreet().isEmpty()) {
            throw new IllegalArgumentException("Street cannot be null or empty");
        }
        if (ownerAddress.getCity() == null || ownerAddress.getCity().isEmpty()) {
            throw new IllegalArgumentException("City cannot be null or empty");
        }
        // Add more validations as needed
    }
}
