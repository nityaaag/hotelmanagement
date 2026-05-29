package com.lrms.hotelmanagement.dto;

import java.time.LocalDateTime;

public class BillingDTO {

    private Long id;
    private Long customerId;
    private String customerName;
    private String customerEmail;
    private Double roomCharges;
    private Double restaurantCharges;
    private Double totalAmount;
    private Double paidAmount;
    private Double dueAmount;
    private LocalDateTime createdAt;
    private LocalDateTime billedDate;

    public BillingDTO() {
    }

    public BillingDTO(Long id, Long customerId, String customerName, String customerEmail,
                      Double roomCharges, Double restaurantCharges, Double totalAmount,
                      Double paidAmount, Double dueAmount, LocalDateTime createdAt,
                      LocalDateTime billedDate) {
        this.id = id;
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.roomCharges = roomCharges;
        this.restaurantCharges = restaurantCharges;
        this.totalAmount = totalAmount;
        this.paidAmount = paidAmount;
        this.dueAmount = dueAmount;
        this.createdAt = createdAt;
        this.billedDate = billedDate;
    }

    // GETTERS & SETTERS
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public Double getRoomCharges() { return roomCharges; }
    public void setRoomCharges(Double roomCharges) { this.roomCharges = roomCharges; }

    public Double getRestaurantCharges() { return restaurantCharges; }
    public void setRestaurantCharges(Double restaurantCharges) { this.restaurantCharges = restaurantCharges; }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    public Double getPaidAmount() { return paidAmount; }
    public void setPaidAmount(Double paidAmount) { this.paidAmount = paidAmount; }

    public Double getDueAmount() { return dueAmount; }
    public void setDueAmount(Double dueAmount) { this.dueAmount = dueAmount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getBilledDate() { return billedDate; }
    public void setBilledDate(LocalDateTime billedDate) { this.billedDate = billedDate; }
}