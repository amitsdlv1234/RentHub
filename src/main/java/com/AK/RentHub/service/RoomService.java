package com.AK.RentHub.service;

import com.AK.RentHub.model.BookedRoom;
import com.AK.RentHub.model.Room;

import java.util.List;

public interface RoomService {
    Room addRoom(Room room);
    void updateRoom(Long roomId, Room roomDetails);
    void deleteRoom(Long roomId);
    BookedRoom bookRoom(Long roomId);
    Room getRoomById(Long id);
    List<Room> getAvailableRooms();
    List<Room> getAllRooms();
    List<Room> getOwnerRooms();
}
