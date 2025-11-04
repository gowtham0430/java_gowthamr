package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Promotion {
    private int promotionId;
    private String name;
    private String description;
    private String promoCode;
    private double discountPercentage;
    private double discountAmount;
    private double minimumOrderAmount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean isActive;
    private int maxUses;
    private int currentUses;
    private String applicableRestaurants; // "ALL" or specific restaurant IDs

    public Promotion(int promotionId, String name, String description, String promoCode, 
                    double discountPercentage, double minimumOrderAmount, 
                    LocalDateTime startDate, LocalDateTime endDate, int maxUses) {
        this.promotionId = promotionId;
        this.name = name;
        this.description = description;
        this.promoCode = promoCode;
        this.discountPercentage = discountPercentage;
        this.discountAmount = 0.0;
        this.minimumOrderAmount = minimumOrderAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = true;
        this.maxUses = maxUses;
        this.currentUses = 0;
        this.applicableRestaurants = "ALL";
    }

    public Promotion(int promotionId, String name, String description, String promoCode, 
                    double discountAmount, double minimumOrderAmount, 
                    LocalDateTime startDate, LocalDateTime endDate, int maxUses) {
        this.promotionId = promotionId;
        this.name = name;
        this.description = description;
        this.promoCode = promoCode;
        this.discountPercentage = 0.0;
        this.discountAmount = discountAmount;
        this.minimumOrderAmount = minimumOrderAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = true;
        this.maxUses = maxUses;
        this.currentUses = 0;
        this.applicableRestaurants = "ALL";
    }

    // Getters
    public int getPromotionId() { return promotionId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getPromoCode() { return promoCode; }
    public double getDiscountPercentage() { return discountPercentage; }
    public double getDiscountAmount() { return discountAmount; }
    public double getMinimumOrderAmount() { return minimumOrderAmount; }
    public LocalDateTime getStartDate() { return startDate; }
    public LocalDateTime getEndDate() { return endDate; }
    public boolean isActive() { return isActive; }
    public int getMaxUses() { return maxUses; }
    public int getCurrentUses() { return currentUses; }
    public String getApplicableRestaurants() { return applicableRestaurants; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setPromoCode(String promoCode) { this.promoCode = promoCode; }
    public void setDiscountPercentage(double discountPercentage) { this.discountPercentage = discountPercentage; }
    public void setDiscountAmount(double discountAmount) { this.discountAmount = discountAmount; }
    public void setMinimumOrderAmount(double minimumOrderAmount) { this.minimumOrderAmount = minimumOrderAmount; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }
    public void setActive(boolean active) { this.isActive = active; }
    public void setMaxUses(int maxUses) { this.maxUses = maxUses; }
    public void setApplicableRestaurants(String applicableRestaurants) { this.applicableRestaurants = applicableRestaurants; }

    // Business logic methods
    public boolean isValid() {
        LocalDateTime now = LocalDateTime.now();
        return isActive && 
               now.isAfter(startDate) && 
               now.isBefore(endDate) && 
               currentUses < maxUses;
    }

    public boolean isApplicable(double orderAmount, int restaurantId) {
        if (!isValid()) return false;
        if (orderAmount < minimumOrderAmount) return false;
        if (!"ALL".equals(applicableRestaurants) && 
            !applicableRestaurants.contains(String.valueOf(restaurantId))) {
            return false;
        }
        return true;
    }

    public double calculateDiscount(double orderAmount) {
        if (!isValid()) return 0.0;
        
        if (discountPercentage > 0) {
            double discount = orderAmount * (discountPercentage / 100.0);
            return Math.min(discount, orderAmount * 0.5); // Max 50% discount
        } else {
            return Math.min(discountAmount, orderAmount);
        }
    }

    public boolean applyPromo() {
        if (isValid()) {
            currentUses++;
            return true;
        }
        return false;
    }

    public String getFormattedStartDate() {
        return startDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
    }

    public String getFormattedEndDate() {
        return endDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
    }

    public String getDiscountDisplay() {
        if (discountPercentage > 0) {
            return discountPercentage + "% OFF";
        } else {
            return "Rs. " + String.format("%.2f", discountAmount) + " OFF";
        }
    }

    @Override
    public String toString() {
        return String.format("Promotion{id=%d, name='%s', code='%s', discount=%s, valid=%s}", 
                           promotionId, name, promoCode, getDiscountDisplay(), isValid());
    }

    public String getDetailedString() {
        StringBuilder sb = new StringBuilder();
        sb.append("🎉 Promotion Details:\n");
        sb.append("Name: ").append(name).append("\n");
        sb.append("Description: ").append(description).append("\n");
        sb.append("Promo Code: ").append(promoCode).append("\n");
        sb.append("Discount: ").append(getDiscountDisplay()).append("\n");
        sb.append("Minimum Order: Rs. ").append(String.format("%.2f", minimumOrderAmount)).append("\n");
        sb.append("Valid From: ").append(getFormattedStartDate()).append("\n");
        sb.append("Valid Until: ").append(getFormattedEndDate()).append("\n");
        sb.append("Uses: ").append(currentUses).append("/").append(maxUses).append("\n");
        sb.append("Status: ").append(isValid() ? "✅ Active" : "❌ Inactive").append("\n");
        return sb.toString();
    }
}
