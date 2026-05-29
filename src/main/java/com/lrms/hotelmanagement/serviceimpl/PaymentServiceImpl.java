package com.lrms.hotelmanagement.serviceimpl;

import com.lrms.hotelmanagement.dto.PaymentDTO;
import com.lrms.hotelmanagement.dto.PaymentRequest;
import com.lrms.hotelmanagement.dto.PaymentResponse;
import com.lrms.hotelmanagement.entity.*;
import com.lrms.hotelmanagement.exception.PaymentException;
import com.lrms.hotelmanagement.repository.BookingRepository;
import com.lrms.hotelmanagement.repository.OrderRepository;
import com.lrms.hotelmanagement.repository.PaymentRepository;
import com.lrms.hotelmanagement.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final OrderRepository orderRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              BookingRepository bookingRepository,
                              OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional
    public PaymentResponse processPayment(PaymentRequest paymentRequest) {
        // Validate amount
        if (paymentRequest.getAmount() <= 0) {
            throw new PaymentException("Payment amount must be greater than 0");
        }

        Payment payment = new Payment();
        payment.setAmount(paymentRequest.getAmount());
        payment.setPaymentMethod(paymentRequest.getPaymentMethod());
        payment.setNotes(paymentRequest.getNotes());
        payment.setPaymentStatus(PaymentStatus.PENDING);

        // Validate booking or order
        if (paymentRequest.getBookingId() != null) {
            Booking booking = bookingRepository.findById(paymentRequest.getBookingId())
                    .orElseThrow(() -> new PaymentException("Booking not found"));
            payment.setBooking(booking);
        } else if (paymentRequest.getOrderId() != null) {
            Order order = orderRepository.findById(paymentRequest.getOrderId())
                    .orElseThrow(() -> new PaymentException("Order not found"));
            payment.setOrder(order);
        } else {
            throw new PaymentException("Either booking or order ID must be provided");
        }

        // Generate transaction ID
        payment.setTransactionId(generateTransactionId());
        payment.setPaymentStatus(PaymentStatus.COMPLETED);
        payment.setPaymentDate(LocalDateTime.now());

        Payment savedPayment = paymentRepository.save(payment);
        return convertToPaymentResponse(savedPayment);
    }

    @Override
    public Optional<PaymentDTO> getPaymentById(Long id) {
        return paymentRepository.findById(id).map(this::convertToPaymentDTO);
    }

    @Override
    public List<PaymentDTO> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(this::convertToPaymentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentDTO> getPaymentsByBooking(Long bookingId) {
        return paymentRepository.findByBookingId(bookingId).stream()
                .map(this::convertToPaymentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentDTO> getPaymentsByOrder(Long orderId) {
        return paymentRepository.findByOrderId(orderId).stream()
                .map(this::convertToPaymentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentDTO> getPaymentsByStatus(PaymentStatus status) {
        return paymentRepository.findByPaymentStatus(status).stream()
                .map(this::convertToPaymentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentDTO> getPaymentsByCustomer(Long customerId) {
        return paymentRepository.findPaymentsByCustomerId(customerId).stream()
                .map(this::convertToPaymentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentDTO> getPaymentsBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return paymentRepository.findPaymentsBetweenDates(startDate, endDate).stream()
                .map(this::convertToPaymentDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PaymentResponse updatePaymentStatus(Long paymentId, PaymentStatus status) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentException("Payment not found"));

        if (payment.getPaymentStatus().equals(PaymentStatus.REFUNDED)) {
            throw new PaymentException("Cannot update a refunded payment");
        }

        payment.setPaymentStatus(status);
        Payment updatedPayment = paymentRepository.save(payment);
        return convertToPaymentResponse(updatedPayment);
    }

    @Override
    @Transactional
    public PaymentResponse refundPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentException("Payment not found"));

        if (!payment.getPaymentStatus().equals(PaymentStatus.COMPLETED)) {
            throw new PaymentException("Only completed payments can be refunded");
        }

        payment.setPaymentStatus(PaymentStatus.REFUNDED);
        Payment refundedPayment = paymentRepository.save(payment);
        return convertToPaymentResponse(refundedPayment);
    }

    @Override
    public Double getTotalPaymentByCustomer(Long customerId) {
        Double total = paymentRepository.getTotalPaymentByCustomer(customerId);
        return total != null ? total : 0.0;
    }

    @Override
    public Long getTotalPaymentCount(PaymentStatus status) {
        return paymentRepository.countByPaymentStatus(status);
    }

    @Override
    @Transactional
    public void deletePayment(Long paymentId) {
        if (!paymentRepository.existsById(paymentId)) {
            throw new PaymentException("Payment not found");
        }
        paymentRepository.deleteById(paymentId);
    }

    @Override
    public Double calculateRoomBill(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new PaymentException("Booking not found"));
        return booking.getTotalAmount();
    }

    @Override
    public Double calculateRestaurantBill(Long customerId) {
        List<Order> orders = orderRepository.findByCustomerId(customerId);
        return orders.stream()
                .filter(o -> o.getOrderStatus().equals(OrderStatus.COMPLETED) || 
                           o.getOrderStatus().equals(OrderStatus.SERVED))
                .mapToDouble(Order::getTotalPrice)
                .sum();
    }

    @Override
    public Double calculateTotalBill(Long customerId) {
        return getTotalPaymentByCustomer(customerId);
    }

    @Override
    public String generateInvoiceNumber() {
        return "INV-" + LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd")) + "-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
    }

    // Helper methods
    private PaymentDTO convertToPaymentDTO(Payment payment) {
        return new PaymentDTO(
                payment.getId(),
                payment.getBooking() != null ? payment.getBooking().getId() : null,
                payment.getOrder() != null ? payment.getOrder().getId() : null,
                payment.getAmount(),
                payment.getPaymentMethod(),
                payment.getPaymentStatus(),
                payment.getTransactionId(),
                payment.getPaymentDate(),
                payment.getNotes(),
                payment.getCreatedAt()
        );
    }

    private PaymentResponse convertToPaymentResponse(Payment payment) {
        String customerName = null;
        String customerEmail = null;

        if (payment.getBooking() != null) {
            customerName = payment.getBooking().getCustomer().getName();
            customerEmail = payment.getBooking().getCustomer().getEmail();
        } else if (payment.getOrder() != null) {
            customerName = payment.getOrder().getCustomer().getName();
            customerEmail = payment.getOrder().getCustomer().getEmail();
        }

        return new PaymentResponse(
                payment.getId(),
                payment.getBooking() != null ? payment.getBooking().getId() : null,
                customerName,
                customerEmail,
                payment.getOrder() != null ? payment.getOrder().getId() : null,
                payment.getAmount(),
                payment.getPaymentMethod(),
                payment.getPaymentStatus(),
                payment.getTransactionId(),
                payment.getPaymentDate(),
                payment.getNotes(),
                payment.getCreatedAt()
        );
    }

    private String generateTransactionId() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
