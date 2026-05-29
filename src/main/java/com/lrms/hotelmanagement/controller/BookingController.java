package com.lrms.hotelmanagement.controller;

import com.lrms.hotelmanagement.dto.BookingDTO;
import com.lrms.hotelmanagement.dto.BookingRequest;
import com.lrms.hotelmanagement.dto.BookingResponse;
import com.lrms.hotelmanagement.entity.BookingStatus;
import com.lrms.hotelmanagement.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingRequest bookingRequest) {
        BookingResponse booking = bookingService.createBooking(bookingRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(booking);
    }

    @GetMapping
    public ResponseEntity<List<BookingDTO>> getAllBookings() {
        List<BookingDTO> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable Long id) {
        Optional<BookingDTO> booking = bookingService.getBookingById(id);
        return booking.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<BookingDTO>> getCustomerBookings(@PathVariable Long customerId) {
        List<BookingDTO> bookings = bookingService.getCustomerBookings(customerId);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/customer/{customerId}/history")
    public ResponseEntity<List<BookingDTO>> getBookingHistory(@PathVariable Long customerId) {
        List<BookingDTO> history = bookingService.getBookingHistory(customerId);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<BookingDTO>> getRoomBookings(@PathVariable Long roomId) {
        List<BookingDTO> bookings = bookingService.getRoomBookings(roomId);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<BookingDTO>> getBookingsByStatus(@PathVariable BookingStatus status) {
        List<BookingDTO> bookings = bookingService.getBookingsByStatus(status);
        return ResponseEntity.ok(bookings);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<BookingResponse> updateBookingStatus(
            @PathVariable Long id,
            @RequestParam BookingStatus status) {
        BookingResponse updatedBooking = bookingService.updateBookingStatus(id, status);
        return ResponseEntity.ok(updatedBooking);
    }

    @PostMapping("/{id}/check-in")
    public ResponseEntity<BookingResponse> checkInBooking(@PathVariable Long id) {
        BookingResponse checkedInBooking = bookingService.checkInBooking(id);
        return ResponseEntity.ok(checkedInBooking);
    }

    @PostMapping("/{id}/check-out")
    public ResponseEntity<BookingResponse> checkOutBooking(@PathVariable Long id) {
        BookingResponse checkedOutBooking = bookingService.checkOutBooking(id);
        return ResponseEntity.ok(checkedOutBooking);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/availability/check")
    public ResponseEntity<Boolean> checkRoomAvailability(
            @RequestParam Long roomId,
            @RequestParam LocalDate checkInDate,
            @RequestParam LocalDate checkOutDate) {
        boolean isAvailable = bookingService.isRoomAvailable(roomId, checkInDate, checkOutDate);
        return ResponseEntity.ok(isAvailable);
    }

    @GetMapping("/charge/calculate")
    public ResponseEntity<Double> calculateRoomCharge(
            @RequestParam Long roomId,
            @RequestParam LocalDate checkInDate,
            @RequestParam LocalDate checkOutDate) {
        Double totalAmount = bookingService.calculateTotalAmount(roomId, checkInDate, checkOutDate);
        return ResponseEntity.ok(totalAmount);
    }

    @GetMapping("/check-in/today")
    public ResponseEntity<List<BookingDTO>> getCheckInsToday() {
        List<BookingDTO> bookings = bookingService.getBookingsCheckingInToday();
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/check-out/today")
    public ResponseEntity<List<BookingDTO>> getCheckOutsToday() {
        List<BookingDTO> bookings = bookingService.getBookingsCheckingOutToday();
        return ResponseEntity.ok(bookings);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }
}
