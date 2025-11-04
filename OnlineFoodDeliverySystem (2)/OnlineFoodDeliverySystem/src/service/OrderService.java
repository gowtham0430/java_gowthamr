package service;

import model.*;
import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OrderService {
    private List<Order> orders;

    public OrderService(List<Order> orders) {
        this.orders = orders;
    }

    // Order Management
    public Order placeOrder(Customer customer) {
        if (customer.getCart().getItems().isEmpty()) {
            System.out.println("Your cart is empty. Add items before placing an order.");
            return null;
        }
        int orderId = orders.size() + 1;
        Order order = new Order(orderId, customer);
        orders.add(order);
        System.out.println("Order placed successfully! Order ID: " + orderId);
        displayOrderSummary(order);
        return order;
    }

    public Order placeOrder(Customer customer, String deliveryAddress, String paymentMethod, String specialInstructions) {
        if (customer.getCart().getItems().isEmpty()) {
            System.out.println("Your cart is empty. Add items before placing an order.");
            return null;
        }
        int orderId = orders.size() + 1;
        Order order = new Order(orderId, customer, deliveryAddress, paymentMethod, specialInstructions);
        orders.add(order);
        System.out.println("Order placed successfully! Order ID: " + orderId);
        displayOrderSummary(order);
        return order;
    }

    // Order Status Management
    public void updateOrderStatus(int orderId, String newStatus) {
        Order order = findOrderById(orderId);
        if (order == null) {
            System.out.println("Order not found!");
            return;
        }
        
        String oldStatus = order.getStatus();
        order.setStatus(newStatus);
        System.out.println("Order status updated from '" + oldStatus + "' to '" + newStatus + "'");
        
        // Send notification based on status
        sendStatusNotification(order, newStatus);
    }

    public void confirmOrder(int orderId) {
        updateOrderStatus(orderId, "Confirmed");
    }

    public void startPreparing(int orderId) {
        updateOrderStatus(orderId, "Preparing");
    }

    public void markOutForDelivery(int orderId) {
        updateOrderStatus(orderId, "Out for Delivery");
    }

    public void markDelivered(int orderId) {
        updateOrderStatus(orderId, "Delivered");
    }

    // Delivery Management
    public void assignDeliveryPerson(int orderId, DeliveryPerson dp) {
        Order order = findOrderById(orderId);
        if (order == null) {
            System.out.println("Order not found!");
            return;
        }
        
        if (dp == null) {
            System.out.println("Delivery person not found!");
            return;
        }
        
        order.setDeliveryPerson(dp);
        order.setStatus("Out for Delivery");
        System.out.println("Delivery Person " + dp.getName() + " assigned to Order ID: " + orderId);
        System.out.println("Contact: " + dp.getContactNo());
    }

    // Order Viewing and Tracking
    public void viewAllOrders() {
        if (orders.isEmpty()) {
            System.out.println("No orders found!");
            return;
        }
        
        System.out.println("All Orders:");
        System.out.println("=" + "=".repeat(60));
        
        // Sort orders by order time (newest first)
        List<Order> sortedOrders = orders.stream()
                                        .sorted((o1, o2) -> o2.getOrderTime().compareTo(o1.getOrderTime()))
                                        .collect(Collectors.toList());
        
        for (Order order : sortedOrders) {
            displayOrderSummary(order);
            System.out.println();
        }
    }

    public void viewOrdersByCustomer(int customerId) {
        List<Order> customerOrders = orders.stream()
                                          .filter(o -> o.getCustomer().getUserId() == customerId)
                                          .sorted((o1, o2) -> o2.getOrderTime().compareTo(o1.getOrderTime()))
                                          .collect(Collectors.toList());
        
        if (customerOrders.isEmpty()) {
            System.out.println("No orders found for this customer.");
            return;
        }
        
        System.out.println("Orders for Customer ID " + customerId + ":");
        System.out.println("=" + "=".repeat(50));
        for (Order order : customerOrders) {
            displayOrderSummary(order);
            System.out.println();
        }
    }

    public void viewOrdersByStatus(String status) {
        List<Order> statusOrders = orders.stream()
                                        .filter(o -> o.getStatus().equalsIgnoreCase(status))
                                        .sorted((o1, o2) -> o2.getOrderTime().compareTo(o1.getOrderTime()))
                                        .collect(Collectors.toList());
        
        if (statusOrders.isEmpty()) {
            System.out.println("No orders found with status: " + status);
            return;
        }
        
        System.out.println("Orders with Status: " + status);
        System.out.println("=" + "=".repeat(50));
        for (Order order : statusOrders) {
            displayOrderSummary(order);
            System.out.println();
        }
    }

    public void trackOrder(int orderId) {
        Order order = findOrderById(orderId);
        if (order == null) {
            System.out.println("Order not found!");
            return;
        }
        
        System.out.println("Order Tracking:");
        System.out.println("=" + "=".repeat(40));
        System.out.println("Order ID: " + order.getOrderId());
        System.out.println("Tracking Number: " + order.getTrackingNumber());
        System.out.println("Customer: " + order.getCustomer().getUsername());
        System.out.println("Contact: " + order.getCustomer().getContactNo());
        System.out.println("Delivery Address: " + order.getDeliveryAddress());
        System.out.println("Status: " + order.getStatusWithEmoji());
        System.out.println("Order Time: " + order.getFormattedOrderTime());
        System.out.println("Estimated Delivery: " + order.getFormattedDeliveryTime());
        
        if (order.getDeliveryPerson() != null) {
            System.out.println("Delivery Person: " + order.getDeliveryPerson().getName());
            System.out.println("Delivery Contact: " + order.getDeliveryPerson().getContactNo());
        }
        
        System.out.println("Items:");
        for (Map.Entry<FoodItem, Integer> entry : order.getItems().entrySet()) {
            double itemTotal = entry.getKey().getPrice() * entry.getValue();
            System.out.println("   - " + entry.getKey().getName() + " x " + entry.getValue() + 
                             " = Rs. " + String.format("%.2f", itemTotal));
        }
        
        System.out.println("Subtotal: Rs. " + String.format("%.2f", order.getSubtotal()));
        System.out.println("Delivery Fee: Rs. " + String.format("%.2f", order.getDeliveryFee()));
        System.out.println("Tax: Rs. " + String.format("%.2f", order.getTax()));
        System.out.println("Total: Rs. " + String.format("%.2f", order.getTotalAmount()));
    }

    // Order Cancellation
    public void cancelOrder(int orderId) {
        Order order = findOrderById(orderId);
        if (order == null) {
            System.out.println("Order not found!");
            return;
        }
        
        if (order.isDelivered()) {
            System.out.println("Cannot cancel a delivered order!");
            return;
        }
        
        if (!order.canBeCancelled()) {
            System.out.println("Cannot cancel order with status: " + order.getStatus());
            return;
        }
        
        order.setStatus("Cancelled");
        System.out.println("Order ID " + orderId + " has been cancelled.");
        System.out.println("Refund will be processed within 3-5 business days.");
    }

    // Analytics and Reports
    public void viewOrderAnalytics() {
        if (orders.isEmpty()) {
            System.out.println("No orders found for analytics!");
            return;
        }
        
        System.out.println("Order Analytics:");
        System.out.println("=" + "=".repeat(40));
        
        // Total orders
        System.out.println("Total Orders: " + orders.size());
        
        // Status breakdown
        Map<String, Long> statusCount = orders.stream()
                                            .collect(Collectors.groupingBy(Order::getStatus, Collectors.counting()));
        
        System.out.println("\nOrder Status Breakdown:");
        statusCount.forEach((status, count) -> 
            System.out.println("   " + status + ": " + count + " orders"));
        
        // Revenue calculation
        double totalRevenue = orders.stream()
                                  .filter(o -> "Delivered".equals(o.getStatus()))
                                  .mapToDouble(Order::getTotalAmount)
                                  .sum();
        
        double pendingRevenue = orders.stream()
                                    .filter(o -> !"Delivered".equals(o.getStatus()) && !"Cancelled".equals(o.getStatus()))
                                    .mapToDouble(Order::getTotalAmount)
                                    .sum();
        
        System.out.println("\nRevenue Analysis:");
        System.out.println("   Delivered Orders Revenue: Rs. " + String.format("%.2f", totalRevenue));
        System.out.println("   Pending Orders Value: Rs. " + String.format("%.2f", pendingRevenue));
        System.out.println("   Total Revenue: Rs. " + String.format("%.2f", totalRevenue + pendingRevenue));
        
        // Average order value
        double avgOrderValue = orders.stream()
                                   .filter(o -> "Delivered".equals(o.getStatus()))
                                   .mapToDouble(Order::getTotalAmount)
                                   .average()
                                   .orElse(0.0);
        
        System.out.println("   Average Order Value: Rs. " + String.format("%.2f", avgOrderValue));
        
        // Today's orders
        long todayOrders = orders.stream()
                                .filter(o -> o.getOrderTime().toLocalDate().equals(LocalDateTime.now().toLocalDate()))
                                .count();
        
        System.out.println("\nToday's Orders: " + todayOrders);
    }

    // Helper Methods
    public Order findOrderById(int orderId) {
        return orders.stream()
                    .filter(o -> o.getOrderId() == orderId)
                    .findFirst()
                    .orElse(null);
    }

    public List<Order> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return orders.stream()
                    .filter(o -> o.getOrderTime().isAfter(startDate) && o.getOrderTime().isBefore(endDate))
                    .collect(Collectors.toList());
    }

    public List<Order> getOrdersByCustomer(int customerId) {
        return orders.stream()
                    .filter(o -> o.getCustomer().getUserId() == customerId)
                    .collect(Collectors.toList());
    }

    private void sendStatusNotification(Order order, String status) {
        String message = "";
        switch (status) {
            case "Confirmed":
                message = "Your order has been confirmed and is being prepared!";
                break;
            case "Preparing":
                message = "Your order is being prepared by our chefs!";
                break;
            case "Out for Delivery":
                message = "Your order is out for delivery! Track your order for real-time updates.";
                break;
            case "Delivered":
                message = "Your order has been delivered! Enjoy your meal!";
                break;
            case "Cancelled":
                message = "Your order has been cancelled. Refund will be processed soon.";
                break;
        }
        
        System.out.println("Notification sent to " + order.getCustomer().getUsername() + ": " + message);
    }

    private void displayOrderSummary(Order order) {
        System.out.println("Order ID: " + order.getOrderId() + " | " + order.getTrackingNumber());
        System.out.println("Customer: " + order.getCustomer().getUsername());
        System.out.println("Status: " + order.getStatusWithEmoji());
        System.out.println("Total: Rs. " + String.format("%.2f", order.getTotalAmount()));
        System.out.println("Order Time: " + order.getFormattedOrderTime());
        if (order.getDeliveryPerson() != null) {
            System.out.println("Delivery By: " + order.getDeliveryPerson().getName());
        }
    }

    // Getters
    public List<Order> getAllOrders() { return orders; }
    public int getTotalOrders() { return orders.size(); }
}
