package com.lrms.hotelmanagement.service;

import com.lrms.hotelmanagement.dto.OrderDTO;
import com.lrms.hotelmanagement.dto.OrderRequest;
import com.lrms.hotelmanagement.dto.OrderResponse;
import com.lrms.hotelmanagement.entity.OrderStatus;
import com.lrms.hotelmanagement.entity.OrderType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderService {
    OrderResponse createOrder(OrderRequest orderRequest);
    Optional<OrderDTO> getOrderById(Long id);
    List<OrderDTO> getAllOrders();
    List<OrderDTO> getCustomerOrders(Long customerId);
    List<OrderDTO> getOrdersByStatus(OrderStatus status);
    List<OrderDTO> getOrdersByType(OrderType type);
    List<OrderDTO> getRoomServiceOrders(Long bookingId);
    List<OrderDTO> getOrderHistory(Long customerId);
    List<OrderDTO> getPendingOrders();
    OrderResponse updateOrderStatus(Long orderId, OrderStatus newStatus);
    OrderResponse cancelOrder(Long orderId);
    Double calculateOrderTotal(Long menuItemId, Integer quantity);
    List<OrderDTO> getOrdersBetweenDates(LocalDateTime startDate, LocalDateTime endDate);
    Long getTotalOrderCount(OrderStatus status);
    void deleteOrder(Long orderId);
}