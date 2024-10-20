package com.AK.RentHub.service;

import com.AK.RentHub.model.BookedRoom;
import com.AK.RentHub.model.Room;
import com.AK.RentHub.model.User;
import com.AK.RentHub.repository.BookedRoomRepository;
import com.AK.RentHub.repository.RoomRepository;

import com.AK.RentHub.security.JwtFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtFilter jwtFilter;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private BookedRoomRepository bookedRoomRepository;
    @Override
    public Room addRoom(Room room) {
        String email = jwtFilter.getUsername();
        Long ownerId = userService.findByEmail(email).getId();
        room.setOwnerId(ownerId);
        User user=userService.findByEmail(email);
        if(user!=null){
            return roomRepository.save(room); // Save the room to the database
        }
            return null;
    }

    @Override
    public void updateRoom(Long roomId, Room roomDetails) {
        Optional<Room> existingRoom = roomRepository.findById(roomId);
        if (existingRoom.isPresent()) {
            Room room = existingRoom.get();
            room.setNoOfRoom(roomDetails.getNoOfRoom());
            room.setFloor(roomDetails.getFloor());
            room.setRoomType(roomDetails.getRoomType());
            room.setRent(roomDetails.getRent());
            room.setDescription(roomDetails.getDescription());
            room.setImageUrl(roomDetails.getImageUrl());
            room.setWifiAvailable(roomDetails.isWifiAvailable());
            room.setAcAvailable(roomDetails.isAcAvailable());
            room.setParkingAvailable(roomDetails.isParkingAvailable());
            room.setPetFriendly(roomDetails.isPetFriendly());
            room.setOwnerContact(roomDetails.getOwnerContact());
            room.setAvailabilityStartDate(roomDetails.getAvailabilityStartDate());
            room.setAvailabilityEndDate(roomDetails.getAvailabilityEndDate());
            room.setBooked(roomDetails.isBooked());
            roomRepository.save(room); // Update room details
        }
    }

    @Override
    public void deleteRoom(Long roomId) {
        roomRepository.deleteById(roomId); // Delete room by ID
    }

    @Override
    public BookedRoom bookRoom(Long roomId) {
        Optional<Room> roomOptional = roomRepository.findById(roomId);
        if (roomOptional.isPresent()) {
            Room room = roomOptional.get();
            if (!room.isBooked()) {
                room.setBooked(true);  // Mark room as booked
                roomRepository.save(room);  // Save the updated room

                // Get user information from JWT
                String email = jwtFilter.getUsername();
                Long userId = userService.findByEmail(email).getId();

                // Create BookedRoom entry
                BookedRoom bookedRoom = new BookedRoom();
                bookedRoom.setRoom_id(roomId);
                bookedRoom.setUserId(userId);
                bookedRoom.setBookingDate(LocalDateTime.now());
                bookedRoom.setBookingMessage(room.getDescription());
                bookedRoom.setBookingAmount(room.getRent());

                // Save the booking information
                return bookedRoomRepository.save(bookedRoom);
            } else {
                // Return null if the room is already booked
                System.out.println("Room is already booked.");
                return null;
            }
        } else {
            // Return null if the room is not found
            System.out.println("Room not found.");
            return null;
        }
    }

    @Override
    public Room getRoomById(Long id) {
        return roomRepository.findById(id).orElse(null); // Fetch room by ID
    }

    @Override
    public List<Room> getAvailableRooms() {
        return roomRepository.findByIsBooked(false); // Fetch only available rooms (not booked)
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public List<Room> getOwnerRooms() {
        String email = jwtFilter.getUsername();
        Long ownerId = userService.findByEmail(email).getId();
        return roomRepository.findByOwnerId(ownerId);
    }
}
