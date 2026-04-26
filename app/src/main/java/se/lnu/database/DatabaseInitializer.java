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
                (3, 1, 'Bacon & Herbs', 'Burger with bacon and herb flavor', 8.99, 'images/bacon-herbs.png', 1),
                (4, 1, 'Double Bacon & Herbs', 'Double burger with bacon and herb flavor', 10.99, 'images/double-bacon-herbs.png', 1),
                (5, 1, 'Butter Style Burger', 'Burger with buttery toasted bun and cheese', 7.99, 'images/butter-style-burger.png', 1),
                (6, 1, 'Big Arch', 'Large signature burger with sauce and vegetables', 9.49, 'images/big-arch.png', 1),
                (7, 1, 'The Maco', 'House special burger', 8.49, 'images/the-maco.png', 1),
                (8, 1, 'Tasty', 'Classic tasty burger with cheese', 7.49, 'images/tasty.png', 1),
                (9, 1, 'Double Tasty', 'Double tasty burger with extra beef and cheese', 9.99, 'images/double-tasty.png', 1),
                (10, 1, 'Tasty Bacon', 'Tasty burger with bacon', 8.49, 'images/tasty-bacon.png', 1),
                (11, 1, 'Double Tasty Bacon', 'Double tasty burger with bacon', 10.49, 'images/double-tasty-bacon.png', 1),
                (12, 1, 'Big Mac', 'Two beef patties with signature sauce', 8.29, 'images/big-mac.png', 1),
                (13, 1, 'McFeast', 'Burger with lettuce, tomato and special sauce', 8.79, 'images/mcfeast.png', 1),
                (14, 1, 'Triple Cheeseburger', 'Triple beef burger with cheese', 9.29, 'images/triple-cheeseburger.png', 1),
                (15, 1, 'Double QP Cheese', 'Double quarter pounder with cheese', 10.29, 'images/double-qp-cheese.png', 1),
                (16, 1, 'QP Cheese', 'Quarter pounder with cheese', 8.99, 'images/qp-cheese.png', 1),
                (17, 1, 'Tasty Cheese', 'Tasty burger focused on melted cheese', 7.79, 'images/tasty-cheese.png', 1),
                (18, 1, 'Double Cheeseburger', 'Two beef patties with cheese', 6.99, 'images/double-cheeseburger.png', 1),
                (19, 1, 'Hamburger', 'Classic hamburger', 4.99, 'images/hamburger.png', 1);
            """);

      stmt.execute("""
                INSERT OR IGNORE INTO MenuItem
                (menu_item_id, category_id, name, description, price, image_path, is_available)
                VALUES
                (2, 2, 'Coca-Cola', 'Cold soft drink', 2.50, 'images/coke.png', 1);
            """);

      stmt.execute("""
                INSERT OR IGNORE INTO MenuItem
                (menu_item_id, category_id, name, description, price, image_path, is_available)
                VALUES
                (20, 2, 'Coca-Cola Original Taste', 'Classic Coca-Cola soft drink', 2.99, 'images/coca-cola-original-taste.png', 1),
                (21, 2, 'Coca-Cola Zero Sugar', 'Coca-Cola with zero sugar', 2.99, 'images/coca-cola-zero-sugar.png', 1),
                (22, 2, 'Fanta Orange Zero', 'Orange soda with zero sugar', 2.99, 'images/fanta-orange-zero.png', 1),
                (23, 2, 'Fanta Exotic', 'Fruity exotic soda', 2.99, 'images/fanta-exotic.png', 1),
                (24, 2, 'Sprite Zero Sugar', 'Lemon-lime soda with zero sugar', 2.99, 'images/sprite-zero-sugar.png', 1),
                (25, 2, 'Green Pop Lemonade', 'Sparkling green lemonade', 3.29, 'images/green-pop-lemonade.png', 1),
                (26, 2, 'Sakura x Sprite Zero', 'Limited edition cherry blossom Sprite Zero', 3.49, 'images/sakura-sprite-zero.png', 1),
                (27, 2, 'Deluxe Shake Mystery Blue', 'Creamy blue mystery shake', 4.99, 'images/deluxe-shake-mystery-blue.png', 1),
                (28, 2, 'Milkshake Mystery Blue', 'Blue mystery flavored milkshake', 4.49, 'images/milkshake-mystery-blue.png', 1),
                (29, 2, 'Milkshake Chocolate Flavor', 'Chocolate flavored milkshake', 4.49, 'images/milkshake-chocolate-flavor.png', 1),
                (30, 2, 'Milkshake Strawberry Flavor', 'Strawberry flavored milkshake', 4.49, 'images/milkshake-strawberry-flavor.png', 1),
                (31, 2, 'Milkshake Vanilla flavor', 'Vanilla flavored milkshake', 4.49, 'images/milkshake-vanilla-flavor.png', 1),
                (32, 2, 'Organic skimmed milk, 3 dl', 'Organic skimmed milk bottle', 2.79, 'images/organic-skimmed-milk-3dl.png', 1),
                (33, 2, 'Good Morning Orange Juice', 'Orange juice drink', 3.29, 'images/good-morning-orange-juice.png', 1),
                (34, 2, 'Good Morning Apple Juice', 'Apple juice drink', 3.29, 'images/good-morning-apple-juice.png', 1),
                (35, 2, 'Bonaqua Lemon Lime', 'Still water with lemon lime flavor', 2.59, 'images/bonaqua-lemon-lime.png', 1),
                (36, 2, 'Bonaqua Natural', 'Natural bottled water', 2.39, 'images/bonaqua-natural.png', 1);
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
