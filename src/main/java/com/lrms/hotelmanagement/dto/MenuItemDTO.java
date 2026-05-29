package com.lrms.hotelmanagement.dto;

public class MenuItemDTO {
    private Long id;
    private String itemName;
    private Double price;
    private String category;
    private String description;
    private Boolean availability;

    public MenuItemDTO() {}

    public MenuItemDTO(Long id, String itemName, Double price, String category, String description, Boolean availability) {
        this.id = id;
        this.itemName = itemName;
        this.price = price;
        this.category = category;
        this.description = description;
        this.availability = availability;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getAvailability() {
        return availability;
    }

    public void setAvailability(Boolean availability) {
        this.availability = availability;
    }
}