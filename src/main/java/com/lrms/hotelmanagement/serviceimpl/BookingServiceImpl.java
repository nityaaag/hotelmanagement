package com.lrms.hotelmanagement.serviceimpl;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lrms.hotelmanagement.dto.BookingDTO;
import com.lrms.hotelmanagement.dto.BookingRequest;
import com.lrms.hotelmanagement.dto.BookingResponse;
import com.lrms.hotelmanagement.entity.Booking;
import com.lrms.hotelmanagement.entity.BookingStatus;
import com.lrms.hotelmanagement.entity.Room;
import com.lrms.hotelmanagement.entity.User;
import com.lrms.hotelmanagement.exception.InvalidBookingException;
import com.lrms.hotelmanagement.repository.BookingRepository;
import com.lrms.hotelmanagement.repository.RoomRepository;
import com.lrms.hotelmanagement.service.BookingService;
import com.lrms.hotelmanagement.service.UserService;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserService userService;

    public BookingServiceImpl(BookingRepository bookingRepository,
                              RoomRepository roomRepository,
                              UserService userService) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.userService = userService;
    }

    @Override
    @Transactional
    public BookingResponse createBooking(BookingRequest bookingRequest) {

        if (!bookingRequest.getCheckOutDate().isAfter(bookingRequest.getCheckInDate())) {
            throw new InvalidBookingException("Check-out date must be after check-in date (minimum 1 night stay)");
        }

        if (bookingRequest.getCheckInDate().isBefore(LocalDate.now())) {
            throw new InvalidBookingException("Check-in date cannot be in the past");
        }

        if (!isRoomAvailable(
                bookingRequest.getRoomId(),
                bookingRequest.getCheckInDate(),
                bookingRequest.getCheckOutDate())) {
            throw new InvalidBookingException("Room not available");
        }

        User customer = userService.getUserById(bookingRequest.getCustomerId())
                .orElseThrow(() -> new InvalidBookingException("Customer not found"));

        Room room = roomRepository.findById(bookingRequest.getRoomId())
                .orElseThrow(() -> new InvalidBookingException("Room not found"));

        Double totalAmount = calculateTotalAmount(
                bookingRequest.getRoomId(),
                bookingRequest.getCheckInDate(),
                bookingRequest.getCheckOutDate());

        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setRoom(room);
        booking.setCheckInDate(bookingRequest.getCheckInDate());
        booking.setCheckOutDate(bookingRequest.getCheckOutDate());
        booking.setTotalAmount(totalAmount);
        booking.setBookingStatus(BookingStatus.PENDING);

        Booking saved = bookingRepository.save(booking);

        return convertToBookingResponse(saved);
    }

    @Override
    public Optional<BookingDTO> getBookingById(Long id) {
        return bookingRepository.findById(id).map(this::convertToBookingDTO);
    }

    @Override
    public List<BookingDTO> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(this::convertToBookingDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDTO> getCustomerBookings(Long customerId) {
        return bookingRepository.findByCustomerId(customerId)
                .stream()
                .map(this::convertToBookingDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDTO> getRoomBookings(Long roomId) {
        return bookingRepository.findByRoomId(roomId)
                .stream()
                .map(this::convertToBookingDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDTO> getBookingsByStatus(BookingStatus status) {
        return bookingRepository.findByBookingStatus(status)
                .stream()
                .map(this::convertToBookingDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDTO> getBookingHistory(Long customerId) {
        return bookingRepository.findCustomerBookingHistory(customerId)
                .stream()
                .map(this::convertToBookingDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookingResponse updateBookingStatus(Long bookingId, BookingStatus newStatus) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new InvalidBookingException("Booking not found"));

        booking.setBookingStatus(newStatus);

        return convertToBookingResponse(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingResponse checkInBooking(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new InvalidBookingException("Booking not found"));

        booking.setBookingStatus(BookingStatus.CHECKED_IN);

        return convertToBookingResponse(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingResponse checkOutBooking(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new InvalidBookingException("Booking not found"));

        booking.setBookingStatus(BookingStatus.CHECKED_OUT);

        return convertToBookingResponse(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public void cancelBooking(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new InvalidBookingException("Booking not found"));

        booking.setBookingStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }

    @Override
    public boolean isRoomAvailable(Long roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        return bookingRepository.findOverlappingBookings(roomId, checkInDate, checkOutDate).isEmpty();
    }

    @Override
    public Double calculateTotalAmount(Long roomId, LocalDate checkInDate, LocalDate checkOutDate) {

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new InvalidBookingException("Room not found"));

        long nights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);

        return room.getPrice() * nights;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDTO> getBookingsCheckingInToday() {
        LocalDate today = LocalDate.now();
        // Include both PENDING and CONFIRMED bookings arriving today
        List<BookingStatus> arrivalStatuses = List.of(BookingStatus.PENDING, BookingStatus.CONFIRMED);
        return bookingRepository.findByCheckInDateAndBookingStatusIn(today, arrivalStatuses)
                .stream()
                .map(this::convertToBookingDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true) // Added readOnly for consistency
    public List<BookingDTO> getBookingsCheckingOutToday() {
        LocalDate today = LocalDate.now();
        // See bookings that are currently CHECKED_IN and scheduled to leave today
        return bookingRepository.findByCheckOutDateAndBookingStatusIn(today, List.of(BookingStatus.CHECKED_IN))
                .stream()
                .map(this::convertToBookingDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteBooking(Long bookingId) {
        if (!bookingRepository.existsById(bookingId)) {
            throw new InvalidBookingException("Booking not found with id: " + bookingId);
        }
        bookingRepository.deleteById(bookingId);
    }

    private BookingDTO convertToBookingDTO(Booking booking) {
        BookingDTO dto = new BookingDTO();
        dto.setId(booking.getId());
        dto.setCustomerId(booking.getCustomer().getId());
        dto.setRoomId(booking.getRoom().getId());
        dto.setCheckInDate(booking.getCheckInDate());
        dto.setCheckOutDate(booking.getCheckOutDate());
        dto.setTotalAmount(booking.getTotalAmount());
        dto.setBookingStatus(booking.getBookingStatus());
        return dto;
    }

    private BookingResponse convertToBookingResponse(Booking booking) {
        BookingResponse response = new BookingResponse();
        response.setId(booking.getId());
        response.setCustomerId(booking.getCustomer().getId());
        response.setCustomerName(booking.getCustomer().getName());
        response.setRoomId(booking.getRoom().getId());
        response.setRoomNumber(booking.getRoom().getRoomNumber());
        response.setCheckInDate(booking.getCheckInDate());
        response.setCheckOutDate(booking.getCheckOutDate());
        response.setTotalAmount(booking.getTotalAmount());
        response.setBookingStatus(booking.getBookingStatus());
        return response;
    }
}