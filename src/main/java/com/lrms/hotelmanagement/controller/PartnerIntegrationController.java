package com.lrms.hotelmanagement.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lrms.hotelmanagement.dto.BookingDTO;
import com.lrms.hotelmanagement.dto.BookingRequest;
import com.lrms.hotelmanagement.dto.BookingResponse;
import com.lrms.hotelmanagement.dto.OrderDTO;
import com.lrms.hotelmanagement.dto.OrderRequest;
import com.lrms.hotelmanagement.dto.OrderResponse;
import com.lrms.hotelmanagement.entity.BookingStatus;
import com.lrms.hotelmanagement.entity.OrderStatus;
import com.lrms.hotelmanagement.service.ApiUsageService;
import com.lrms.hotelmanagement.service.BookingService;
import com.lrms.hotelmanagement.service.OrderService;

@RestController
@RequestMapping("/api/v1/partners")
public class PartnerIntegrationController {

    private final BookingService bookingService;
    private final OrderService orderService;
    private final ApiUsageService usageService;

    public PartnerIntegrationController(BookingService bookingService, 
                                        OrderService orderService, 
                                        ApiUsageService usageService) {
        this.bookingService = bookingService;
        this.orderService = orderService;
        this.usageService = usageService;
    }

    @GetMapping("/bookings")
    public ResponseEntity<List<BookingDTO>> listBookings(@RequestHeader("X-Partner-Id") String partnerId) {
        usageService.logUsage(partnerId, "/api/v1/partners/bookings", "GET");
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @PostMapping("/bookings")
    public ResponseEntity<BookingResponse> createBooking(@RequestHeader("X-Partner-Id") String partnerId, 
                                                       @RequestBody BookingRequest request) {
        usageService.logUsage(partnerId, "/api/v1/partners/bookings", "POST");
        return ResponseEntity.ok(bookingService.createBooking(request));
    }

    @PatchMapping("/bookings/{id}/status")
    public ResponseEntity<BookingResponse> updateBookingStatus(@RequestHeader("X-Partner-Id") String partnerId,
                                                             @PathVariable Long id,
                                                             @RequestParam BookingStatus status) {
        usageService.logUsage(partnerId, "/api/v1/partners/bookings/" + id + "/status", "PATCH");
        return ResponseEntity.ok(bookingService.updateBookingStatus(id, status));
    }

    @DeleteMapping("/bookings/{id}")
    public ResponseEntity<Void> cancelBooking(@RequestHeader("X-Partner-Id") String partnerId,
                                            @PathVariable Long id) {
        usageService.logUsage(partnerId, "/api/v1/partners/bookings/" + id, "DELETE");
        bookingService.cancelBooking(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderDTO>> listOrders(@RequestHeader("X-Partner-Id") String partnerId) {
        usageService.logUsage(partnerId, "/api/v1/partners/orders", "GET");
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PostMapping("/orders")
    public ResponseEntity<OrderResponse> createOrder(@RequestHeader("X-Partner-Id") String partnerId,
                                                   @RequestBody OrderRequest request) {
        usageService.logUsage(partnerId, "/api/v1/partners/orders", "POST");
        return ResponseEntity.ok(orderService.createOrder(request));
    }

    @PatchMapping("/orders/{id}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(@RequestHeader("X-Partner-Id") String partnerId,
                                                         @PathVariable Long id,
                                                         @RequestParam OrderStatus status) {
        usageService.logUsage(partnerId, "/api/v1/partners/orders/" + id + "/status", "PATCH");
        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    }

    @DeleteMapping("/orders/{id}")
    public ResponseEntity<OrderResponse> cancelOrder(@RequestHeader("X-Partner-Id") String partnerId,
                                                   @PathVariable Long id) {
        usageService.logUsage(partnerId, "/api/v1/partners/orders/" + id, "DELETE");
        return ResponseEntity.ok(orderService.cancelOrder(id));
    }

    @GetMapping("/dashboard/usage")
    public ResponseEntity<Map<String, Long>> getUsageDashboard() {
        return ResponseEntity.ok(
            usageService.getAllUsage().stream()
                .collect(Collectors.groupingBy(usage -> usage.getPartnerName() + " (" + usage.getMethod() + " " + usage.getEndpoint() + ")", Collectors.counting()))
        );
    }
}