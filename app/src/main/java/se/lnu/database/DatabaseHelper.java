package se.lnu.database;

import se.lnu.Category;
import se.lnu.MenuItem;
import se.lnu.RemovableIngredient;
import se.lnu.ExtraOption;

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
                    int itemId = rs.getInt("menu_item_id");
                    MenuItem currentItem = new MenuItem(
                            itemId,
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getDouble("price")
                    );
                    List<RemovableIngredient> currentRemovable = getRemovableIngredientsByItem(itemId);
                    currentItem.setRemovableIngredients(currentRemovable);
                    items.add(currentItem);
                }
            }

        } catch (SQLException e) {
            System.out.println("DB error (getItemsByCategory): " + e.getMessage());
        }

        return items;
    }

    public static List<RemovableIngredient> getRemovableIngredientsByItem(int itemId) {
        List<RemovableIngredient> removableIngredients = new ArrayList<>();

        String sql = """
            SELECT r.ingredient_id, r.name
            FROM RemovableIngredient r JOIN MenuItemRemovableIngredient m
            ON r.ingredient_id = m.ingredient_id
            WHERE m.menu_item_id = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, itemId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    removableIngredients.add(new RemovableIngredient(
                            rs.getInt("ingredient_id"),
                            rs.getString("name")
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("DB error (getRemovableIngredientsByItem) " + e.getMessage());
        }

        return removableIngredients;
    }
    public static List<ExtraOption> getExtrasByItem(int itemId) {
        List<ExtraOption> extras = new ArrayList<>();

        String sql = """
        SELECT e.extra_id, e.name, e.price
        FROM ExtraOption e
        JOIN MenuItemExtraOption m
        ON e.extra_id = m.extra_id
        WHERE m.menu_item_id = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, itemId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    extras.add(new ExtraOption(
                            rs.getInt("extra_id"),
                            rs.getString("name"),
                            rs.getDouble("price")
                    ));
                }
            }

        } catch (SQLException e) {
            System.out.println("DB error (getExtrasByItem): " + e.getMessage());
        }

        return extras;
    }
}