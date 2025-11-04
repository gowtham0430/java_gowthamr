package model;

public class FoodItem {
    private int id;
    private String name;
    private double price;
    private String category;
    private String description;
    private double rating;
    private int reviewCount;
    private boolean isAvailable;
    private int preparationTime; // in minutes

    public FoodItem(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = "General";
        this.description = "Delicious food item";
        this.rating = 0.0;
        this.reviewCount = 0;
        this.isAvailable = true;
        this.preparationTime = 15;
    }

    public FoodItem(int id, String name, double price, String category, String description, int preparationTime) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.description = description;
        this.rating = 0.0;
        this.reviewCount = 0;
        this.isAvailable = true;
        this.preparationTime = preparationTime;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public double getRating() { return rating; }
    public int getReviewCount() { return reviewCount; }
    public boolean isAvailable() { return isAvailable; }
    public int getPreparationTime() { return preparationTime; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    public void setCategory(String category) { this.category = category; }
    public void setDescription(String description) { this.description = description; }
    public void setAvailable(boolean available) { this.isAvailable = available; }
    public void setPreparationTime(int preparationTime) { this.preparationTime = preparationTime; }

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

    @Override
    public String toString() {
        return String.format("FoodItem{id=%d, name='%s', price=%.2f, category='%s', rating=%.1f, available=%s, prepTime=%dmin}", 
                           id, name, price, category, rating, isAvailable, preparationTime);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        FoodItem foodItem = (FoodItem) obj;
        return id == foodItem.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}

