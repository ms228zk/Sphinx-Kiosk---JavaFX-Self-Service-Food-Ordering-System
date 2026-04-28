package se.lnu.database;

import se.lnu.Category;
import se.lnu.MenuItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {

    public static List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();

        String sql = "SELECT category_id, name FROM Category";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                categories.add(new Category(
                        rs.getInt("category_id"),
                        rs.getString("name")
                ));
            }

        } catch (SQLException e) {
            System.out.println("DB error (getCategories): " + e.getMessage());
        }

        return categories;
    }

    public static List<MenuItem> getItemsByCategory(int categoryId) {
        List<MenuItem> items = new ArrayList<>();

        String sql = """
            SELECT menu_item_id, name, description, price
            FROM MenuItem
            WHERE category_id = ?
            ORDER BY name
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, categoryId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    items.add(new MenuItem(
                            rs.getInt("menu_item_id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getDouble("price")
                    ));
                }
            }

        } catch (SQLException e) {
            System.out.println("DB error (getItemsByCategory): " + e.getMessage());
        }

        return items;
    }
}