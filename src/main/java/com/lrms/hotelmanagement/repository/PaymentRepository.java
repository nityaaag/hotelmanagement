package com.lrms.hotelmanagement.repository;

import com.lrms.hotelmanagement.entity.Payment;
import com.lrms.hotelmanagement.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByBookingId(Long bookingId);
    List<Payment> findByOrderId(Long orderId);
    List<Payment> findByPaymentStatus(PaymentStatus status);

    @Query("SELECT p FROM Payment p WHERE p.booking.customer.id = :customerId OR p.order.customer.id = :customerId")
    List<Payment> findPaymentsByCustomerId(@Param("customerId") Long customerId);
    @Query("SELECT p FROM Payment p WHERE p.paymentDate BETWEEN :startDate AND :endDate")
    List<Payment> findPaymentsBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.booking.customer.id = :customerId OR p.order.customer.id = :customerId AND p.paymentStatus = 'COMPLETED'")
    Double getTotalPaymentByCustomer(@Param("customerId") Long customerId);
    Long countByPaymentStatus(PaymentStatus status);
}