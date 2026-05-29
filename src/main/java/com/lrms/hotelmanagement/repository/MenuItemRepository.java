package com.lrms.hotelmanagement.repository;

import com.lrms.hotelmanagement.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByCategory(String category);

    List<MenuItem> findByAvailability(Boolean availability);

    List<MenuItem> findByItemNameContainingIgnoreCase(String itemName);
}
