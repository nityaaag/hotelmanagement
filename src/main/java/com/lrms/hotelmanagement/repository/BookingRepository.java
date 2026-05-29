package com.lrms.hotelmanagement.repository;

import com.lrms.hotelmanagement.entity.Booking;
import com.lrms.hotelmanagement.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByCustomerId(Long customerId);

    List<Booking> findByRoomId(Long roomId);

    List<Booking> findByBookingStatus(BookingStatus bookingStatus);

    @Query("SELECT b FROM Booking b WHERE b.room.id = :roomId AND b.bookingStatus IN ('CONFIRMED', 'CHECKED_IN') " +
            "AND ((b.checkInDate <= :checkOutDate) AND (b.checkOutDate >= :checkInDate))")
    List<Booking> findOverlappingBookings(@Param("roomId") Long roomId,
                                           @Param("checkInDate") LocalDate checkInDate,
                                           @Param("checkOutDate") LocalDate checkOutDate);

    @Query("SELECT b FROM Booking b WHERE b.customer.id = :customerId ORDER BY b.createdAt DESC")
    List<Booking> findCustomerBookingHistory(@Param("customerId") Long customerId);

    List<Booking> findByCheckOutDateAndBookingStatusIn(LocalDate checkOutDate, List<BookingStatus> statuses);

    List<Booking> findByCheckInDateAndBookingStatusIn(LocalDate checkInDate, List<BookingStatus> statuses);

    List<Booking> findByCheckInDate(LocalDate checkInDate); // Added for getBookingsCheckingInToday()
    List<Booking> findByCheckOutDate(LocalDate checkOutDate); // Added for getBookingsCheckingOutToday()
}
