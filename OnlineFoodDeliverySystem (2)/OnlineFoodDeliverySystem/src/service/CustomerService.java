package service;

import model.*;
import java.util.*;
import java.util.stream.Collectors;

public class CustomerService {
    private List<Customer> customers = new ArrayList<>();
    private List<Order> orders;
    private List<Restaurant> restaurants;
    private List<Promotion> promotions;
    private List<Review> reviews;
    private int nextReviewId = 1;

    public CustomerService(List<Order> orders, List<Restaurant> restaurants) {
        this.orders = orders;
        this.restaurants = restaurants;
        this.promotions = new ArrayList<>();
        this.reviews = new ArrayList<>();
    }

    public void setPromotions(List<Promotion> promotions) {
        this.promotions = promotions;
    }

    // Customer Management
    public void addCustomer(int id, String name, long contact) {
        customers.add(new Customer(id, name, contact));
        System.out.println("✅ Customer created successfully!");
    }

    public Customer findCustomerById(int id) {
        return customers.stream().filter(c -> c.getUserId() == id).findFirst().orElse(null);
    }

    public void viewCustomers() {
        if (customers.isEmpty()) {
            System.out.println("❌ No customers found!");
            return;
        }
        
        System.out.println("👥 Registered Customers:");
        System.out.println("=" + "=".repeat(40));
        for (Customer c : customers) {
            System.out.println("ID: " + c.getUserId() + " | Name: " + c.getUsername() + " | Contact: " + c.getContactNo());
        }
    }

    // Restaurant and Menu Browsing
    public void viewRestaurants() {
        if (restaurants.isEmpty()) {
            System.out.println("❌ No restaurants available!");
            return;
        }
        
        System.out.println("🍽️ Available Restaurants:");
        System.out.println("=" + "=".repeat(50));
        
        for (Restaurant r : restaurants) {
            if (r.isOpen()) {
                System.out.println("🏪 Restaurant ID: " + r.getId());
                System.out.println("📛 Name: " + r.getName());
                System.out.println("🍴 Cuisine: " + r.getCuisineType());
                System.out.println("⭐ Rating: " + r.getRatingDisplay());
                System.out.println("🚚 Delivery Time: " + r.getDeliveryTime() + " minutes");
                System.out.println("💰 Delivery Fee: Rs. " + String.format("%.2f", r.getDeliveryFee()));
                System.out.println("💵 Min Order: Rs. " + String.format("%.2f", r.getMinimumOrderAmount()));
                System.out.println();
            }
        }
    }

    public void viewFoodItems() {
        if (restaurants.isEmpty()) {
            System.out.println("❌ No restaurants available!");
            return;
        }
        
        System.out.println("🍽️ Available Food Items:");
        System.out.println("=" + "=".repeat(60));
        
        for (Restaurant r : restaurants) {
            if (r.isOpen() && !r.getAvailableFoodItems().isEmpty()) {
                System.out.println("🏪 " + r.getName() + " (" + r.getCuisineType() + ")");
                System.out.println("⭐ Rating: " + r.getRatingDisplay());
                System.out.println("📋 Menu:");
                
                for (FoodItem item : r.getAvailableFoodItems()) {
                    System.out.println("   • " + item.getName() + " - Rs. " + String.format("%.2f", item.getPrice()));
                    System.out.println("     Category: " + item.getCategory() + " | Rating: " + item.getRatingDisplay());
                    System.out.println("     Prep Time: " + item.getPreparationTime() + "min | Available: ✅");
                    System.out.println("     Description: " + item.getDescription());
                    System.out.println();
                }
                System.out.println("-".repeat(60));
            }
        }
    }

    public void viewFoodItemsByCategory(String category) {
        System.out.println("🍽️ Food Items in Category: " + category);
        System.out.println("=" + "=".repeat(50));
        
        boolean found = false;
        for (Restaurant r : restaurants) {
            if (r.isOpen()) {
                List<FoodItem> categoryItems = r.getFoodItemsByCategory(category);
                if (!categoryItems.isEmpty()) {
                    found = true;
                    System.out.println("🏪 " + r.getName());
                    for (FoodItem item : categoryItems) {
                        if (item.isAvailable()) {
                            System.out.println("   • " + item.getName() + " - Rs. " + String.format("%.2f", item.getPrice()));
                            System.out.println("     Rating: " + item.getRatingDisplay() + " | Prep Time: " + item.getPreparationTime() + "min");
                        }
                    }
                    System.out.println();
                }
            }
        }
        
        if (!found) {
            System.out.println("❌ No items found in this category!");
        }
    }

