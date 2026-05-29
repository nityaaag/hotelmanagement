package com.lrms.hotelmanagement.repository;

import com.lrms.hotelmanagement.entity.Room;
import com.lrms.hotelmanagement.entity.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByStatus(RoomStatus status);

    List<Room> findByRoomType(String roomType);
}