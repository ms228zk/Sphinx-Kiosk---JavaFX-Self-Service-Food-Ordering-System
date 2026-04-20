package se.lnu.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

  public static void initialize() {
    String createCategoryTable = """
            CREATE TABLE IF NOT EXISTS Category (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL
            );
            """;

    String createMenuItemTable = """
            CREATE TABLE IF NOT EXISTS MenuItem (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                category_id INTEGER NOT NULL,
                name TEXT NOT NULL,
                description TEXT,
                price REAL NOT NULL,
                image_path TEXT,
                is_available INTEGER NOT NULL DEFAULT 1,
                FOREIGN KEY (category_id) REFERENCES Category(id)
            );
            """;

    String insertCategory = """
            INSERT OR IGNORE INTO Category (id, name)
            VALUES (1, 'Burgers');
            """;

    String insertMenuItem = """
            INSERT OR IGNORE INTO MenuItem (id, category_id, name, description, price, image_path, is_available)
            VALUES (1, 1, 'Cheeseburger', 'Tasty burger with cheese', 5.99, NULL, 1);
            """;

    try (Connection conn = DatabaseConnection.getConnection();
         Statement stmt = conn.createStatement()) {

      stmt.execute(createCategoryTable);
      stmt.execute(createMenuItemTable);

      stmt.execute(insertCategory);
      stmt.execute(insertMenuItem);

      ResultSet rs = stmt.executeQuery("SELECT * FROM MenuItem");

      System.out.println("Menu items in database:");
      while (rs.next()) {
        System.out.println(
                rs.getInt("id") + " | " +
                        rs.getString("name") + " | " +
                        rs.getDouble("price")
        );
      }

      System.out.println("SQLite database initialized successfully.");

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}