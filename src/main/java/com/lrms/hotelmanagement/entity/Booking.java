package com.lrms.hotelmanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User customer;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    @Positive
    private Double totalAmount;

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    private LocalDate createdAt;
    private LocalDate updatedAt;

    public Booking() {}

    public Booking(User customer, Room room, LocalDate checkInDate, LocalDate checkOutDate, Double totalAmount) {
        this.customer = customer;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalAmount = totalAmount;
        this.bookingStatus = BookingStatus.PENDING;
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
    }

    public Booking(Long id, User customer, Room room, LocalDate checkInDate,
                   LocalDate checkOutDate, Double totalAmount,
                   BookingStatus bookingStatus, LocalDate createdAt) {
        this.id = id;
        this.customer = customer;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalAmount = totalAmount;
        this.bookingStatus = bookingStatus;
        this.createdAt = createdAt;
        this.updatedAt = LocalDate.now(); // Initialize updatedAt
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDate.now();
        if (updatedAt == null) updatedAt = LocalDate.now();
        if (bookingStatus == null) bookingStatus = BookingStatus.PENDING;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDate.now();
    }

    // GETTERS & SETTERS
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getCustomer() { return customer; }
    public void setCustomer(User customer) { this.customer = customer; }

    public Room getRoom() { return room; }
    public void setRoom(Room room) { this.room = room; }

    public LocalDate getCheckInDate() { return checkInDate; }
    public void setCheckInDate(LocalDate checkInDate) { this.checkInDate = checkInDate; }

    public LocalDate getCheckOutDate() { return checkOutDate; }
    public void setCheckOutDate(LocalDate checkOutDate) { this.checkOutDate = checkOutDate; }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    public BookingStatus getBookingStatus() { return bookingStatus; }
    public void setBookingStatus(BookingStatus bookingStatus) { this.bookingStatus = bookingStatus; }

    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }

    public LocalDate getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDate updatedAt) { this.updatedAt = updatedAt; }
}