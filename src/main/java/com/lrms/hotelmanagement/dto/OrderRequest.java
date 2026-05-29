package com.lrms.hotelmanagement.dto;

import com.lrms.hotelmanagement.entity.OrderType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class OrderRequest {
    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotNull(message = "Menu Item ID is required")
    private Long menuItemId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "Order type is required")
    private OrderType orderType;

    private Long bookingId; // Optional, for room service
    private String specialRequests;

    public OrderRequest() {}

    public OrderRequest(Long customerId, Long menuItemId, Integer quantity, OrderType orderType, Long bookingId, String specialRequests) {
        this.customerId = customerId;
        this.menuItemId = menuItemId;
        this.quantity = quantity;
        this.orderType = orderType;
        this.bookingId = bookingId;
        this.specialRequests = specialRequests;
    }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public Long getMenuItemId() { return menuItemId; }
    public void setMenuItemId(Long menuItemId) { this.menuItemId = menuItemId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public OrderType getOrderType() { return orderType; }
    public void setOrderType(OrderType orderType) { this.orderType = orderType; }

    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }

    public String getSpecialRequests() { return specialRequests; }
    public void setSpecialRequests(String specialRequests) { this.specialRequests = specialRequests; }
}