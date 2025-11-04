package service;

import model.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AdminService {
    private List<Restaurant> restaurants = new ArrayList<>();
    private List<DeliveryPerson> deliveryPersons = new ArrayList<>();
    private List<Promotion> promotions = new ArrayList<>();
    private List<Review> reviews = new ArrayList<>();
    private List<Order> orders;
    private int nextPromotionId = 1;
    private int nextReviewId = 1;

    public AdminService(List<Order> orders) {
        this.orders = orders;
        initializeSampleData();
    }

    private void initializeSampleData() {
        // Add sample restaurants
        addRestaurant(1, "Pizza Palace", "123 Main St, City", "9876543210", "Italian", 25, 30.0, 200.0);
        addRestaurant(2, "Burger King", "456 Oak Ave, City", "9876543211", "American", 20, 25.0, 150.0);
        addRestaurant(3, "Sushi Master", "789 Pine St, City", "9876543212", "Japanese", 35, 40.0, 300.0);
        
        // Add sample food items
        addFoodItemToRestaurant(1, 1, "Margherita Pizza", 299.0, "Pizza", "Classic tomato and mozzarella", 20);
        addFoodItemToRestaurant(1, 2, "Pepperoni Pizza", 349.0, "Pizza", "Spicy pepperoni with cheese", 25);
        addFoodItemToRestaurant(2, 3, "Chicken Burger", 199.0, "Burger", "Grilled chicken with lettuce and mayo", 15);
        addFoodItemToRestaurant(2, 4, "Beef Burger", 249.0, "Burger", "Juicy beef patty with cheese", 18);
        addFoodItemToRestaurant(3, 5, "California Roll", 299.0, "Sushi", "Crab, avocado, and cucumber", 30);
        addFoodItemToRestaurant(3, 6, "Salmon Sashimi", 399.0, "Sushi", "Fresh salmon slices", 25);
        
        // Add sample delivery persons
        addDeliveryPerson(1, "John Doe", 9876543210L);
        addDeliveryPerson(2, "Jane Smith", 9876543211L);
        addDeliveryPerson(3, "Mike Johnson", 9876543212L);
        
        // Add sample promotions
        addPromotion("WELCOME20", "Welcome Offer", "20% off on first order", 20.0, 0.0, 100.0, 30, 100);
        addPromotion("SAVE50", "Flat Discount", "Rs. 50 off on orders above Rs. 300", 0.0, 50.0, 300.0, 15, 50);
    }

    // Restaurant Management
    public void addRestaurant(int id, String name) {
        restaurants.add(new Restaurant(id, name));
        System.out.println("Restaurant added successfully!");
    }

    public void addRestaurant(int id, String name, String address, String phoneNumber, 
                             String cuisineType, int deliveryTime, double deliveryFee, double minimumOrderAmount) {
        restaurants.add(new Restaurant(id, name, address, phoneNumber, cuisineType, deliveryTime, deliveryFee, minimumOrderAmount));
        System.out.println("Restaurant added successfully with full details!");
    }

    public void updateRestaurant(int id, String name, String address, String phoneNumber, String cuisineType) {
        Restaurant restaurant = findRestaurantById(id);
        if (restaurant != null) {
            restaurant.setName(name);
            restaurant.setAddress(address);
            restaurant.setPhoneNumber(phoneNumber);
            restaurant.setCuisineType(cuisineType);
            System.out.println("Restaurant updated successfully!");
        } else {
            System.out.println("Restaurant not found!");
        }
    }

    public Restaurant findRestaurantById(int id) {
        return restaurants.stream()
                         .filter(r -> r.getId() == id)
                         .findFirst()
                         .orElse(null);
    }

    // Food Item Management
    public void addFoodItemToRestaurant(int restId, int foodId, String name, double price) {
        Restaurant restaurant = findRestaurantById(restId);
        if (restaurant != null) {
            restaurant.addFoodItem(new FoodItem(foodId, name, price));
            System.out.println("Food item added successfully!");
        } else {
            System.out.println("Restaurant not found!");
        }
    }

    public void addFoodItemToRestaurant(int restId, int foodId, String name, double price, 
                                       String category, String description, int preparationTime) {
        Restaurant restaurant = findRestaurantById(restId);
        if (restaurant != null) {
            restaurant.addFoodItem(new FoodItem(foodId, name, price, category, description, preparationTime));
            System.out.println("Food item added successfully with full details!");
        } else {
            System.out.println("Restaurant not found!");
        }
    }

    public void updateFoodItem(int restId, int foodId, String name, double price, String category, String description) {
        Restaurant restaurant = findRestaurantById(restId);
        if (restaurant != null) {
            FoodItem item = restaurant.findFoodItemById(foodId);
            if (item != null) {
                item.setName(name);
                item.setPrice(price);
                item.setCategory(category);
                item.setDescription(description);
                System.out.println("Food item updated successfully!");
            } else {
                System.out.println("Food item not found!");
            }
        } else {
            System.out.println("Restaurant not found!");
        }
    }

    public void removeFoodItemFromRestaurant(int restId, int foodId) {
        Restaurant restaurant = findRestaurantById(restId);
        if (restaurant != null) {
            restaurant.removeFoodItem(foodId);
            System.out.println("Food item removed successfully!");
        } else {
            System.out.println("Restaurant not found!");
        }
    }

    public void toggleFoodItemAvailability(int restId, int foodId) {
        Restaurant restaurant = findRestaurantById(restId);
        if (restaurant != null) {
            FoodItem item = restaurant.findFoodItemById(foodId);
            if (item != null) {
                item.setAvailable(!item.isAvailable());
                System.out.println("Food item availability toggled to: " + item.isAvailable());
            } else {
                System.out.println("Food item not found!");
            }
        } else {
            System.out.println("Restaurant not found!");
        }
    }

    // Restaurant Display
    public void viewRestaurants() {
        if (restaurants.isEmpty()) {
            System.out.println("No restaurants found!");
            return;
        }
        
        System.out.println("Restaurants and Menus:");
        System.out.println("=" + "=".repeat(50));
        
        for (Restaurant r : restaurants) {
            System.out.println("Restaurant ID: " + r.getId());
            System.out.println("Name: " + r.getName());
            System.out.println("Address: " + r.getAddress());
            System.out.println("Phone: " + r.getPhoneNumber());
            System.out.println("Cuisine: " + r.getCuisineType());
            System.out.println("Rating: " + r.getRatingDisplay());
            System.out.println("Status: " + (r.isOpen() ? "Open" : "Closed"));
            System.out.println("Delivery Time: " + r.getDeliveryTime() + " minutes");
            System.out.println("Delivery Fee: Rs. " + String.format("%.2f", r.getDeliveryFee()));
            System.out.println("Min Order: Rs. " + String.format("%.2f", r.getMinimumOrderAmount()));
            System.out.println("Menu:");
            
            if (r.getMenu().isEmpty()) {
                System.out.println("   No items available");
            } else {
                for (FoodItem item : r.getMenu()) {
                    System.out.println("   - " + item.getName() + " - Rs. " + String.format("%.2f", item.getPrice()));
                    System.out.println("     Category: " + item.getCategory() + " | Rating: " + item.getRatingDisplay());
                    System.out.println("     Available: " + (item.isAvailable() ? "Yes" : "No") + " | Prep Time: " + item.getPreparationTime() + "min");
                    System.out.println("     Description: " + item.getDescription());
                    System.out.println();
                }
            }
            System.out.println("-".repeat(50));
        }
    }

    // Delivery Person Management
    public DeliveryPerson findDeliveryPersonById(int id) {
        return deliveryPersons.stream()
                             .filter(d -> d.getDeliveryPersonId() == id)
                             .findFirst()
                             .orElse(null);
    }

    public void addDeliveryPerson(int id, String name, long contact) {
        deliveryPersons.add(new DeliveryPerson(id, name, contact));
        System.out.println("Delivery person added successfully!");
    }

    public void viewDeliveryPersons() {
        if (deliveryPersons.isEmpty()) {
            System.out.println("No delivery persons found!");
            return;
        }
        
        System.out.println("Delivery Persons:");
        System.out.println("=" + "=".repeat(30));
        for (DeliveryPerson dp : deliveryPersons) {
            System.out.println("ID: " + dp.getDeliveryPersonId() + " | Name: " + dp.getName() + " | Contact: " + dp.getContactNo());
        }
    }

    // Promotion Management
    public void addPromotion(String promoCode, String name, String description, double discountPercentage, 
                           double discountAmount, double minimumOrderAmount, int validDays, int maxUses) {
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusDays(validDays);
        
        Promotion promotion;
        if (discountPercentage > 0) {
            promotion = new Promotion(nextPromotionId++, name, description, promoCode, 
                                   discountPercentage, minimumOrderAmount, startDate, endDate, maxUses);
        } else {
            promotion = new Promotion(nextPromotionId++, name, description, promoCode, 
                                   discountAmount, minimumOrderAmount, startDate, endDate, maxUses);
        }
        
        promotions.add(promotion);
        System.out.println("Promotion added successfully!");
    }

    public void viewPromotions() {
        if (promotions.isEmpty()) {
            System.out.println("No promotions found!");
            return;
        }
        
        System.out.println("Active Promotions:");
        System.out.println("=" + "=".repeat(40));
        for (Promotion p : promotions) {
            if (p.isValid()) {
                System.out.println(p.getDetailedString());
                System.out.println("-".repeat(40));
            }
        }
    }

    public Promotion findPromotionByCode(String promoCode) {
        return promotions.stream()
                        .filter(p -> p.getPromoCode().equalsIgnoreCase(promoCode))
                        .findFirst()
                        .orElse(null);
    }

    // Analytics and Reports
    public void viewAnalytics() {
        System.out.println("System Analytics:");
        System.out.println("=" + "=".repeat(30));
        System.out.println("Total Restaurants: " + restaurants.size());
        System.out.println("Total Food Items: " + restaurants.stream().mapToInt(r -> r.getMenu().size()).sum());
        System.out.println("Total Delivery Persons: " + deliveryPersons.size());
        System.out.println("Total Orders: " + orders.size());
        System.out.println("Active Promotions: " + promotions.stream().mapToInt(p -> p.isValid() ? 1 : 0).sum());
        System.out.println("Total Reviews: " + reviews.size());
        
        // Order status breakdown
        Map<String, Long> orderStatusCount = new HashMap<>();
        for (Order order : orders) {
            orderStatusCount.merge(order.getStatus(), 1L, Long::sum);
        }
        
        System.out.println("\nOrder Status Breakdown:");
        orderStatusCount.forEach((status, count) -> 
            System.out.println("   " + status + ": " + count + " orders"));
        
        // Revenue calculation
        double totalRevenue = orders.stream()
                                  .filter(o -> "Delivered".equals(o.getStatus()))
                                  .mapToDouble(Order::getTotalAmount)
                                  .sum();
        System.out.println("\nTotal Revenue: Rs. " + String.format("%.2f", totalRevenue));
    }

    // Getters
    public List<Restaurant> getRestaurants() { return restaurants; }
    public List<DeliveryPerson> getDeliveryPersons() { return deliveryPersons; }
    public List<Promotion> getPromotions() { return promotions; }
    public List<Review> getReviews() { return reviews; }
}
