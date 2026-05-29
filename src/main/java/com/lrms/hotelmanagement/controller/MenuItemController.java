package com.lrms.hotelmanagement.controller;

import com.lrms.hotelmanagement.dto.MenuItemDTO;
import com.lrms.hotelmanagement.dto.MenuItemRequest;
import com.lrms.hotelmanagement.service.MenuItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/menu-items")
@CrossOrigin(origins = "*")
public class MenuItemController {

    @Autowired
    private MenuItemService menuItemService;

    @PostMapping
    public ResponseEntity<MenuItemDTO> createMenuItem(@Valid @RequestBody MenuItemRequest request) {
        MenuItemDTO menuItem = menuItemService.createMenuItem(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(menuItem);
    }

    @GetMapping
    public ResponseEntity<List<MenuItemDTO>> getAllMenuItems() {
        List<MenuItemDTO> items = menuItemService.getAllMenuItems();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItemDTO> getMenuItemById(@PathVariable Long id) {
        Optional<MenuItemDTO> item = menuItemService.getMenuItemById(id);
        return item.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<MenuItemDTO>> getMenuItemsByCategory(@PathVariable String category) {
        List<MenuItemDTO> items = menuItemService.getMenuItemsByCategory(category);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/available")
    public ResponseEntity<List<MenuItemDTO>> getAvailableMenuItems() {
        List<MenuItemDTO> items = menuItemService.getAvailableMenuItems();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/search")
    public ResponseEntity<List<MenuItemDTO>> searchMenuItems(@RequestParam String itemName) {
        List<MenuItemDTO> items = menuItemService.searchMenuItems(itemName);
        return ResponseEntity.ok(items);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuItemDTO> updateMenuItem(
            @PathVariable Long id,
            @Valid @RequestBody MenuItemRequest request) {
        MenuItemDTO updatedItem = menuItemService.updateMenuItem(id, request);
        return ResponseEntity.ok(updatedItem);
    }

    @PatchMapping("/{id}/availability")
    public ResponseEntity<Void> updateItemAvailability(
            @PathVariable Long id,
            @RequestParam Boolean availability) {
        menuItemService.updateItemAvailability(id, availability);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        menuItemService.deleteMenuItem(id);
        return ResponseEntity.noContent().build();
    }
}
