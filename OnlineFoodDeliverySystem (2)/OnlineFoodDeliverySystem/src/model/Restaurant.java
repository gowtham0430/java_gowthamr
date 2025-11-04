package model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Restaurant {
    private int id;
    private String name;
    private String address;
    private String phoneNumber;
    private String cuisineType;
    private double rating;
    private int reviewCount;
    private boolean isOpen;
    private int deliveryTime; // in minutes
    private double deliveryFee;
    private double minimumOrderAmount;
    private List<FoodItem> menu;

    public Restaurant(int id, String name) {
        this.id = id;
        this.name = name;
        this.address = "Address not specified";
        this.phoneNumber = "0000000000";
        this.cuisineType = "Multi-cuisine";
        this.rating = 0.0;
        this.reviewCount = 0;
        this.isOpen = true;
        this.deliveryTime = 30;
        this.deliveryFee = 0.0;
        this.minimumOrderAmount = 0.0;
        this.menu = new ArrayList<>();
    }

    public Restaurant(int id, String name, String address, String phoneNumber, String cuisineType, 
                     int deliveryTime, double deliveryFee, double minimumOrderAmount) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.cuisineType = cuisineType;
        this.rating = 0.0;
        this.reviewCount = 0;
        this.isOpen = true;
        this.deliveryTime = deliveryTime;
        this.deliveryFee = deliveryFee;
        this.minimumOrderAmount = minimumOrderAmount;
        this.menu = new ArrayList<>();
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getCuisineType() { return cuisineType; }
    public double getRating() { return rating; }
    public int getReviewCount() { return reviewCount; }
    public boolean isOpen() { return isOpen; }
    public int getDeliveryTime() { return deliveryTime; }
    public double getDeliveryFee() { return deliveryFee; }
    public double getMinimumOrderAmount() { return minimumOrderAmount; }
    public List<FoodItem> getMenu() { return menu; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setAddress(String address) { this.address = address; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setCuisineType(String cuisineType) { this.cuisineType = cuisineType; }
    public void setOpen(boolean open) { this.isOpen = open; }
    public void setDeliveryTime(int deliveryTime) { this.deliveryTime = deliveryTime; }
    public void setDeliveryFee(double deliveryFee) { this.deliveryFee = deliveryFee; }
    public void setMinimumOrderAmount(double minimumOrderAmount) { this.minimumOrderAmount = minimumOrderAmount; }

    // Menu management
    public void addFoodItem(FoodItem item) {
        menu.add(item);
    }

    public void removeFoodItem(int id) {
        menu.removeIf(item -> item.getId() == id);
    }

    public FoodItem findFoodItemById(int id) {
        return menu.stream()
                  .filter(item -> item.getId() == id)
                  .findFirst()
                  .orElse(null);
    }

    public List<FoodItem> getFoodItemsByCategory(String category) {
        return menu.stream()
                  .filter(item -> item.getCategory().equalsIgnoreCase(category))
                  .collect(Collectors.toList());
    }

    public List<FoodItem> getAvailableFoodItems() {
        return menu.stream()
                  .filter(FoodItem::isAvailable)
                  .collect(Collectors.toList());
    }

    // Rating methods
    public void addRating(double newRating) {
        if (newRating >= 1.0 && newRating <= 5.0) {
            double totalRating = rating * reviewCount + newRating;
            reviewCount++;
            rating = totalRating / reviewCount;
        }
    }

    public String getRatingDisplay() {
        if (reviewCount == 0) return "No ratings yet";
        return String.format("%.1f/5.0 (%d reviews)", rating, reviewCount);
    }

    // Business logic
    public boolean canAcceptOrder(double orderAmount) {
        return isOpen && orderAmount >= minimumOrderAmount;
    }

    public double calculateTotalWithDelivery(double orderAmount) {
        return orderAmount + deliveryFee;
    }

    @Override
    public String toString() {
        return String.format("Restaurant{id=%d, name='%s', cuisine='%s', rating=%.1f, open=%s, deliveryTime=%dmin}", 
                           id, name, cuisineType, rating, isOpen, deliveryTime);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Restaurant restaurant = (Restaurant) obj;
        return id == restaurant.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
