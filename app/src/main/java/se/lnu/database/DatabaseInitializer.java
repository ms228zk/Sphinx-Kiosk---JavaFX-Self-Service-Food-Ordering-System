package se.lnu.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

  public static void initialize() {
    try (Connection conn = DatabaseConnection.getConnection();
         Statement stmt = conn.createStatement()) {

      // Create Category table
      stmt.execute("""
                CREATE TABLE IF NOT EXISTS Category (
                    category_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL
                )
            """);

      // Create MenuItem table
      stmt.execute("""
                CREATE TABLE IF NOT EXISTS MenuItem (
                    menu_item_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    category_id INTEGER NOT NULL,
                    name TEXT NOT NULL,
                    description TEXT,
                    price REAL NOT NULL,
                    FOREIGN KEY (category_id) REFERENCES Category(category_id)
                )
            """);

      // Reset data
      stmt.execute("DELETE FROM MenuItem");
      stmt.execute("DELETE FROM Category");

      // Insert Categories
      stmt.execute("""
                INSERT INTO Category (category_id, name) VALUES
                (1, 'Burgers'),
                (2, 'Drinks'),
                (3, 'Sides'),
                (4, 'Desserts'),
                (5, 'Combos')
            """);

      // Insert Menu Items
      stmt.execute("""
                INSERT INTO MenuItem (category_id, name, description, price) VALUES

                -- Burgers
                (1, 'BBQ Smash Burger', 'Double beef patty with BBQ sauce', 49.00),
                (1, 'Crispy Chicken Burger', 'Crispy chicken with garlic mayo', 45.00),
                (1, 'Halloumi Burger', 'Grilled halloumi with salad', 42.00),

                -- Drinks
                (2, 'Iced Coffee', 'Cold coffee with milk', 35.00),
                (2, 'Mango Smoothie', 'Fresh mango smoothie', 38.00),
                (2, 'Lemon Mint Cooler', 'Lemon drink with fresh mint', 30.00),

                -- Sides
                (3, 'Loaded Fries', 'Fries with cheese and sauce', 39.00),
                (3, 'Mozzarella Sticks', 'Crispy cheese sticks with dip', 36.00),
                (3, 'Spicy Chicken Bites', 'Small crispy spicy chicken pieces', 42.00),

                -- Desserts
                (4, 'Chocolate Brownie', 'Warm brownie with chocolate sauce', 32.00),
                (4, 'Mini Donuts', 'Three glazed mini donuts', 29.00),
                (4, 'Ice Cream Sundae', 'Ice cream with toppings and syrup', 35.00),

                -- Combos
                (5, 'Family Feast', '2 burgers, loaded fries, mozzarella sticks, and 2 drinks', 179.00),

                (5, 'Kids Combo', 'Mini burger, fries, and juice box', 69.00),

                (5, 'Burger Combo', 'BBQ Smash Burger, loaded fries, and soft drink', 99.00),

                (5, 'Chicken Combo', 'Crispy chicken burger, spicy chicken bites, and iced coffee', 109.00),

                (5, 'Snack Box', 'Mozzarella sticks, loaded fries, and lemon mint cooler', 89.00)
            """);

      System.out.println("Database initialized successfully.");

    } catch (SQLException e) {
      System.out.println("Database initialization error: " + e.getMessage());
    }
  }
}