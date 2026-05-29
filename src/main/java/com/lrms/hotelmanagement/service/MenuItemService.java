package com.lrms.hotelmanagement.service;

import com.lrms.hotelmanagement.dto.MenuItemDTO;
import com.lrms.hotelmanagement.dto.MenuItemRequest;

import java.util.List;
import java.util.Optional;

public interface MenuItemService {
    MenuItemDTO createMenuItem(MenuItemRequest request);
    Optional<MenuItemDTO> getMenuItemById(Long id);
    List<MenuItemDTO> getAllMenuItems();
    List<MenuItemDTO> getMenuItemsByCategory(String category);
    List<MenuItemDTO> getAvailableMenuItems();
    List<MenuItemDTO> searchMenuItems(String itemName);
    MenuItemDTO updateMenuItem(Long id, MenuItemRequest request);
    void deleteMenuItem(Long id);
    void updateItemAvailability(Long id, Boolean availability);
}