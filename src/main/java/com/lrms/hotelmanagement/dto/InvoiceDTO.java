package com.lrms.hotelmanagement.dto;


import java.time.LocalDateTime;


public class InvoiceDTO {
    private Long id;
    private Long customerId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String invoiceNumber;
    private Double roomCharges;
    private Double restaurantCharges;
    private Double taxes;
    private Double totalAmount;
    private Double paidAmount;
    private Double dueAmount;
    private String invoiceStatus;
    private LocalDateTime invoiceDate;
    private LocalDateTime dueDate;
}
