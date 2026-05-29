package com.lrms.hotelmanagement.service;

import com.lrms.hotelmanagement.dto.BookingDTO;
import com.lrms.hotelmanagement.dto.BookingRequest;
import com.lrms.hotelmanagement.dto.BookingResponse;
import com.lrms.hotelmanagement.entity.BookingStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingService {
    BookingResponse createBooking(BookingRequest bookingRequest);

    Optional<BookingDTO> getBookingById(Long id);

    List<BookingDTO> getAllBookings();

    List<BookingDTO> getCustomerBookings(Long customerId);

    List<BookingDTO> getRoomBookings(Long roomId);

    List<BookingDTO> getBookingsByStatus(BookingStatus status);

    BookingResponse updateBookingStatus(Long bookingId, BookingStatus newStatus);

    BookingResponse checkInBooking(Long bookingId);

    BookingResponse checkOutBooking(Long bookingId);

    void cancelBooking(Long bookingId);

    List<BookingDTO> getBookingHistory(Long customerId);

    boolean isRoomAvailable(Long roomId, LocalDate checkInDate, LocalDate checkOutDate);

    Double calculateTotalAmount(Long roomId, LocalDate checkInDate, LocalDate checkOutDate);

    List<BookingDTO> getBookingsCheckingInToday();

    List<BookingDTO> getBookingsCheckingOutToday();

    void deleteBooking(Long bookingId);
}
