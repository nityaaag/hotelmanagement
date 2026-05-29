package com.lrms.hotelmanagement.service;

import com.lrms.hotelmanagement.dto.PaymentDTO;
import com.lrms.hotelmanagement.dto.PaymentRequest;
import com.lrms.hotelmanagement.dto.PaymentResponse;
import com.lrms.hotelmanagement.entity.PaymentStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentService {
    PaymentResponse processPayment(PaymentRequest paymentRequest);

    Optional<PaymentDTO> getPaymentById(Long id);

    List<PaymentDTO> getAllPayments();

    List<PaymentDTO> getPaymentsByBooking(Long bookingId);

    List<PaymentDTO> getPaymentsByOrder(Long orderId);

    List<PaymentDTO> getPaymentsByStatus(PaymentStatus status);

    List<PaymentDTO> getPaymentsByCustomer(Long customerId);

    List<PaymentDTO> getPaymentsBetweenDates(LocalDateTime startDate, LocalDateTime endDate);

    PaymentResponse updatePaymentStatus(Long paymentId, PaymentStatus status);

    PaymentResponse refundPayment(Long paymentId);

    Double getTotalPaymentByCustomer(Long customerId);

    Long getTotalPaymentCount(PaymentStatus status);

    void deletePayment(Long paymentId);

    // Billing operations
    Double calculateRoomBill(Long bookingId);

    Double calculateRestaurantBill(Long customerId);

    Double calculateTotalBill(Long customerId);

    String generateInvoiceNumber();
}
