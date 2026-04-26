package se.lnu.database;

import se.lnu.MenuItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MenuItemRepository {

  public static List<MenuItem> findByCategoryName(String categoryName) {
    List<MenuItem> items = new ArrayList<>();
    String sql = """
            SELECT m.menu_item_id, m.category_id, m.name, m.description, m.price
            FROM MenuItem m
            INNER JOIN Category c ON c.category_id = m.category_id
            WHERE c.name = ? AND m.is_available = 1
            ORDER BY m.name
        """;

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {

      statement.setString(1, categoryName);

      try (ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
          items.add(new MenuItem(
                  resultSet.getInt("menu_item_id"),
                  resultSet.getInt("category_id"),
                  resultSet.getString("name"),
                  resultSet.getString("description"),
                  resultSet.getDouble("price")
          ));
        }
      }
    } catch (SQLException e) {
      System.out.println("Failed to load menu items: " + e.getMessage());
    }

    return items;
  }
}
