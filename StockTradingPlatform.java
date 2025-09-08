
import java.io.*;
import java.util.*;

// Stock class
class Stock implements Serializable {
    private String symbol;
    private String name;
    private double price;

    public Stock(String symbol, String name, double price) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
    }

    public String getSymbol() { return symbol; }
    public String getName() { return name; }
    public double getPrice() { return price; }

    public void setPrice(double price) { this.price = price; }

    @Override
    public String toString() {
        return symbol + " - " + name + " | Price: $" + price;
    }
}

// User class
class User implements Serializable {
    private String name;
    private double balance;
    private Map<String, Integer> portfolio; // symbol -> quantity

    public User(String name, double balance) {
        this.name = name;
        this.balance = balance;
        this.portfolio = new HashMap<>();
    }

    public String getName() { return name; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }

    public Map<String, Integer> getPortfolio() { return portfolio; }

    public void buyStock(Stock stock, int quantity) {
        double cost = stock.getPrice() * quantity;
        if (cost > balance) {
            System.out.println("Insufficient funds.");
            return;
        }
        balance -= cost;
        portfolio.put(stock.getSymbol(), portfolio.getOrDefault(stock.getSymbol(), 0) + quantity);
        System.out.println("Bought " + quantity + " shares of " + stock.getName());
    }

    public void sellStock(Stock stock, int quantity) {
        int available = portfolio.getOrDefault(stock.getSymbol(), 0);
        if (quantity > available) {
            System.out.println("Not enough shares to sell.");
            return;
        }
        portfolio.put(stock.getSymbol(), available - quantity);
        if (portfolio.get(stock.getSymbol()) == 0) {
            portfolio.remove(stock.getSymbol());
        }
        double revenue = stock.getPrice() * quantity;
        balance += revenue;
        System.out.println("Sold " + quantity + " shares of " + stock.getName());
    }

    public void viewPortfolio(Map<String, Stock> marketStocks) {
        System.out.println("Portfolio of " + name);
        System.out.println("Balance: $" + balance);
        if (portfolio.isEmpty()) {
            System.out.println("No stocks in portfolio.");
            return;
        }
        for (String symbol : portfolio.keySet()) {
            Stock stock = marketStocks.get(symbol);
            int qty = portfolio.get(symbol);
            System.out.println(stock.getName() + " (" + symbol + "): " + qty + " shares | Current Price: $" + stock.getPrice());
        }
    }
}

// Handles saving and loading user data
class PortfolioManager {
    private static final String FILE_NAME = "portfolio.dat";

    public static void saveUser(User user) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(user);
            System.out.println("Portfolio saved.");
        } catch (IOException e) {
            System.out.println("Error saving portfolio: " + e.getMessage());
        }
    }

    public static User loadUser() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (User) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No saved portfolio found. Starting new user.");
            return null;
        }
    }
}

// StockMarket class
class StockMarket {
    private Map<String, Stock> stocks = new HashMap<>();
    private User user;
    private Scanner scanner = new Scanner(System.in);

    public StockMarket() {
        stocks.put("AAPL", new Stock("AAPL", "Apple Inc.", 150.0));
        stocks.put("GOOGL", new Stock("GOOGL", "Alphabet Inc.", 2800.0));
        stocks.put("AMZN", new Stock("AMZN", "Amazon.com Inc.", 3400.0));
        stocks.put("TSLA", new Stock("TSLA", "Tesla Inc.", 700.0));

        user = PortfolioManager.loadUser();
        if (user == null) {
            System.out.print("Enter your name: ");
            String name = scanner.nextLine();
            user = new User(name, 10000); // Start with $10,000
        }
    }

    public void displayMenu() {
        while (true) {
            System.out.println("\n--- Stock Trading Platform ---");
            System.out.println("1. View Market Data");
            System.out.println("2. View Portfolio");
            System.out.println("3. Buy Stock");
            System.out.println("4. Sell Stock");
            System.out.println("5. Save Portfolio");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1: viewMarketData(); break;
                case 2: user.viewPortfolio(stocks); break;
                case 3: buyStock(); break;
                case 4: sellStock(); break;
                case 5: PortfolioManager.saveUser(user); break;
                case 6:
                    PortfolioManager.saveUser(user);
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void viewMarketData() {
        System.out.println("\n--- Market Data ---");
        for (Stock stock : stocks.values()) {
            System.out.println(stock);
        }
    }

    private void buyStock() {
        System.out.print("Enter stock symbol to buy: ");
        String symbol = scanner.nextLine().toUpperCase();
        Stock stock = stocks.get(symbol);
        if (stock == null) {
            System.out.println("Stock not found.");
            return;
        }
        System.out.print("Enter quantity to buy: ");
        int qty = scanner.nextInt();
        scanner.nextLine();
        user.buyStock(stock, qty);
    }

    private void sellStock() {
        System.out.print("Enter stock symbol to sell: ");
        String symbol = scanner.nextLine().toUpperCase();
        Stock stock = stocks.get(symbol);
        if (stock == null) {
            System.out.println("Stock not found.");
            return;
        }
        System.out.print("Enter quantity to sell: ");
        int qty = scanner.nextInt();
        scanner.nextLine();
        user.sellStock(stock, qty);
    }
}

// Main class
public class StockTradingPlatform {
    public static void main(String[] args) {
        StockMarket market = new StockMarket();
        market.displayMenu();
    }
}
