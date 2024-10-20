package com.AK.RentHub.repository;

import com.AK.RentHub.model.BookedRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookedRoomRepository extends JpaRepository<BookedRoom, Long> {
}