    public void searchFoodItems(String searchTerm) {
        System.out.println("🔍 Search Results for: " + searchTerm);
        System.out.println("=" + "=".repeat(50));
        
        boolean found = false;
        for (Restaurant r : restaurants) {
            if (r.isOpen()) {
                for (FoodItem item : r.getAvailableFoodItems()) {
                    if (item.getName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(searchTerm.toLowerCase()) ||
                        item.getCategory().toLowerCase().contains(searchTerm.toLowerCase())) {
                        found = true;
                        System.out.println("🏪 " + r.getName());
                        System.out.println("   • " + item.getName() + " - Rs. " + String.format("%.2f", item.getPrice()));
                        System.out.println("     Category: " + item.getCategory() + " | Rating: " + item.getRatingDisplay());
                        System.out.println("     Description: " + item.getDescription());
                        System.out.println();
                    }
                }
            }
        }
        
        if (!found) {
            System.out.println("❌ No items found matching your search!");
        }
    }

    // Cart Management
    public void addFoodToCart(int custId, int restId, int foodId, int qty) {
        Customer customer = findCustomerById(custId);
        if (customer == null) {
            System.out.println("❌ Customer not found!");
            return;
        }
        
        Restaurant restaurant = restaurants.stream()
                                         .filter(r -> r.getId() == restId)
                                         .findFirst()
                                         .orElse(null);
        
        if (restaurant == null) {
            System.out.println("❌ Restaurant not found!");
            return;
        }
        
        FoodItem item = restaurant.findFoodItemById(foodId);
        if (item == null) {
            System.out.println("❌ Food item not found!");
            return;
        }
        
        if (!item.isAvailable()) {
            System.out.println("❌ This item is currently unavailable!");
            return;
        }
        
        if (qty <= 0) {
            System.out.println("❌ Quantity must be greater than 0!");
            return;
        }
        
        customer.getCart().addItem(item, qty);
        System.out.println("✅ " + item.getName() + " x" + qty + " added to cart!");
    }

    public void removeFromCart(int custId, int restId, int foodId) {
        Customer customer = findCustomerById(custId);
        if (customer == null) {
            System.out.println("❌ Customer not found!");
            return;
        }
        
        Restaurant restaurant = restaurants.stream()
                                         .filter(r -> r.getId() == restId)
                                         .findFirst()
                                         .orElse(null);
        
        if (restaurant == null) {
            System.out.println("❌ Restaurant not found!");
            return;
        }
        
        FoodItem item = restaurant.findFoodItemById(foodId);
        if (item == null) {
            System.out.println("❌ Food item not found!");
            return;
        }
        
        customer.getCart().removeItem(item);
        System.out.println("✅ " + item.getName() + " removed from cart!");
    }

    public void viewCart(int custId) {
        Customer c = findCustomerById(custId);
        if (c == null) {
            System.out.println("❌ Customer not found!");
            return;
        }
        
        if (c.getCart().getItems().isEmpty()) {
            System.out.println("🛒 Your cart is empty!");
            return;
        }
        
        System.out.println("🛒 Your Cart:");
        System.out.println("=" + "=".repeat(40));
        System.out.println(c.getCart());
    }

    public void clearCart(int custId) {
        Customer c = findCustomerById(custId);
        if (c == null) {
            System.out.println("❌ Customer not found!");
            return;
        }
        
        c.getCart().getItems().clear();
        System.out.println("✅ Cart cleared successfully!");
    }

    // Order Management
    public Order placeOrder(int custId, String deliveryAddress, String paymentMethod, String specialInstructions) {
        Customer c = findCustomerById(custId);
        if (c == null) {
            System.out.println("❌ Customer not found!");
            return null;
        }
        
        if (c.getCart().getItems().isEmpty()) {
            System.out.println("❌ Your cart is empty! Add items before placing an order.");
            return null;
        }
        
        int orderId = orders.size() + 1;
        Order order = new Order(orderId, c, deliveryAddress, paymentMethod, specialInstructions);
        orders.add(order);
        
        System.out.println("✅ Order placed successfully!");
        System.out.println("🆔 Order ID: " + orderId);
        System.out.println("📋 Tracking Number: " + order.getTrackingNumber());
        System.out.println("💰 Total Amount: Rs. " + String.format("%.2f", order.getTotalAmount()));
        System.out.println("⏰ Estimated Delivery: " + order.getFormattedDeliveryTime());
        
        // Clear cart after successful order
        c.getCart().getItems().clear();
        
        return order;
    }

    public void viewOrders(int custId) {
        List<Order> customerOrders = orders.stream()
                                          .filter(o -> o.getCustomer().getUserId() == custId)
                                          .collect(Collectors.toList());
        
        if (customerOrders.isEmpty()) {
            System.out.println("❌ No orders found for this customer!");
            return;
        }
        
        System.out.println("📦 Your Orders:");
        System.out.println("=" + "=".repeat(50));
        for (Order order : customerOrders) {
            System.out.println(order.getDetailedString());
            System.out.println();
        }
    }

