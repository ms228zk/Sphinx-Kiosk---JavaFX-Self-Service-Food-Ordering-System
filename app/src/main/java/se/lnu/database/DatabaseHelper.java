package se.lnu.database;

import se.lnu.Category;
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
            System.out.println("DB error: " + e.getMessage());
        }

        return categories;
    }
}