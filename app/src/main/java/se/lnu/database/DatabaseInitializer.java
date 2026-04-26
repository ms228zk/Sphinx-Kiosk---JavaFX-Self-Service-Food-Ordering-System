package se.lnu.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

  public static void initialize() {
    try (Connection conn = DatabaseConnection.getConnection();
         Statement stmt = conn.createStatement()) {

      stmt.execute("""
                CREATE TABLE IF NOT EXISTS Category (
                    category_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL
                )
            """);

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

      stmt.execute("DELETE FROM MenuItem");
      stmt.execute("DELETE FROM Category");

      stmt.execute("""
                INSERT INTO Category (category_id, name) VALUES
                (1, 'Burgers'),
                (2, 'Drinks'),
                (3, 'Sides'),
                (4, 'Desserts')
            """);

      stmt.execute("""
                INSERT INTO MenuItem (category_id, name, description, price) VALUES
                (1, 'Cheeseburger', 'Beef burger with cheese', 39.00),
                (1, 'Chicken Burger', 'Chicken burger with salad', 45.00),
                (1, 'Veg Burger', 'Vegetarian burger', 35.00),

                (2, 'Cola', 'Cold soft drink', 25.00),
                (2, 'Water', 'Still water', 20.00),
                (2, 'Juice', 'Fresh fruit juice', 30.00),

                (3, 'Fries', 'Crispy fries', 29.00),
                (3, 'Onion Rings', 'Fried onion rings', 32.00),

                (4, 'Ice Cream', 'Vanilla ice cream', 28.00),
                (4, 'Chocolate Cake', 'Chocolate dessert', 40.00)
            """);

      System.out.println("Database initialized successfully.");

    } catch (SQLException e) {
      System.out.println("Database initialization error: " + e.getMessage());
    }
  }
}