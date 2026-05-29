package com.lrms.hotelmanagement.serviceimpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lrms.hotelmanagement.dto.OrderDTO;
import com.lrms.hotelmanagement.dto.OrderRequest;
import com.lrms.hotelmanagement.dto.OrderResponse;
import com.lrms.hotelmanagement.entity.MenuItem;
import com.lrms.hotelmanagement.entity.Order;
import com.lrms.hotelmanagement.entity.OrderStatus;
import com.lrms.hotelmanagement.entity.OrderType;
import com.lrms.hotelmanagement.entity.User;
import com.lrms.hotelmanagement.exception.OrderException;
import com.lrms.hotelmanagement.repository.MenuItemRepository;
import com.lrms.hotelmanagement.repository.OrderRepository;
import com.lrms.hotelmanagement.service.OrderService;
import com.lrms.hotelmanagement.service.UserService;

@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository;
    private final UserService userService;

    public OrderServiceImpl(OrderRepository orderRepository, 
                            MenuItemRepository menuItemRepository, 
                            UserService userService) {
        this.orderRepository = orderRepository;
        this.menuItemRepository = menuItemRepository;
        this.userService = userService;
    }

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {
        // Validate customer
        User customer = userService.getUserById(orderRequest.getCustomerId())
                .orElseThrow(() -> new OrderException("Customer not found"));

        // Validate menu item
        MenuItem menuItem = menuItemRepository.findById(orderRequest.getMenuItemId())
                .orElseThrow(() -> new OrderException("Menu item not found"));

        if (!menuItem.getAvailability()) {
            throw new OrderException("Menu item is not available");
        }

        // Calculate total price
        Double totalPrice = menuItem.getPrice() * orderRequest.getQuantity();

        // Create order
        Order order = new Order();
        order.setCustomer(customer);
        order.setMenuItem(menuItem);
        order.setQuantity(orderRequest.getQuantity());
        order.setTotalPrice(totalPrice);
        order.setOrderType(orderRequest.getOrderType());
        order.setBookingId(orderRequest.getBookingId());
        order.setSpecialRequests(orderRequest.getSpecialRequests());
        order.setOrderStatus(OrderStatus.PENDING);

        Order savedOrder = orderRepository.save(order);
        return convertToOrderResponse(savedOrder);
    }

    @Override
    public Optional<OrderDTO> getOrderById(Long id) {
        return orderRepository.findById(id).map(this::convertToOrderDTO);
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToOrderDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getCustomerOrders(Long customerId) {
        return orderRepository.findByCustomerId(customerId).stream()
                .map(this::convertToOrderDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByOrderStatus(status).stream()
                .map(this::convertToOrderDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getOrdersByType(OrderType type) {
        return orderRepository.findByOrderType(type).stream()
                .map(this::convertToOrderDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getRoomServiceOrders(Long bookingId) {
        return orderRepository.findByBookingId(bookingId).stream()
                .map(this::convertToOrderDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getOrderHistory(Long customerId) {
        return orderRepository.findCustomerOrderHistory(customerId).stream()
                .map(this::convertToOrderDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getPendingOrders() {
        return orderRepository.findPendingOrders().stream()
                .map(this::convertToOrderDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException("Order not found"));

        // Validate status transitions
        if (order.getOrderStatus().equals(OrderStatus.CANCELLED)) {
            throw new OrderException("Cannot update a cancelled order");
        }
        
        if (order.getOrderStatus().equals(OrderStatus.COMPLETED) && !newStatus.equals(OrderStatus.COMPLETED)) {
            throw new OrderException("Cannot change status of a completed order");
        }

        order.setOrderStatus(newStatus);
        Order updatedOrder = orderRepository.save(order);
        return convertToOrderResponse(updatedOrder);
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException("Order not found"));

        if (order.getOrderStatus().equals(OrderStatus.SERVED) || 
            order.getOrderStatus().equals(OrderStatus.COMPLETED)) {
            throw new OrderException("Cannot cancel a served or completed order");
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
        Order cancelledOrder = orderRepository.save(order);
        return convertToOrderResponse(cancelledOrder);
    }

    @Override
    public Double calculateOrderTotal(Long menuItemId, Integer quantity) {
        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new OrderException("Menu item not found"));
        return menuItem.getPrice() * quantity;
    }

    @Override
    public List<OrderDTO> getOrdersBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findOrdersBetweenDates(startDate, endDate).stream()
                .map(this::convertToOrderDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Long getTotalOrderCount(OrderStatus status) {
        return orderRepository.countByOrderStatus(status);
    }

    @Override
    @Transactional
    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException("Order not found"));

        if (order.getOrderStatus().equals(OrderStatus.COMPLETED) || 
            order.getOrderStatus().equals(OrderStatus.SERVED)) {
            throw new OrderException("Cannot delete an order that has already been served or completed. Please cancel it instead if applicable.");
        }

        orderRepository.deleteById(orderId);
    }

    private OrderDTO convertToOrderDTO(Order order) {
        return new OrderDTO(
                order.getId(),
                order.getCustomer().getId(),
                order.getCustomer().getName(),
                order.getMenuItem().getId(),
                order.getMenuItem().getItemName(),
                order.getQuantity(),
                order.getMenuItem().getPrice(),
                order.getTotalPrice(),
                order.getOrderType(),
                order.getOrderStatus(),
                order.getBookingId(),
                order.getSpecialRequests(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }

    private OrderResponse convertToOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getCustomer().getId(),
                order.getCustomer().getName(),
                order.getCustomer().getEmail(),
                order.getMenuItem().getId(),
                order.getMenuItem().getItemName(),
                order.getMenuItem().getCategory(),
                order.getQuantity(),
                order.getMenuItem().getPrice(),
                order.getTotalPrice(),
                order.getOrderType(),
                order.getOrderStatus(),
                order.getBookingId(),
                order.getSpecialRequests(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}
