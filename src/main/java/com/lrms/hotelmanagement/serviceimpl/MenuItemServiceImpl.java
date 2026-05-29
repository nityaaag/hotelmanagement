package com.lrms.hotelmanagement.serviceimpl;

import com.lrms.hotelmanagement.dto.MenuItemDTO;
import com.lrms.hotelmanagement.dto.MenuItemRequest;
import com.lrms.hotelmanagement.entity.MenuItem;
import com.lrms.hotelmanagement.exception.MenuItemException;
import com.lrms.hotelmanagement.repository.MenuItemRepository;
import com.lrms.hotelmanagement.service.MenuItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional // Added transactional for write operations
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository menuItemRepository;

    public MenuItemServiceImpl(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    @Transactional // Explicitly transactional for create
    public MenuItemDTO createMenuItem(MenuItemRequest request) {
        MenuItem menuItem = new MenuItem();
        menuItem.setItemName(request.getItemName());
        menuItem.setPrice(request.getPrice());
        menuItem.setCategory(request.getCategory());
        menuItem.setDescription(request.getDescription());
        menuItem.setAvailability(request.getAvailability());

        MenuItem savedItem = menuItemRepository.save(menuItem);
        return convertToDTO(savedItem);
    }

    @Override
    @Transactional(readOnly = true) // Read-only for read operations
    public Optional<MenuItemDTO> getMenuItemById(Long id) {
        return menuItemRepository.findById(id).map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemDTO> getAllMenuItems() {
        return menuItemRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemDTO> getMenuItemsByCategory(String category) {
        return menuItemRepository.findByCategory(category).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemDTO> getAvailableMenuItems() {
        return menuItemRepository.findByAvailability(true).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemDTO> searchMenuItems(String itemName) {
        return menuItemRepository.findByItemNameContainingIgnoreCase(itemName).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MenuItemDTO updateMenuItem(Long id, MenuItemRequest request) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new MenuItemException("Menu item not found"));

        menuItem.setItemName(request.getItemName());
        menuItem.setPrice(request.getPrice());
        menuItem.setCategory(request.getCategory());
        menuItem.setDescription(request.getDescription());
        menuItem.setAvailability(request.getAvailability());

        MenuItem updatedItem = menuItemRepository.save(menuItem);
        return convertToDTO(updatedItem);
    }

    @Override
    @Transactional
    public void deleteMenuItem(Long id) {
        if (!menuItemRepository.existsById(id)) {
            throw new MenuItemException("Menu item not found");
        }
        menuItemRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateItemAvailability(Long id, Boolean availability) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new MenuItemException("Menu item not found"));
        menuItem.setAvailability(availability);
        menuItemRepository.save(menuItem);
    }

    private MenuItemDTO convertToDTO(MenuItem menuItem) {
        return new MenuItemDTO(
                menuItem.getId(),
                menuItem.getItemName(),
                menuItem.getPrice(),
                menuItem.getCategory(),
                menuItem.getDescription(),
                menuItem.getAvailability()
        );
    }
}
