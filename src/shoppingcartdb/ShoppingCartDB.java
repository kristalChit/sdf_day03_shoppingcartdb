package sdf.day03.shoppingcartdb;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

// ShoppingCartDB class manages user shopping carts
class ShoppingCartDB {
    private String cartDirectory;

    // Constructor: Initialize the cartDirectory and create the directory if it doesn't exist
    public ShoppingCartDB(String cartDirectory) {
        this.cartDirectory = cartDirectory;
        File directory = new File(cartDirectory);
        if (!directory.exists()) {
            directory.mkdirs(); // Create the directory if it doesn't exist
        }
    }

    // Load user's shopping cart from a file
    public void loadCart(String username, List<String> cart) {
        try {
            File cartFile = new File(cartDirectory, username + ".db");
            if (cartFile.exists()) {
                Scanner scanner = new Scanner(cartFile);
                while (scanner.hasNextLine()) {
                    cart.add(scanner.nextLine());
                }
                scanner.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Save user's shopping cart to a file
    public void saveCart(String username, List<String> cart) {
        try {
            File cartFile = new File(cartDirectory, username + ".db");
            FileWriter writer = new FileWriter(cartFile);
            for (String item : cart) {
                writer.write(item + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // List all registered users
    public List<String> listUsers() {
        File[] files = new File(cartDirectory).listFiles();
        if (files == null) {
            return new ArrayList<>();
        }
        List<String> users = Arrays.stream(files)
                .filter(file -> file.isFile() && file.getName().endsWith(".db"))
                .map(file -> file.getName().replace(".db", ""))
                .collect(Collectors.toList();
        return users;
    }
}

// ShoppingCartApp class handles user interactions
class ShoppingCartApp {
    private String currentUser;
    private List<String> cart;
    private ShoppingCartDB cartDB;

    // Constructor: Initialize the cartDB, cart, and currentUser
    public ShoppingCartApp(String cartDirectory) {
        cartDB = new ShoppingCartDB(cartDirectory);
        cart = new ArrayList<>();
        currentUser = null;
    }

    // Log in as a user and load their shopping cart
    public void login(String username) {
        if (currentUser != null) {
            System.out.println("Please log out before logging in as a different user.");
            return;
        }
        cart.clear();
        cartDB.loadCart(username, cart);
        currentUser = username;
        System.out.println(username + ", your cart contains the following items:");
        listCart();
    }

    // Save the current user's shopping cart
    public void save() {
        if (currentUser == null) {
            System.out.println("Please login as a user before saving the cart.");
            return;
        }
        cartDB.saveCart(currentUser, cart);
        System.out.println("Your cart has been saved.");
    }

    // Add items to the shopping cart
    public void add(String items) {
        if (currentUser == null) {
            System.out.println("Please login as a user before adding items to the cart.");
            return;
        }
        String[] itemsArray = items.split(", ");
        cart.addAll(Arrays.asList(itemsArray));
        for (String item : itemsArray) {
            System.out.println(item + " added to cart");
        }
    }

    // List items in the current user's shopping cart
    public void listCart() {
        for (int i = 0; i < cart.size(); i++) {
            System.out.println((i + 1) + ". " + cart.get(i));
        }
    }

    // List all registered users
    public void listUsers() {
        List<String> users = cartDB.listUsers();
        System.out.println("The following users are registered:");
        for (int i = 0; i < users.size(); i++) {
            System.out.println((i + 1) + ". " + users.get(i));
        }
    }
}

// Main class for running the program
public class Main {
    public static void main(String[] args) {
        // Replace "cartdb" with the desired directory path
        ShoppingCartApp app = new ShoppingCartApp("cartdb");
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to your shopping cart");
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();
            String[] parts = input.split(" ", 2);
            String command = parts[0].toLowerCase();
            String arguments = parts.length > 1 ? parts[1] : "";

            switch (command) {
                case "login":
                    app.login(arguments);
                    break;
                case "save":
                    app.save();
                    break;
                case "add":
                    app.add(arguments);
                    break;
                case "list":
                    app.listCart();
                    break;
                case "users":
                    app.listUsers();
                    break;
                default:
                    System.out.println("Invalid command. Try again.");
            }
        }
    }
}

// This code provides an interactive shopping cart program. 
// Users can use commands like "login," "save," "add," "list," and "users" to interact with the shopping cart.
// The ShoppingCartDB class manages cart database operations, including loading and saving shopping carts and listing users.
// The ShoppingCartApp class handles user interactions and maintains the current user's cart.
// The Main class is the entry point for the program and reads user input for commands.
