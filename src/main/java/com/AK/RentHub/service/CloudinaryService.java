package com.AK.RentHub.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    // Upload image to Cloudinary and return the URL
    public String uploadImage(MultipartFile imageFile) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), ObjectUtils.emptyMap());
        return (String) uploadResult.get("url");
    }

    // Extract public_id from the Cloudinary URL
    public String extractPublicIdFromUrl(String imageUrl) {
        // Example: https://res.cloudinary.com/<cloud_name>/image/upload/<public_id>.<file_extension>
        String[] parts = imageUrl.split("/");
        String publicIdWithExtension = parts[parts.length - 1];  // Get the last part of the URL which is <public_id>.<file_extension>
        return publicIdWithExtension.split("\\.")[0];  // Remove the file extension from public_id
    }

    // Delete an image from Cloudinary by public_id
    public void deleteImage(String publicId) throws IOException {
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }
}
