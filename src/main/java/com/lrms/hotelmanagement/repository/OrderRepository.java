package com.lrms.hotelmanagement.repository;

import com.lrms.hotelmanagement.entity.Order;
import com.lrms.hotelmanagement.entity.OrderStatus;
import com.lrms.hotelmanagement.entity.OrderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // This method already exists in JpaRepository, but explicitly defining for clarity or if custom logic was intended
    List<Order> findByCustomerId(Long customerId);

    List<Order> findByOrderStatus(OrderStatus orderStatus);

    List<Order> findByOrderType(OrderType orderType);

    List<Order> findByBookingId(Long bookingId);

    @Query("SELECT o FROM Order o WHERE o.customer.id = :customerId ORDER BY o.createdAt DESC")
    List<Order> findCustomerOrderHistory(@Param("customerId") Long customerId);

    @Query("SELECT o FROM Order o WHERE o.orderStatus IN ('PENDING', 'CONFIRMED', 'PREPARING', 'READY')")
    List<Order> findPendingOrders();

    @Query("SELECT o FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate")
    List<Order> findOrdersBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.orderStatus = :status")
    Long countByOrderStatus(@Param("status") OrderStatus status);
}
