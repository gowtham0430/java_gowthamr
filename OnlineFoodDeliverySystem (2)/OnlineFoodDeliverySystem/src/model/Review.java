package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Review {
    private int reviewId;
    private Customer customer;
    private Restaurant restaurant;
    private FoodItem foodItem;
    private double rating;
    private String comment;
    private LocalDateTime reviewDate;
    private boolean isVerified;

    public Review(int reviewId, Customer customer, Restaurant restaurant, double rating, String comment) {
        this.reviewId = reviewId;
        this.customer = customer;
        this.restaurant = restaurant;
        this.foodItem = null;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = LocalDateTime.now();
        this.isVerified = false;
    }

    public Review(int reviewId, Customer customer, FoodItem foodItem, double rating, String comment) {
        this.reviewId = reviewId;
        this.customer = customer;
        this.restaurant = null;
        this.foodItem = foodItem;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = LocalDateTime.now();
        this.isVerified = false;
    }

    // Getters
    public int getReviewId() { return reviewId; }
    public Customer getCustomer() { return customer; }
    public Restaurant getRestaurant() { return restaurant; }
    public FoodItem getFoodItem() { return foodItem; }
    public double getRating() { return rating; }
    public String getComment() { return comment; }
    public LocalDateTime getReviewDate() { return reviewDate; }
    public boolean isVerified() { return isVerified; }

    // Setters
    public void setRating(double rating) { 
        if (rating >= 1.0 && rating <= 5.0) {
            this.rating = rating;
        }
    }
    public void setComment(String comment) { this.comment = comment; }
    public void setVerified(boolean verified) { this.isVerified = verified; }

    // Business logic methods
    public boolean isValid() {
        return rating >= 1.0 && rating <= 5.0 && 
               comment != null && !comment.trim().isEmpty();
    }

    public String getRatingStars() {
        int fullStars = (int) rating;
        int halfStar = (rating - fullStars) >= 0.5 ? 1 : 0;
        int emptyStars = 5 - fullStars - halfStar;
        
        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < fullStars; i++) {
            stars.append("⭐");
        }
        if (halfStar > 0) {
            stars.append("✨");
        }
        for (int i = 0; i < emptyStars; i++) {
            stars.append("☆");
        }
        return stars.toString();
    }

    public String getFormattedDate() {
        return reviewDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
    }

    public String getReviewType() {
        if (restaurant != null) return "Restaurant Review";
        if (foodItem != null) return "Food Item Review";
        return "General Review";
    }

    @Override
    public String toString() {
        String target = restaurant != null ? restaurant.getName() : 
                       foodItem != null ? foodItem.getName() : "Unknown";
        return String.format("Review{id=%d, customer=%s, target=%s, rating=%.1f, verified=%s}", 
                           reviewId, customer.getUsername(), target, rating, isVerified);
    }

    public String getDetailedString() {
        StringBuilder sb = new StringBuilder();
        sb.append("📝 Review Details:\n");
        sb.append("Review ID: ").append(reviewId).append("\n");
        sb.append("Customer: ").append(customer.getUsername()).append("\n");
        sb.append("Type: ").append(getReviewType()).append("\n");
        
        if (restaurant != null) {
            sb.append("Restaurant: ").append(restaurant.getName()).append("\n");
        }
        if (foodItem != null) {
            sb.append("Food Item: ").append(foodItem.getName()).append("\n");
        }
        
        sb.append("Rating: ").append(getRatingStars()).append(" (").append(rating).append("/5.0)\n");
        sb.append("Comment: ").append(comment).append("\n");
        sb.append("Date: ").append(getFormattedDate()).append("\n");
        sb.append("Verified: ").append(isVerified ? "✅ Yes" : "❌ No").append("\n");
        return sb.toString();
    }
}
