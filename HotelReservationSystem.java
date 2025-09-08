
import java.io.*;
import java.util.*;

// Room class
class Room implements Serializable {
    private int roomNumber;
    private String category;
    private double price;
    private boolean isBooked;

    public Room(int roomNumber, String category, double price) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.price = price;
        this.isBooked = false;
    }

    public int getRoomNumber() { return roomNumber; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public boolean isBooked() { return isBooked; }

    public void book() { isBooked = true; }
    public void cancel() { isBooked = false; }

    @Override
    public String toString() {
        return "Room " + roomNumber + " | " + category + " | Price: $" + price + 
               " | " + (isBooked ? "Booked" : "Available");
    }
}

// Booking class
class Booking implements Serializable {
    private String userName;
    private Room room;

    public Booking(String userName, Room room) {
        this.userName = userName;
        this.room = room;
    }

    public String getUserName() { return userName; }
    public Room getRoom() { return room; }

    @Override
    public String toString() {
        return "Booking for " + userName + " | " + room;
    }
}

// Hotel class
class Hotel {
    private List<Room> rooms = new ArrayList<>();
    private List<Booking> bookings = new ArrayList<>();
    private static final String FILE_NAME = "hotel_data.dat";

    public Hotel() {
        loadData();
        if (rooms.isEmpty()) initializeRooms();
    }

    private void initializeRooms() {
        rooms.add(new Room(101, "Standard", 100));
        rooms.add(new Room(102, "Standard", 100));
        rooms.add(new Room(201, "Deluxe", 200));
        rooms.add(new Room(202, "Deluxe", 200));
        rooms.add(new Room(301, "Suite", 350));
        rooms.add(new Room(302, "Suite", 350));
    }

    public void viewRooms() {
        System.out.println("\n--- Hotel Rooms ---");
        for (Room room : rooms) {
            System.out.println(room);
        }
    }

    public void bookRoom(String userName, int roomNumber) {
        Room room = findRoom(roomNumber);
        if (room == null) {
            System.out.println("Room not found.");
            return;
        }
        if (room.isBooked()) {
            System.out.println("Room is already booked.");
            return;
        }
        room.book();
        Booking booking = new Booking(userName, room);
        bookings.add(booking);
        System.out.println("Room booked successfully for " + userName);
        simulatePayment(room.getPrice());
        saveData();
    }

    public void cancelBooking(String userName, int roomNumber) {
        Booking bookingToCancel = null;
        for (Booking b : bookings) {
            if (b.getUserName().equalsIgnoreCase(userName) && b.getRoom().getRoomNumber() == roomNumber) {
                bookingToCancel = b;
                break;
            }
        }
        if (bookingToCancel == null) {
            System.out.println("Booking not found.");
            return;
        }
        bookingToCancel.getRoom().cancel();
        bookings.remove(bookingToCancel);
        System.out.println("Booking canceled for " + userName);
        saveData();
    }

    public void viewBookings() {
        System.out.println("\n--- Current Bookings ---");
        if (bookings.isEmpty()) {
            System.out.println("No bookings yet.");
            return;
        }
        for (Booking b : bookings) {
            System.out.println(b);
        }
    }

    private Room findRoom(int roomNumber) {
        for (Room r : rooms) {
            if (r.getRoomNumber() == roomNumber) return r;
        }
        return null;
    }

    private void simulatePayment(double amount) {
        System.out.println("Processing payment of $" + amount + "...");
        System.out.println("Payment successful!\n");
    }

    private void saveData() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(rooms);
            out.writeObject(bookings);
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    private void loadData() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            rooms = (List<Room>) in.readObject();
            bookings = (List<Booking>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // File may not exist yet, start fresh
        }
    }
}

// Main class
public class HotelReservationSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Hotel hotel = new Hotel();

        System.out.print("Enter your name: ");
        String userName = scanner.nextLine();

        while (true) {
            System.out.println("\n--- Hotel Reservation System ---");
            System.out.println("1. View Rooms");
            System.out.println("2. Book Room");
            System.out.println("3. Cancel Booking");
            System.out.println("4. View My Bookings");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1 -> hotel.viewRooms();
                case 2 -> {
                    System.out.print("Enter room number to book: ");
                    int roomNumber = scanner.nextInt();
                    scanner.nextLine();
                    hotel.bookRoom(userName, roomNumber);
                }
                case 3 -> {
                    System.out.print("Enter room number to cancel: ");
                    int roomNumber = scanner.nextInt();
                    scanner.nextLine();
                    hotel.cancelBooking(userName, roomNumber);
                }
                case 4 -> hotel.viewBookings();
                case 5 -> {
                    System.out.println("Thank you for using Hotel Reservation System.");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }
}
