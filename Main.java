package oops;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Abstract class representing a Vehicle
abstract class Vehicle {
    private String vehicleId;
    private String model;
    private boolean isAvailable;

    public Vehicle(String vehicleId, String model) {
        this.vehicleId = vehicleId;
        this.model = model;
        this.isAvailable = true;
    }

    // Encapsulation: Getters and Setters
    public String getVehicleId() {
        return vehicleId;
    }

    public String getModel() {
        return model;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    // Abstract method to calculate fare
    public abstract double calculateFare(double distance);

    @Override
    public String toString() {
        return "Vehicle ID: " + vehicleId + ", Model: " + model + ", Available: " + isAvailable;
    }
}

// Inheritance: Taxi class extends Vehicle
class Taxi extends Vehicle {
    private static final double RATE_PER_KM = 10.0;

    public Taxi(String vehicleId, String model) {
        super(vehicleId, model);
    }

    // Polymorphism: Overriding calculateFare method
    @Override
    public double calculateFare(double distance) {
        return distance * RATE_PER_KM;
    }
}

// Inheritance: LuxuryTaxi class extends Taxi
class LuxuryTaxi extends Taxi {
    private static final double LUXURY_RATE_PER_KM = 20.0;

    public LuxuryTaxi(String vehicleId, String model) {
        super(vehicleId, model);
    }

    // Polymorphism: Overriding calculateFare method
    @Override
    public double calculateFare(double distance) {
        return distance * LUXURY_RATE_PER_KM;
    }
}

// Class representing a User
class User {
    private String userId;
    private String name;
    private List<Booking> rideHistory;

    public User(String userId, String name) {
        this.userId = userId;
        this.name = name;
        this.rideHistory = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public List<Booking> getRideHistory() {
        return rideHistory;
    }

    public void addRideToHistory(Booking booking) {
        rideHistory.add(booking);
    }

    @Override
    public String toString() {
        return "User ID: " + userId + ", Name: " + name;
    }
}

// Class representing a Booking
class Booking {
    private User user;
    private Vehicle vehicle;
    private double distance;
    private double fare;
    private boolean isCompleted;

    public Booking(User user, Vehicle vehicle, double distance) {
        this.user = user;
        this.vehicle = vehicle;
        this.distance = distance;
        this.fare = vehicle.calculateFare(distance);
        this.isCompleted = false;
        vehicle.setAvailable(false); // Mark vehicle as booked
    }

    public double getFare() {
        return fare;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void completeRide() {
        isCompleted = true;
        vehicle.setAvailable(true); // Mark vehicle as available after ride completion
        user.addRideToHistory(this);
        System.out.println("Ride completed. Fare: " + fare);
    }

    @Override
    public String toString() {
        return "Booking Details: User=" + user.getName() + ", Vehicle=" + vehicle.getModel() +
                ", Distance=" + distance + " km, Fare=" + fare + ", Completed=" + isCompleted;
    }
}

// Class representing Payment
class Payment {
    private String paymentId;
    private double amount;
    private boolean isPaid;

    public Payment(double amount) {
        this.paymentId = "PAY" + System.currentTimeMillis();
        this.amount = amount;
        this.isPaid = false;
    }

    public void processPayment() {
        this.isPaid = true;
        System.out.println("Payment of " + amount + " processed successfully. Payment ID: " + paymentId);
    }

    @Override
    public String toString() {
        return "Payment ID: " + paymentId + ", Amount: " + amount + ", Paid: " + isPaid;
    }
}

// Class representing the Taxi Booking System
class TaxiBookingSystem {
    private List<Vehicle> vehicles;
    private List<Booking> bookings;
    private List<User> users;

    public TaxiBookingSystem() {
        this.vehicles = new ArrayList<>();
        this.bookings = new ArrayList<>();
        this.users = new ArrayList<>();
    }

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
    }

    public void addUser(User user) {
        users.add(user);
    }

    public List<User> getUsers() {
        return users;
    }

    public void bookTaxi(User user, double distance) {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.isAvailable()) {
                Booking booking = new Booking(user, vehicle, distance);
                bookings.add(booking);
                System.out.println("Taxi booked successfully! Fare: " + booking.getFare());
                return;
            }
        }
        System.out.println("No taxis available at the moment.");
    }

    public void completeRide(String vehicleId) {
        for (Booking booking : bookings) {
            if (booking.getVehicle().getVehicleId().equals(vehicleId) && !booking.isCompleted()) {
                booking.completeRide();
                Payment payment = new Payment(booking.getFare());
                payment.processPayment();
                return;
            }
        }
        System.out.println("No booking found for the given vehicle ID.");
    }

    public void showRideHistory(String userId) {
        for (User user : users) {
            if (user.getUserId().equals(userId)) {
                System.out.println("Ride History for User: " + user.getName());
                for (Booking booking : user.getRideHistory()) {
                    System.out.println(booking);
                }
                return;
            }
        }
        System.out.println("User not found.");
    }

    public void showAllVehicles() {
        System.out.println("Available Vehicles:");
        for (Vehicle vehicle : vehicles) {
            System.out.println(vehicle);
        }
    }
}

// Main class to test the Taxi Booking System
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TaxiBookingSystem system = new TaxiBookingSystem();

        // Add taxis to the system
        system.addVehicle(new Taxi("TAXI001", "Toyota Corolla"));
        system.addVehicle(new Taxi("TAXI002", "Honda Civic"));
        system.addVehicle(new LuxuryTaxi("LUX001", "Mercedes S-Class"));

        // Create users
        User user1 = new User("USER001", "John Doe");
        User user2 = new User("USER002", "Jane Smith");
        system.addUser(user1);
        system.addUser(user2);

        while (true) {
            System.out.println("\n--- Taxi Booking System ---");
            System.out.println("1. Book a Taxi");
            System.out.println("2. Complete a Ride");
            System.out.println("3. Show Ride History");
            System.out.println("4. Show All Vehicles");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter user ID: ");
                    String userId = scanner.next();
                    System.out.print("Enter distance (in km): ");
                    double distance = scanner.nextDouble();
                    User user = null;
                    for (User u : system.getUsers()) {
                        if (u.getUserId().equals(userId)) {
                            user = u;
                            break;
                        }
                    }
                    if (user != null) {
                        system.bookTaxi(user, distance);
                    } else {
                        System.out.println("User not found.");
                    }
                    break;
                case 2:
                    System.out.print("Enter vehicle ID: ");
                    String vehicleId = scanner.next();
                    system.completeRide(vehicleId);
                    break;
                case 3:
                    System.out.print("Enter user ID: ");
                    String historyUserId = scanner.next();
                    system.showRideHistory(historyUserId);
                    break;
                case 4:
                    system.showAllVehicles();
                    break;
                case 5:
                    System.out.println("Exiting the system. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}