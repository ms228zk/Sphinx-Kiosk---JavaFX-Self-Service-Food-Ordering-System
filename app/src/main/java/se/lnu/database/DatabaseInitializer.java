package se.lnu.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

  public static void initialize() {
    String url = "jdbc:sqlite:kiosk.db";

    try (Connection conn = DriverManager.getConnection(url);
         Statement stmt = conn.createStatement()) {

      String createCategory = """
                CREATE TABLE IF NOT EXISTS Category (
                    category_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    description TEXT
                );
            """;

      String createMenuItem = """
                CREATE TABLE IF NOT EXISTS MenuItem (
                    menu_item_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    category_id INTEGER NOT NULL,
                    name TEXT NOT NULL,
                    description TEXT,
                    price REAL NOT NULL,
                    image_path TEXT,
                    is_available INTEGER NOT NULL DEFAULT 1,
                    FOREIGN KEY (category_id) REFERENCES Category(category_id)
                );
            """;
      String createCustomerOrder = """
                CREATE TABLE IF NOT EXISTS CustomerOrder (
                    order_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    order_time TEXT,
                    total_price REAL,
                    status TEXT
                );
            """;

      String createOrderItem = """
                CREATE TABLE IF NOT EXISTS OrderItem (
                    order_item_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    order_id INTEGER,
                    menu_item_id INTEGER,
                    quantity INTEGER,
                    price REAL,
                    FOREIGN KEY (order_id) REFERENCES CustomerOrder(order_id),
                    FOREIGN KEY (menu_item_id) REFERENCES MenuItem(menu_item_id)
                );
            """;


      // Create tables
      stmt.execute(createCategory);
      stmt.execute(createMenuItem);
      stmt.execute(createCustomerOrder);
      stmt.execute(createOrderItem);

      // Insert test categories
      stmt.execute("""
                INSERT OR IGNORE INTO Category (category_id, name, description)
                VALUES (1, 'Burgers', 'Burger meals and sandwiches');
            """);

      stmt.execute("""
                INSERT OR IGNORE INTO Category (category_id, name, description)
                VALUES (2, 'Drinks', 'Cold beverages');
            """);

      // Insert test menu items
      stmt.execute("""
                INSERT OR IGNORE INTO MenuItem
                (menu_item_id, category_id, name, description, price, image_path, is_available)
                VALUES
                (1, 1, 'Cheeseburger', 'Beef burger with cheese', 5.99, 'images/cheeseburger.png', 1);
            """);

      stmt.execute("""
                INSERT OR IGNORE INTO MenuItem
                (menu_item_id, category_id, name, description, price, image_path, is_available)
                VALUES
                (2, 2, 'Coca-Cola', 'Cold soft drink', 2.50, 'images/coke.png', 1);
            """);
      // Insert test customer order
      stmt.execute("""
                INSERT OR IGNORE INTO CustomerOrder
                (order_id, order_time, total_price, status)
                VALUES
                (1, '2026-04-20 21:00:00', 8.49, 'Pending');
            """);

      // Insert test order items
      stmt.execute("""
                INSERT OR IGNORE INTO OrderItem
                (order_item_id, order_id, menu_item_id, quantity, price)
                VALUES
                (1, 1, 1, 1, 5.99);
            """);

      stmt.execute("""
                INSERT OR IGNORE INTO OrderItem
                (order_item_id, order_id, menu_item_id, quantity, price)
                VALUES
                (2, 1, 2, 1, 2.50);
            """);

      System.out.println("All database tables created with test data!");

    } catch (SQLException e) {
      System.out.println("Database error: " + e.getMessage());
    }
  }
}