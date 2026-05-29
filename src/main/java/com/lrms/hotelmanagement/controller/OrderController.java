package com.lrms.hotelmanagement.controller;

import com.lrms.hotelmanagement.dto.OrderDTO;
import com.lrms.hotelmanagement.dto.OrderRequest;
import com.lrms.hotelmanagement.dto.OrderResponse;
import com.lrms.hotelmanagement.entity.OrderStatus;
import com.lrms.hotelmanagement.entity.OrderType;
import com.lrms.hotelmanagement.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        OrderResponse order = orderService.createOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        Optional<OrderDTO> order = orderService.getOrderById(id);
        return order.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderDTO>> getCustomerOrders(@PathVariable Long customerId) {
        List<OrderDTO> orders = orderService.getCustomerOrders(customerId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/customer/{customerId}/history")
    public ResponseEntity<List<OrderDTO>> getOrderHistory(@PathVariable Long customerId) {
        List<OrderDTO> history = orderService.getOrderHistory(customerId);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderDTO>> getOrdersByStatus(@PathVariable OrderStatus status) {
        List<OrderDTO> orders = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<OrderDTO>> getOrdersByType(@PathVariable OrderType type) {
        List<OrderDTO> orders = orderService.getOrdersByType(type);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<OrderDTO>> getRoomServiceOrders(@PathVariable Long bookingId) {
        List<OrderDTO> orders = orderService.getRoomServiceOrders(bookingId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<OrderDTO>> getPendingOrders() {
        List<OrderDTO> orders = orderService.getPendingOrders();
        return ResponseEntity.ok(orders);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {
        OrderResponse updatedOrder = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(updatedOrder);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Long id) {
        OrderResponse cancelledOrder = orderService.cancelOrder(id);
        return ResponseEntity.ok(cancelledOrder);
    }

    @GetMapping("/charge/calculate")
    public ResponseEntity<Double> calculateOrderTotal(
            @RequestParam Long menuItemId,
            @RequestParam Integer quantity) {
        Double total = orderService.calculateOrderTotal(menuItemId, quantity);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/between-dates")
    public ResponseEntity<List<OrderDTO>> getOrdersBetweenDates(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        List<OrderDTO> orders = orderService.getOrdersBetweenDates(startDate, endDate);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/count/status")
    public ResponseEntity<Long> getTotalOrderCount(@RequestParam OrderStatus status) {
        Long count = orderService.getTotalOrderCount(status);
        return ResponseEntity.ok(count);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
