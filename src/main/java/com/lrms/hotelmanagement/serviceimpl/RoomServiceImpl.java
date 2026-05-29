package com.lrms.hotelmanagement.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lrms.hotelmanagement.entity.Room;
import com.lrms.hotelmanagement.entity.RoomStatus;
import com.lrms.hotelmanagement.exception.InvalidBookingException;
import com.lrms.hotelmanagement.repository.RoomRepository;
import com.lrms.hotelmanagement.service.RoomService;

@Service
@Transactional(readOnly = true)
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    @Transactional
    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    @Override
    public Optional<Room> getRoomById(Long id) {
        return roomRepository.findById(id);
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public List<Room> getRoomsByStatus(RoomStatus status) {
        return roomRepository.findByStatus(status);
    }

    @Override
    public List<Room> getRoomsByType(String roomType) {
        return roomRepository.findByRoomType(roomType);
    }

    @Override
    @Transactional
    public Room updateRoom(Long id, Room roomDetails) {
        Room existingRoom = roomRepository.findById(id)
                .orElseThrow(() -> new InvalidBookingException("Room not found with id: " + id));

        existingRoom.setRoomNumber(roomDetails.getRoomNumber());
        existingRoom.setRoomType(roomDetails.getRoomType());
        existingRoom.setPrice(roomDetails.getPrice());
        existingRoom.setStatus(roomDetails.getStatus());
        existingRoom.setDescription(roomDetails.getDescription());
        
        return roomRepository.save(existingRoom);
    }

    @Override
    @Transactional
    public void deleteRoom(Long id) {
        if (!roomRepository.existsById(id)) {
            throw new InvalidBookingException("Room not found with id: " + id);
        }
        roomRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Room updateRoomStatus(Long id, RoomStatus status) {
        Room existingRoom = roomRepository.findById(id)
                .orElseThrow(() -> new InvalidBookingException("Room not found with id: " + id));
                
        existingRoom.setStatus(status);
        return roomRepository.save(existingRoom);
    }
}
