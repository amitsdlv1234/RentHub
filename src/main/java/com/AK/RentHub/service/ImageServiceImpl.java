package com.AK.RentHub.service;

import com.AK.RentHub.service.CloudinaryService;
import com.AK.RentHub.service.ImageService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public String handleImageUpdate(String currentImageUrl, MultipartFile newImageFile) throws IOException {
        // If the new image file is provided
        if (newImageFile != null && !newImageFile.isEmpty()) {

            // If there's an existing image URL, delete the old image from Cloudinary
            if (currentImageUrl != null) {
                String publicId = cloudinaryService.extractPublicIdFromUrl(currentImageUrl);
                cloudinaryService.deleteImage(publicId);
            }

            // Upload the new image and return its URL
            return cloudinaryService.uploadImage(newImageFile);
        }

        // Return the existing image URL if no new image is provided
        return currentImageUrl;
    }
    // Extract the public ID from the image URL
    @Override
    public String extractPublicIdFromUrl(String imageUrl) {
        String[] parts = imageUrl.split("/");
        String publicIdWithExtension = parts[parts.length - 1];
        return publicIdWithExtension.split("\\.")[0];
    }
    // Delete the image from Cloudinary using the public ID
    @Override
    public void deleteImage(String publicId) throws IOException {
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }
}
