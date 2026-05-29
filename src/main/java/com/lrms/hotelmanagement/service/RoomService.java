package com.lrms.hotelmanagement.service;

import java.util.List;
import java.util.Optional;

import com.lrms.hotelmanagement.entity.Room;
import com.lrms.hotelmanagement.entity.RoomStatus;

public interface RoomService {
    Room createRoom(Room room);

    Optional<Room> getRoomById(Long id);

    List<Room> getAllRooms();

    List<Room> getRoomsByStatus(RoomStatus status);

    List<Room> getRoomsByType(String roomType);

    Room updateRoom(Long id, Room room);

    void deleteRoom(Long id);

    Room updateRoomStatus(Long id, RoomStatus status);
}
