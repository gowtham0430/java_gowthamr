package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Order {
    private int orderId;
    private Customer customer;
    private Map<FoodItem, Integer> items;
    private String status;
    private DeliveryPerson deliveryPerson;
    private String deliveryAddress;
    private String paymentMethod;
    private double subtotal;
    private double deliveryFee;
    private double tax;
    private double totalAmount;
    private LocalDateTime orderTime;
    private LocalDateTime estimatedDeliveryTime;
    private String specialInstructions;
    private String trackingNumber;

    public Order(int orderId, Customer customer) {
        this.orderId = orderId;
        this.customer = customer;
        this.items = new HashMap<>(customer.getCart().getItems());
        this.status = "Pending";
        this.paymentMethod = "Cash on Delivery";
        this.orderTime = LocalDateTime.now();
        this.estimatedDeliveryTime = orderTime.plusMinutes(30);
        this.specialInstructions = "";
        this.trackingNumber = generateTrackingNumber();
        calculateAmounts();
    }

    public Order(int orderId, Customer customer, String deliveryAddress, String paymentMethod, String specialInstructions) {
        this.orderId = orderId;
        this.customer = customer;
        this.items = new HashMap<>(customer.getCart().getItems());
        this.status = "Pending";
        this.deliveryAddress = deliveryAddress;
        this.paymentMethod = paymentMethod;
        this.orderTime = LocalDateTime.now();
        this.estimatedDeliveryTime = orderTime.plusMinutes(30);
        this.specialInstructions = specialInstructions;
        this.trackingNumber = generateTrackingNumber();
        calculateAmounts();
    }

    // Getters
    public int getOrderId() { return orderId; }
    public Customer getCustomer() { return customer; }
    public Map<FoodItem, Integer> getItems() { return items; }
    public String getStatus() { return status; }
    public DeliveryPerson getDeliveryPerson() { return deliveryPerson; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public String getPaymentMethod() { return paymentMethod; }
    public double getSubtotal() { return subtotal; }
    public double getDeliveryFee() { return deliveryFee; }
    public double getTax() { return tax; }
    public double getTotalAmount() { return totalAmount; }
    public LocalDateTime getOrderTime() { return orderTime; }
    public LocalDateTime getEstimatedDeliveryTime() { return estimatedDeliveryTime; }
    public String getSpecialInstructions() { return specialInstructions; }
    public String getTrackingNumber() { return trackingNumber; }

    // Setters
    public void setDeliveryPerson(DeliveryPerson dp) { 
        this.deliveryPerson = dp; 
        if (dp != null) {
            this.estimatedDeliveryTime = orderTime.plusMinutes(30);
        }
    }
    
    public void setStatus(String status) { 
        this.status = status; 
        if ("Delivered".equals(status)) {
            this.estimatedDeliveryTime = LocalDateTime.now();
        }
    }
    
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public void setSpecialInstructions(String specialInstructions) { this.specialInstructions = specialInstructions; }

    // Business logic methods
    private void calculateAmounts() {
        subtotal = 0.0;
        for (Map.Entry<FoodItem, Integer> entry : items.entrySet()) {
            subtotal += entry.getKey().getPrice() * entry.getValue();
        }
        
        // Calculate tax (assuming 10% tax)
        tax = subtotal * 0.10;
        
        // Delivery fee (can be customized per restaurant)
        deliveryFee = 25.0; // Default delivery fee
        
        totalAmount = subtotal + tax + deliveryFee;
    }

    private String generateTrackingNumber() {
        return "ORD" + String.format("%06d", orderId) + orderTime.format(DateTimeFormatter.ofPattern("MMdd"));
    }

    public boolean canBeCancelled() {
        return "Pending".equals(status) || "Confirmed".equals(status);
    }

    public boolean isDelivered() {
        return "Delivered".equals(status);
    }

    public String getFormattedOrderTime() {
        return orderTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
    }

    public String getFormattedDeliveryTime() {
        return estimatedDeliveryTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
    }

    public String getStatusWithEmoji() {
        return status;
    }

    @Override
    public String toString() {
        return String.format("Order{orderId=%d, customer=%s, status='%s', total=%.2f, tracking=%s}", 
                           orderId, customer.getUsername(), status, totalAmount, trackingNumber);
    }

    public String getDetailedString() {
        StringBuilder sb = new StringBuilder();
        sb.append("==========================================\n");
        sb.append("Order ID: ").append(orderId).append("\n");
        sb.append("Tracking Number: ").append(trackingNumber).append("\n");
        sb.append("Customer: ").append(customer.getUsername()).append("\n");
        sb.append("Contact: ").append(customer.getContactNo()).append("\n");
        sb.append("Delivery Address: ").append(deliveryAddress != null ? deliveryAddress : "Not specified").append("\n");
        sb.append("Payment Method: ").append(paymentMethod).append("\n");
        sb.append("Special Instructions: ").append(specialInstructions.isEmpty() ? "None" : specialInstructions).append("\n");
        sb.append("Items:\n");
        
        for (Map.Entry<FoodItem, Integer> entry : items.entrySet()) {
            double itemTotal = entry.getKey().getPrice() * entry.getValue();
            sb.append("   - ").append(entry.getKey().getName())
              .append(" x ").append(entry.getValue())
              .append(" = Rs. ").append(String.format("%.2f", itemTotal)).append("\n");
        }
        
        sb.append("Subtotal: Rs. ").append(String.format("%.2f", subtotal)).append("\n");
        sb.append("Delivery Fee: Rs. ").append(String.format("%.2f", deliveryFee)).append("\n");
        sb.append("Tax (10%): Rs. ").append(String.format("%.2f", tax)).append("\n");
        sb.append("Total Amount: Rs. ").append(String.format("%.2f", totalAmount)).append("\n");
        sb.append("Status: ").append(getStatusWithEmoji()).append("\n");
        sb.append("Order Time: ").append(getFormattedOrderTime()).append("\n");
        sb.append("Estimated Delivery: ").append(getFormattedDeliveryTime()).append("\n");
        
        if (deliveryPerson != null) {
            sb.append("Delivery Person: ").append(deliveryPerson.getName())
              .append(" (").append(deliveryPerson.getContactNo()).append(")\n");
        }
        
        sb.append("==========================================");
        return sb.toString();
    }
}
