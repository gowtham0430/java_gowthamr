package main;

import model.*;
import service.*;
import java.util.*;

public class MainApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<Order> orders = new ArrayList<>();

        // Service layers
        AdminService admin = new AdminService(orders);
        CustomerService customer = new CustomerService(orders, admin.getRestaurants());
        OrderService orderService = new OrderService(orders);

        // Connect promotions between admin and customer services
        customer.setPromotions(admin.getPromotions());

        System.out.println("🍽️ Welcome to Advanced Food Delivery System! 🍽️");
        System.out.println("=" + "=".repeat(50));

        while (true) {
            try {
                System.out.println("\n🏠 Main Menu:");
                System.out.println("1. 👨‍💼 Admin Panel");
                System.out.println("2. 👤 Customer Portal");
                System.out.println("3. 📊 System Analytics");
                System.out.println("4. ❌ Exit");
                System.out.print("Choose an option: ");
                int choice = sc.nextInt();
                sc.nextLine();

                if (choice == 1) {
                    adminMenu(sc, admin, orderService);
                } else if (choice == 2) {
                    customerMenu(sc, customer, orderService);
                } else if (choice == 3) {
                    systemAnalytics(admin, orderService);
                } else if (choice == 4) {
                    System.out.println("👋 Thank you for using Advanced Food Delivery System!");
                    System.out.println("Have a great day! 🍕🍔🍜");
                    break;
                } else {
                    System.out.println("⚠️ Invalid option! Please choose 1-4.");
                }
            } catch (InputMismatchException e) {
                System.out.println("⚠️ Please enter numbers only!");
                sc.nextLine(); // clear invalid input
            }
        }
        sc.close();
    }

    // ✅ ADMIN MENU
    private static void adminMenu(Scanner sc, AdminService admin, OrderService orderService) {
        while (true) {
            try {
                System.out.println("\n👨‍💼 Admin Panel:");
                System.out.println("=" + "=".repeat(40));
                System.out.println("🏪 Restaurant Management:");
                System.out.println("1. Add Restaurant (Basic)");
                System.out.println("2. Add Restaurant (Full Details)");
                System.out.println("3. Update Restaurant Details");
                System.out.println("4. View Restaurants and Menus");
                System.out.println("\n🍽️ Food Item Management:");
                System.out.println("5. Add Food Item (Basic)");
                System.out.println("6. Add Food Item (Full Details)");
                System.out.println("7. Update Food Item");
                System.out.println("8. Remove Food Item");
                System.out.println("9. Toggle Food Item Availability");
                System.out.println("\n📦 Order Management:");
                System.out.println("10. View All Orders");
                System.out.println("11. View Orders by Status");
                System.out.println("12. Update Order Status");
                System.out.println("13. Assign Delivery Person");
                System.out.println("\n🚚 Delivery Management:");
                System.out.println("14. Add Delivery Person");
                System.out.println("15. View Delivery Persons");
                System.out.println("\n🎉 Promotion Management:");
                System.out.println("16. Add Promotion");
                System.out.println("17. View Promotions");
                System.out.println("\n📊 Analytics:");
                System.out.println("18. View System Analytics");
                System.out.println("19. View Order Analytics");
                System.out.println("\n20. 🔙 Back to Main Menu");
                System.out.print("Choose an option: ");
                int opt = sc.nextInt();
                sc.nextLine();

                switch (opt) {
                    case 1:
                        addRestaurantBasic(sc, admin);
                        break;
                    case 2:
                        addRestaurantFull(sc, admin);
                        break;
                    case 3:
                        updateRestaurant(sc, admin);
                        break;
                    case 4:
                        admin.viewRestaurants();
                        break;
                    case 5:
                        addFoodItemBasic(sc, admin);
                        break;
                    case 6:
                        addFoodItemFull(sc, admin);
                        break;
                    case 7:
                        updateFoodItem(sc, admin);
                        break;
                    case 8:
                        removeFoodItem(sc, admin);
                        break;
                    case 9:
                        toggleFoodItemAvailability(sc, admin);
                        break;
                    case 10:
                        orderService.viewAllOrders();
                        break;
                    case 11:
                        viewOrdersByStatus(sc, orderService);
                        break;
                    case 12:
                        updateOrderStatus(sc, orderService);
                        break;
                    case 13:
                        assignDeliveryPerson(sc, admin, orderService);
                        break;
                    case 14:
                        addDeliveryPerson(sc, admin);
                        break;
                    case 15:
                        admin.viewDeliveryPersons();
                        break;
                    case 16:
                        addPromotion(sc, admin);
                        break;
                    case 17:
                        admin.viewPromotions();
                        break;
                    case 18:
                        admin.viewAnalytics();
                        break;
                    case 19:
                        orderService.viewOrderAnalytics();
                        break;
                    case 20:
                        return;
                    default:
                        System.out.println("⚠️ Invalid option! Please choose 1-20.");
                }
            } catch (InputMismatchException e) {
                System.out.println("⚠️ Please enter valid numbers!");
                sc.nextLine();
            }
        }
    }

    // ✅ CUSTOMER MENU
    private static void customerMenu(Scanner sc, CustomerService cust, OrderService orderService) {
        while (true) {
            try {
                System.out.println("\n👤 Customer Portal:");
                System.out.println("=" + "=".repeat(40));
                System.out.println("1. Register Customer");
                System.out.println("2. View Restaurants");
                System.out.println("3. View All Food Items");
                System.out.println("4. Browse Food by Category");
                System.out.println("5. Search Food Items");
                System.out.println("6. Add Food to Cart");
                System.out.println("7. Remove Item from Cart");
                System.out.println("8. View Cart");
                System.out.println("9. Clear Cart");
                System.out.println("10. View Promotions");
                System.out.println("11. Place Order (with address/payment)");
                System.out.println("12. View My Orders");
                System.out.println("13. Track an Order");
                System.out.println("14. Cancel Order");
                System.out.println("15. Add Restaurant Review");
                System.out.println("16. Add Food Item Review");
                System.out.println("17. View Reviews for Restaurant");
                System.out.println("18. 🔙 Back to Main Menu");
                System.out.print("Choose an option: ");
                int opt = sc.nextInt();
                sc.nextLine();

                switch (opt) {
                    case 1: // register
                        System.out.print("Enter Customer ID: ");
                        int id = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Enter Name: ");
                        String name = sc.nextLine();
                        System.out.print("Enter Contact: ");
                        long contact = sc.nextLong();
                        cust.addCustomer(id, name, contact);
                        break;
                    case 2:
                        cust.viewRestaurants();
                        break;
                    case 3:
                        cust.viewFoodItems();
                        break;
                    case 4:
                        System.out.print("Enter Category (e.g., Pizza, Burger, Sushi): ");
                        String category = sc.nextLine();
                        cust.viewFoodItemsByCategory(category);
                        break;
                    case 5:
                        System.out.print("Enter search term: ");
                        String term = sc.nextLine();
                        cust.searchFoodItems(term);
                        break;
                    case 6: // add to cart
                        System.out.print("Enter Customer ID: ");
                        int addCid = sc.nextInt();
                        System.out.print("Enter Restaurant ID: ");
                        int addRid = sc.nextInt();
                        System.out.print("Enter Food ID: ");
                        int addFid = sc.nextInt();
                        System.out.print("Enter Quantity: ");
                        int qty = sc.nextInt();
                        cust.addFoodToCart(addCid, addRid, addFid, qty);
                        break;
                    case 7: // remove from cart
                        System.out.print("Enter Customer ID: ");
                        int rmCid = sc.nextInt();
                        System.out.print("Enter Restaurant ID: ");
                        int rmRid = sc.nextInt();
                        System.out.print("Enter Food ID: ");
                        int rmFid = sc.nextInt();
                        cust.removeFromCart(rmCid, rmRid, rmFid);
                        break;
                    case 8:
                        System.out.print("Enter Customer ID: ");
                        int vCid = sc.nextInt();
                        cust.viewCart(vCid);
                        break;
                    case 9:
                        System.out.print("Enter Customer ID: ");
                        int clrCid = sc.nextInt();
                        cust.clearCart(clrCid);
                        break;
                    case 10:
                        cust.viewPromotions();
                        break;
                    case 11: // place order
                        System.out.print("Enter Customer ID: ");
                        int pCid = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Enter Delivery Address: ");
                        String address = sc.nextLine();
                        System.out.print("Enter Payment Method (COD/UPI/Card): ");
                        String pm = sc.nextLine();
                        System.out.print("Any special instructions? (leave blank if none): ");
                        String instr = sc.nextLine();
                        cust.placeOrder(pCid, address, pm, instr);
                        break;
                    case 12:
                        System.out.print("Enter Customer ID: ");
                        int listCid = sc.nextInt();
                        cust.viewOrders(listCid);
                        break;
                    case 13:
                        System.out.print("Enter Order ID: ");
                        int trkId = sc.nextInt();
                        orderService.trackOrder(trkId);
                        break;
                    case 14:
                        System.out.print("Enter Order ID: ");
                        int cancelId = sc.nextInt();
                        orderService.cancelOrder(cancelId);
                        break;
                    case 15: // restaurant review
                        System.out.print("Enter Customer ID: ");
                        int revCid = sc.nextInt();
                        System.out.print("Enter Restaurant ID: ");
                        int revRid = sc.nextInt();
                        System.out.print("Enter Rating (1.0 - 5.0): ");
                        double rRating = sc.nextDouble();
                        sc.nextLine();
                        System.out.print("Enter Comment: ");
                        String rComment = sc.nextLine();
                        cust.addReview(revCid, revRid, rRating, rComment);
                        break;
                    case 16: // food item review
                        System.out.print("Enter Customer ID: ");
                        int frCid = sc.nextInt();
                        System.out.print("Enter Restaurant ID: ");
                        int frRid = sc.nextInt();
                        System.out.print("Enter Food ID: ");
                        int frFid = sc.nextInt();
                        System.out.print("Enter Rating (1.0 - 5.0): ");
                        double fRating = sc.nextDouble();
                        sc.nextLine();
                        System.out.print("Enter Comment: ");
                        String fComment = sc.nextLine();
                        cust.addFoodItemReview(frCid, frRid, frFid, fRating, fComment);
                        break;
                    case 17:
                        System.out.print("Enter Restaurant ID: ");
                        int vrRid = sc.nextInt();
                        cust.viewReviews(vrRid);
                        break;
                    case 18:
                        return;
                    default:
                        System.out.println("⚠️ Invalid option! Please choose 1-18.");
                }
            } catch (InputMismatchException e) {
                System.out.println("⚠️ Please enter valid numbers!");
                sc.nextLine();
            }
        }
    }

    // ===== Admin helpers =====
    private static void addRestaurantBasic(Scanner sc, AdminService admin) {
        System.out.print("Enter Restaurant ID: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Restaurant Name: ");
        String name = sc.nextLine();
        admin.addRestaurant(id, name);
    }

    private static void addRestaurantFull(Scanner sc, AdminService admin) {
        System.out.print("Enter Restaurant ID: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Address: ");
        String address = sc.nextLine();
        System.out.print("Enter Phone: ");
        String phone = sc.nextLine();
        System.out.print("Enter Cuisine Type: ");
        String cuisine = sc.nextLine();
        System.out.print("Enter Delivery Time (min): ");
        int dtime = sc.nextInt();
        System.out.print("Enter Delivery Fee: ");
        double dfee = sc.nextDouble();
        System.out.print("Enter Minimum Order Amount: ");
        double min = sc.nextDouble();
        admin.addRestaurant(id, name, address, phone, cuisine, dtime, dfee, min);
    }

    private static void updateRestaurant(Scanner sc, AdminService admin) {
        System.out.print("Enter Restaurant ID: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter New Name: ");
        String name = sc.nextLine();
        System.out.print("Enter New Address: ");
        String address = sc.nextLine();
        System.out.print("Enter New Phone: ");
        String phone = sc.nextLine();
        System.out.print("Enter New Cuisine: ");
        String cuisine = sc.nextLine();
        admin.updateRestaurant(id, name, address, phone, cuisine);
    }

    private static void addFoodItemBasic(Scanner sc, AdminService admin) {
        System.out.print("Enter Restaurant ID: ");
        int rid = sc.nextInt();
        System.out.print("Enter Food ID: ");
        int fid = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Food Name: ");
        String fname = sc.nextLine();
        System.out.print("Enter Price: ");
        double price = sc.nextDouble();
        admin.addFoodItemToRestaurant(rid, fid, fname, price);
    }

    private static void addFoodItemFull(Scanner sc, AdminService admin) {
        System.out.print("Enter Restaurant ID: ");
        int rid = sc.nextInt();
        System.out.print("Enter Food ID: ");
        int fid = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Food Name: ");
        String fname = sc.nextLine();
        System.out.print("Enter Category: ");
        String cat = sc.nextLine();
        System.out.print("Enter Description: ");
        String desc = sc.nextLine();
        System.out.print("Enter Price: ");
        double price = sc.nextDouble();
        System.out.print("Enter Prep Time (min): ");
        int prep = sc.nextInt();
        admin.addFoodItemToRestaurant(rid, fid, fname, price, cat, desc, prep);
    }

    private static void updateFoodItem(Scanner sc, AdminService admin) {
        System.out.print("Enter Restaurant ID: ");
        int rid = sc.nextInt();
        System.out.print("Enter Food ID: ");
        int fid = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter New Name: ");
        String n = sc.nextLine();
        System.out.print("Enter New Price: ");
        double p = sc.nextDouble();
        sc.nextLine();
        System.out.print("Enter New Category: ");
        String c = sc.nextLine();
        System.out.print("Enter New Description: ");
        String d = sc.nextLine();
        admin.updateFoodItem(rid, fid, n, p, c, d);
    }

    private static void removeFoodItem(Scanner sc, AdminService admin) {
        System.out.print("Enter Restaurant ID: ");
        int rid = sc.nextInt();
        System.out.print("Enter Food ID to remove: ");
        int fid = sc.nextInt();
        admin.removeFoodItemFromRestaurant(rid, fid);
    }

    private static void toggleFoodItemAvailability(Scanner sc, AdminService admin) {
        System.out.print("Enter Restaurant ID: ");
        int rid = sc.nextInt();
        System.out.print("Enter Food ID: ");
        int fid = sc.nextInt();
        admin.toggleFoodItemAvailability(rid, fid);
    }

    private static void viewOrdersByStatus(Scanner sc, OrderService orderService) {
        System.out.print("Enter Status (Pending/Confirmed/Preparing/Out for Delivery/Delivered/Cancelled): ");
        String status = sc.nextLine();
        orderService.viewOrdersByStatus(status);
    }

    private static void updateOrderStatus(Scanner sc, OrderService orderService) {
        System.out.print("Enter Order ID: ");
        int oid = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter New Status (Confirmed/Preparing/Out for Delivery/Delivered/Cancelled): ");
        String status = sc.nextLine();
        orderService.updateOrderStatus(oid, status);
    }

    private static void assignDeliveryPerson(Scanner sc, AdminService admin, OrderService orderService) {
        System.out.print("Enter Order ID: ");
        int oid = sc.nextInt();
        System.out.print("Enter Delivery Person ID: ");
        int did = sc.nextInt();
        DeliveryPerson dp = admin.findDeliveryPersonById(did);
        orderService.assignDeliveryPerson(oid, dp);
    }

    private static void addDeliveryPerson(Scanner sc, AdminService admin) {
        System.out.print("Enter Delivery Person ID: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Contact: ");
        long contact = sc.nextLong();
        admin.addDeliveryPerson(id, name, contact);
    }

    private static void addPromotion(Scanner sc, AdminService admin) {
        System.out.print("Enter Promo Code: ");
        String code = sc.nextLine();
        System.out.print("Enter Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Description: ");
        String desc = sc.nextLine();
        System.out.print("Enter Discount Percentage (0 if flat): ");
        double pct = sc.nextDouble();
        System.out.print("Enter Flat Discount Amount (0 if percent): ");
        double flat = sc.nextDouble();
        System.out.print("Enter Minimum Order Amount: ");
        double min = sc.nextDouble();
        System.out.print("Valid for how many days from now?: ");
        int days = sc.nextInt();
        System.out.print("Max Uses: ");
        int max = sc.nextInt();
        admin.addPromotion(code, name, desc, pct, flat, min, days, max);
    }

    private static void systemAnalytics(AdminService admin, OrderService orderService) {
        admin.viewAnalytics();
        System.out.println();
        orderService.viewOrderAnalytics();
    }
}