    public void trackOrder(int orderId) {
        Order order = orders.stream()
                           .filter(o -> o.getOrderId() == orderId)
                           .findFirst()
                           .orElse(null);
        
        if (order == null) {
            System.out.println("❌ Order not found!");
            return;
        }
        
        System.out.println("📦 Order Tracking:");
        System.out.println("=" + "=".repeat(40));
        System.out.println("🆔 Order ID: " + order.getOrderId());
        System.out.println("📋 Tracking Number: " + order.getTrackingNumber());
        System.out.println("📦 Status: " + order.getStatusWithEmoji());
        System.out.println("⏰ Order Time: " + order.getFormattedOrderTime());
        System.out.println("🚚 Estimated Delivery: " + order.getFormattedDeliveryTime());
        
        if (order.getDeliveryPerson() != null) {
            System.out.println("👨‍💼 Delivery Person: " + order.getDeliveryPerson().getName());
            System.out.println("📞 Contact: " + order.getDeliveryPerson().getContactNo());
        }
    }

    // Promotion Management
    public void viewPromotions() {
        if (promotions.isEmpty()) {
            System.out.println("❌ No promotions available!");
            return;
        }
        
        System.out.println("🎉 Available Promotions:");
        System.out.println("=" + "=".repeat(40));
        for (Promotion p : promotions) {
            if (p.isValid()) {
                System.out.println("🎫 " + p.getName());
                System.out.println("   Code: " + p.getPromoCode());
                System.out.println("   Discount: " + p.getDiscountDisplay());
                System.out.println("   Min Order: Rs. " + String.format("%.2f", p.getMinimumOrderAmount()));
                System.out.println("   Valid Until: " + p.getFormattedEndDate());
                System.out.println();
            }
        }
    }

    public Promotion findPromotionByCode(String promoCode) {
        return promotions.stream()
                        .filter(p -> p.getPromoCode().equalsIgnoreCase(promoCode) && p.isValid())
                        .findFirst()
                        .orElse(null);
    }

    // Review and Rating System
    public void addReview(int custId, int restaurantId, double rating, String comment) {
        Customer customer = findCustomerById(custId);
        if (customer == null) {
            System.out.println("❌ Customer not found!");
            return;
        }
        
        Restaurant restaurant = restaurants.stream()
                                         .filter(r -> r.getId() == restaurantId)
                                         .findFirst()
                                         .orElse(null);
        
        if (restaurant == null) {
            System.out.println("❌ Restaurant not found!");
            return;
        }
        
        if (rating < 1.0 || rating > 5.0) {
            System.out.println("❌ Rating must be between 1.0 and 5.0!");
            return;
        }
        
        Review review = new Review(nextReviewId++, customer, restaurant, rating, comment);
        reviews.add(review);
        
        // Update restaurant rating
        restaurant.addRating(rating);
        
        System.out.println("✅ Review added successfully!");
        System.out.println("⭐ Rating: " + review.getRatingStars());
    }

    public void addFoodItemReview(int custId, int restaurantId, int foodId, double rating, String comment) {
        Customer customer = findCustomerById(custId);
        if (customer == null) {
            System.out.println("❌ Customer not found!");
            return;
        }
        
        Restaurant restaurant = restaurants.stream()
                                         .filter(r -> r.getId() == restaurantId)
                                         .findFirst()
                                         .orElse(null);
        
        if (restaurant == null) {
            System.out.println("❌ Restaurant not found!");
            return;
        }
        
        FoodItem foodItem = restaurant.findFoodItemById(foodId);
        if (foodItem == null) {
            System.out.println("❌ Food item not found!");
            return;
        }
        
        if (rating < 1.0 || rating > 5.0) {
            System.out.println("❌ Rating must be between 1.0 and 5.0!");
            return;
        }
        
        Review review = new Review(nextReviewId++, customer, foodItem, rating, comment);
        reviews.add(review);
        
        // Update food item rating
        foodItem.addRating(rating);
        
        System.out.println("✅ Food item review added successfully!");
        System.out.println("⭐ Rating: " + review.getRatingStars());
    }

    public void viewReviews(int restaurantId) {
        List<Review> restaurantReviews = reviews.stream()
                                               .filter(r -> r.getRestaurant() != null && r.getRestaurant().getId() == restaurantId)
                                               .collect(Collectors.toList());
        
        if (restaurantReviews.isEmpty()) {
            System.out.println("❌ No reviews found for this restaurant!");
            return;
        }
        
        System.out.println("⭐ Reviews for " + restaurants.stream()
                                                         .filter(r -> r.getId() == restaurantId)
                                                         .findFirst()
                                                         .map(Restaurant::getName)
                                                         .orElse("Unknown Restaurant") + ":");
        System.out.println("=" + "=".repeat(50));
        
        for (Review review : restaurantReviews) {
            System.out.println("👤 " + review.getCustomer().getUsername());
            System.out.println("⭐ " + review.getRatingStars() + " (" + review.getRating() + "/5.0)");
            System.out.println("💬 " + review.getComment());
            System.out.println("📅 " + review.getFormattedDate());
            System.out.println("-".repeat(30));
        }
    }

    // Getters
    public List<Customer> getCustomers() { return customers; }
    public List<Review> getReviews() { return reviews; }
}
