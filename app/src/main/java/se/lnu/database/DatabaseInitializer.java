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

      // Create tables
      stmt.execute(createCategory);
      stmt.execute(createMenuItem);

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

      System.out.println("Category and MenuItem tables created with test data!");

    } catch (SQLException e) {
      System.out.println("Database error: " + e.getMessage());
    }
  }
}