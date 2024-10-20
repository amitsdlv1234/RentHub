package com.AK.RentHub.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {

    // Method to handle deleting the old image and uploading the new one
    String handleImageUpdate(String currentImageUrl, MultipartFile newImageFile) throws IOException;
    String extractPublicIdFromUrl(String imageUrl);
    void deleteImage(String publicId) throws IOException;
}
