package com.lrms.hotelmanagement.controller;

import com.lrms.hotelmanagement.dto.PaymentDTO;
import com.lrms.hotelmanagement.dto.PaymentRequest;
import com.lrms.hotelmanagement.dto.PaymentResponse;
import com.lrms.hotelmanagement.entity.PaymentStatus;
import com.lrms.hotelmanagement.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> processPayment(@Valid @RequestBody PaymentRequest paymentRequest) {
        PaymentResponse payment = paymentService.processPayment(paymentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(payment);
    }

    @GetMapping
    public ResponseEntity<List<PaymentDTO>> getAllPayments() {
        List<PaymentDTO> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO> getPaymentById(@PathVariable Long id) {
        Optional<PaymentDTO> payment = paymentService.getPaymentById(id);
        return payment.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<PaymentDTO>> getPaymentsByBooking(@PathVariable Long bookingId) {
        List<PaymentDTO> payments = paymentService.getPaymentsByBooking(bookingId);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<PaymentDTO>> getPaymentsByOrder(@PathVariable Long orderId) {
        List<PaymentDTO> payments = paymentService.getPaymentsByOrder(orderId);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PaymentDTO>> getPaymentsByStatus(@PathVariable PaymentStatus status) {
        List<PaymentDTO> payments = paymentService.getPaymentsByStatus(status);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<PaymentDTO>> getPaymentsByCustomer(@PathVariable Long customerId) {
        List<PaymentDTO> payments = paymentService.getPaymentsByCustomer(customerId);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/between-dates")
    public ResponseEntity<List<PaymentDTO>> getPaymentsBetweenDates(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        List<PaymentDTO> payments = paymentService.getPaymentsBetweenDates(startDate, endDate);
        return ResponseEntity.ok(payments);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PaymentResponse> updatePaymentStatus(
            @PathVariable Long id,
            @RequestParam PaymentStatus status) {
        PaymentResponse updatedPayment = paymentService.updatePaymentStatus(id, status);
        return ResponseEntity.ok(updatedPayment);
    }

    @PostMapping("/{id}/refund")
    public ResponseEntity<PaymentResponse> refundPayment(@PathVariable Long id) {
        PaymentResponse refundedPayment = paymentService.refundPayment(id);
        return ResponseEntity.ok(refundedPayment);
    }

    @GetMapping("/customer/{customerId}/total")
    public ResponseEntity<Double> getTotalPaymentByCustomer(@PathVariable Long customerId) {
        Double total = paymentService.getTotalPaymentByCustomer(customerId);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/count/status")
    public ResponseEntity<Long> getTotalPaymentCount(@RequestParam PaymentStatus status) {
        Long count = paymentService.getTotalPaymentCount(status);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/billing/room-bill")
    public ResponseEntity<Double> calculateRoomBill(@RequestParam Long bookingId) {
        Double roomBill = paymentService.calculateRoomBill(bookingId);
        return ResponseEntity.ok(roomBill);
    }

    @GetMapping("/billing/restaurant-bill")
    public ResponseEntity<Double> calculateRestaurantBill(@RequestParam Long customerId) {
        Double restaurantBill = paymentService.calculateRestaurantBill(customerId);
        return ResponseEntity.ok(restaurantBill);
    }

    @GetMapping("/billing/total-bill")
    public ResponseEntity<Double> calculateTotalBill(@RequestParam Long customerId) {
        Double totalBill = paymentService.calculateTotalBill(customerId);
        return ResponseEntity.ok(totalBill);
    }

    @GetMapping("/invoice/generate-number")
    public ResponseEntity<String> generateInvoiceNumber() {
        String invoiceNumber = paymentService.generateInvoiceNumber();
        return ResponseEntity.ok(invoiceNumber);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}
