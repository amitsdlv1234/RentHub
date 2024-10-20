package com.AK.RentHub.controller;

import com.AK.RentHub.model.BookedRoom;
import com.AK.RentHub.model.Room;
import com.AK.RentHub.service.CloudinaryService;
import com.AK.RentHub.service.ImageService;
import com.AK.RentHub.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/room")
public class RoomBookingController {

    @Autowired
    private RoomService roomService;


    @Autowired
    private ImageService imageService;
    @Autowired
    private CloudinaryService cloudinaryService;

    // Owner adds a room along with an image
    @PostMapping("/owner/add-room")
    public ResponseEntity<String> addRoom(@RequestPart Room room,
                                          @RequestPart MultipartFile imageFile) throws IOException {
        try {
            // Upload the image to Cloudinary and get the image URL
            String imageUrl = cloudinaryService.uploadImage(imageFile);

            // Set the image URL in the room object
            room.setImageUrl(imageUrl);

            // Add the room with the image URL
            Room room1=roomService.addRoom(room);
            if(room1!=null){
                return ResponseEntity.ok("Room added successfully with image");
            }
            return ResponseEntity.ok("Firstly register yourself");
        }catch (Exception e){
            System.out.println("Internal Server Error :"+e);
            return ResponseEntity.status(500).body("Internal Server Error :"+e);
        }
    }


    // Owner updates room details
    @PutMapping("/owner/update-room/{roomId}")
    public ResponseEntity<String> updateRoom(@PathVariable Long roomId, @RequestPart Room room,
                                             @RequestPart MultipartFile imageFile) throws IOException {
         try {
             // Retrieve existing room details if the room already exists
              System.out.println("RoomId "+roomId);
             Room existingRoom = (roomId != null) ? roomService.getRoomById(roomId) : null;
             if(existingRoom==null){
                 return ResponseEntity.status(401).body("Room not found");
             }
             // Handle image update (delete old image if exists and upload new one)
             String updatedImageUrl = imageService.handleImageUpdate(
                     existingRoom.getImageUrl(),
                     imageFile
             );

             // Set the new image URL in the room object
             if(updatedImageUrl!=null){
                 room.setImageUrl(updatedImageUrl);
             }else {
                 return ResponseEntity.status(301).body("Image not upload");
             }

             // Add or update the room with the (new or existing) image URL
             roomService.updateRoom(roomId,room);

             return ResponseEntity.ok("Room added/updated successfully with image");
         }catch (Exception e){
             System.out.println("Internal Server Error :"+e);
             return ResponseEntity.status(500).body("Internal Server Error :"+e);
         }
    }

    // Owner deletes a room
    @DeleteMapping("/owner/delete-room/{roomId}")
    public ResponseEntity<String> deleteRoom(@PathVariable Long roomId) {
        try {
            // Step 1: Retrieve the room details by roomId
            Room room = roomService.getRoomById(roomId);

            if (room != null) {
                // Step 2: Check if the room has an associated image URL
                String imageUrl = room.getImageUrl();
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    // Step 3: Extract the public ID from the image URL
                    String publicId = imageService.extractPublicIdFromUrl(imageUrl);

                    // Step 4: Delete the image from Cloudinary
                    try {
                        imageService.deleteImage(publicId);
                    } catch (IOException e) {
                        return ResponseEntity.status(500).body("Error deleting image from Cloudinary: " + e.getMessage());
                    }
                }

                // Step 5: Delete the room from the database
                roomService.deleteRoom(roomId);
                return ResponseEntity.ok("Room and associated image deleted successfully");
            } else {
                return ResponseEntity.status(404).body("Room not found");
            }
        }
        catch (Exception e){
            System.out.println("Internal Server Error :"+e);
            return ResponseEntity.status(500).body("Internal Server Error :"+e);
        }
    }
    // User books a room
    @PostMapping("/user/book-room/roomId/{roomId}")
    public ResponseEntity<String> bookRoom(@PathVariable Long roomId) {
        try {
            BookedRoom bookedRoom = roomService.bookRoom(roomId);
            if (bookedRoom != null) {
                return ResponseEntity.ok("Room booked successfully. Please visit the room as per the owner's guidance.");
            } else {
                return ResponseEntity.status(400).body("Room is already booked or doesn't exist.");
            }
        } catch (Exception e) {
            System.out.println("Internal Server Error: " + e);
            return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage());
        }
    }


    // User views available rooms
    @GetMapping("/user/available-rooms")
    public ResponseEntity<List<Room>> getAvailableRooms() {
        try{
            List<Room> availableRooms = roomService.getAvailableRooms();
            return ResponseEntity.ok(availableRooms);
        }catch (Exception e){
            System.out.println("Internal Server Error :"+e);
            return null;
        }
    }

    // Fetch room details by ID (for both users and owners)
    @GetMapping("/room-details/{roomId}")
    public ResponseEntity<Room> getRoomDetails(@PathVariable Long roomId) {
        Room room = roomService.getRoomById(roomId);
        return ResponseEntity.ok(room);
    }
    // Fetch all rooms along with their booking status
    @GetMapping("/all-rooms")
    public ResponseEntity<List<Room>> getAllRooms() {
        try {
            List<Room> allRooms = roomService.getAllRooms();  // Get all rooms from the service
            return ResponseEntity.ok(allRooms);
        } catch (Exception e) {
            System.out.println("Internal Server Error: " + e);
            return ResponseEntity.status(500).body(null);
        }
    }
    @GetMapping("/owner/all-rooms")
    public ResponseEntity<List<Room>> getOwnerRooms() {
        try {
            List<Room> ownerRooms = roomService.getOwnerRooms();  // Get rooms owned by the user
            return ResponseEntity.ok(ownerRooms);
        } catch (Exception e) {
            System.out.println("Internal Server Error: " + e);
            return ResponseEntity.status(500).body(null);
        }
    }
}
