package se.lnu.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import se.lnu.Category;
import se.lnu.MenuItem;
import se.lnu.RemovableIngredient;
import se.lnu.ExtraOption;
import se.lnu.ComboChoiceGroup;
import se.lnu.ComboChoiceOption;

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

            System.out.println(
              "DB error (getCategories): "
                + e.getMessage()
            );
        }

        return categories;
    }

    // ADD CATEGORY

    public static boolean addCategory(String name) {

        String sql =
          "INSERT INTO Category (name) VALUES (?)";

        try (Connection conn =
               DatabaseConnection.getConnection();

             PreparedStatement pstmt =
               conn.prepareStatement(sql)) {

            pstmt.setString(1, name);

            pstmt.executeUpdate();

            return true;

        } catch (SQLException e) {

            System.out.println(
              "DB error (addCategory): "
                + e.getMessage()
            );

            return false;
        }
    }

    // DELETE CATEGORY

    public static boolean deleteCategory(int id) {

        String sql =
          "DELETE FROM Category WHERE category_id = ?";

        try (Connection conn =
               DatabaseConnection.getConnection();

             PreparedStatement pstmt =
               conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            pstmt.executeUpdate();

            return true;

        } catch (SQLException e) {

            System.out.println(
              "DB error (deleteCategory): "
                + e.getMessage()
            );

            return false;
        }
    }

    public static List<MenuItem>
    getItemsByCategory(int categoryId) {

        List<MenuItem> items = new ArrayList<>();

        String sql = """
            SELECT menu_item_id,
                   name,
                   description,
                   price,
                   available
            FROM MenuItem
            WHERE category_id = ?
            AND available = 1
            ORDER BY name
        """;

        try (Connection conn =
               DatabaseConnection.getConnection();

             PreparedStatement pstmt =
               conn.prepareStatement(sql)) {

            pstmt.setInt(1, categoryId);

            try (ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {

                    int itemId =
                      rs.getInt("menu_item_id");

                    String itemName =
                      rs.getString("name");

                    String imageFileName =
                      sanitizeImageName(itemName)
                        + ".png";

                    MenuItem currentItem =
                      new MenuItem(
                        itemId,
                        itemName,
                        rs.getString("description"),
                        rs.getDouble("price"),
                        imageFileName
                      );

                    List<RemovableIngredient>
                      currentRemovable =
                      getRemovableIngredientsByItem(
                        itemId
                      );

                    currentItem
                      .setRemovableIngredients(
                        currentRemovable
                      );

                    items.add(currentItem);
                }
            }

        } catch (SQLException e) {

            System.out.println(
              "DB error (getItemsByCategory): "
                + e.getMessage()
            );
        }

        return items;
    }

    public static boolean deleteMenuItem(
      int menuItemId
    ) {

        String deleteComboOptionsSql = """
            DELETE FROM ComboChoiceOption
            WHERE group_id IN (
                SELECT group_id
                FROM ComboChoiceGroup
                WHERE combo_item_id = ?
            )
        """;

        String deleteComboGroupsSql =
          """
          DELETE FROM ComboChoiceGroup
          WHERE combo_item_id = ?
          """;

        String deleteExtrasSql =
          """
          DELETE FROM MenuItemExtraOption
          WHERE menu_item_id = ?
          """;

        String deleteRemovablesSql =
          """
          DELETE FROM MenuItemRemovableIngredient
          WHERE menu_item_id = ?
          """;

        String deleteMenuItemSql =
          """
          DELETE FROM MenuItem
          WHERE menu_item_id = ?
          """;

        try (Connection conn =
               DatabaseConnection.getConnection()) {

            conn.setAutoCommit(false);

            try {

                executeDelete(
                  conn,
                  deleteComboOptionsSql,
                  menuItemId
                );

                executeDelete(
                  conn,
                  deleteComboGroupsSql,
                  menuItemId
                );

                executeDelete(
                  conn,
                  deleteExtrasSql,
                  menuItemId
                );

                executeDelete(
                  conn,
                  deleteRemovablesSql,
                  menuItemId
                );

                int deletedRows =
                  executeDelete(
                    conn,
                    deleteMenuItemSql,
                    menuItemId
                  );

                conn.commit();

                return deletedRows > 0;

            } catch (SQLException e) {

                conn.rollback();

                throw e;
            }

        } catch (SQLException e) {

            System.out.println(
              "DB error (deleteMenuItem): "
                + e.getMessage()
            );

            return false;
        }
    }

    private static int executeDelete(
      Connection conn,
      String sql,
      int menuItemId
    ) throws SQLException {

        try (PreparedStatement pstmt =
               conn.prepareStatement(sql)) {

            pstmt.setInt(1, menuItemId);

            return pstmt.executeUpdate();
        }
    }

    private static String sanitizeImageName(
      String name
    ) {

        return name.toLowerCase()
          .replaceAll("\\s+", "_")
          .replaceAll("[^a-z0-9_]", "");
    }

    public static List<RemovableIngredient>
    getRemovableIngredientsByItem(int itemId) {

        List<RemovableIngredient>
          removableIngredients =
          new ArrayList<>();

        String sql = """
            SELECT r.ingredient_id, r.name
            FROM RemovableIngredient r
            JOIN MenuItemRemovableIngredient m
            ON r.ingredient_id = m.ingredient_id
            WHERE m.menu_item_id = ?
        """;

        try (Connection conn =
               DatabaseConnection.getConnection();

             PreparedStatement pstmt =
               conn.prepareStatement(sql)) {

            pstmt.setInt(1, itemId);

            try (ResultSet rs =
                   pstmt.executeQuery()) {

                while (rs.next()) {

                    removableIngredients.add(
                      new RemovableIngredient(
                        rs.getInt("ingredient_id"),
                        rs.getString("name")
                      )
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
              "DB error (getRemovableIngredientsByItem): "
                + e.getMessage()
            );
        }

        return removableIngredients;
    }

    public static List<ExtraOption>
    getExtrasByItem(int itemId) {

        List<ExtraOption> extras =
          new ArrayList<>();

        String sql = """
            SELECT e.extra_id, e.name, e.price
            FROM ExtraOption e
            JOIN MenuItemExtraOption m
            ON e.extra_id = m.extra_id
            WHERE m.menu_item_id = ?
        """;

        try (Connection conn =
               DatabaseConnection.getConnection();

             PreparedStatement pstmt =
               conn.prepareStatement(sql)) {

            pstmt.setInt(1, itemId);

            try (ResultSet rs =
                   pstmt.executeQuery()) {

                while (rs.next()) {

                    extras.add(
                      new ExtraOption(
                        rs.getInt("extra_id"),
                        rs.getString("name"),
                        rs.getDouble("price")
                      )
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
              "DB error (getExtrasByItem): "
                + e.getMessage()
            );
        }

        return extras;
    }

    public static List<ComboChoiceGroup>
    getComboChoiceGroupsByItem(int comboItemId) {

        List<ComboChoiceGroup> groups =
          new ArrayList<>();

        String groupSql = """
            SELECT group_id, group_name
            FROM ComboChoiceGroup
            WHERE combo_item_id = ?
            ORDER BY display_order
        """;

        String optionSql = """
            SELECT option_id, option_name
            FROM ComboChoiceOption
            WHERE group_id = ?
            ORDER BY display_order
        """;

        try (Connection conn =
               DatabaseConnection.getConnection();

             PreparedStatement groupStmt =
               conn.prepareStatement(groupSql)) {

            groupStmt.setInt(1, comboItemId);

            try (ResultSet groupRs =
                   groupStmt.executeQuery()) {

                while (groupRs.next()) {

                    ComboChoiceGroup group =
                      new ComboChoiceGroup(
                        groupRs.getInt("group_id"),
                        groupRs.getString("group_name")
                      );

                    try (PreparedStatement optionStmt =
                           conn.prepareStatement(optionSql)) {

                        optionStmt.setInt(
                          1,
                          group.getId()
                        );

                        try (ResultSet optionRs =
                               optionStmt.executeQuery()) {

                            while (optionRs.next()) {

                                group.addOption(
                                  new ComboChoiceOption(
                                    optionRs.getInt("option_id"),
                                    optionRs.getString("option_name")
                                  )
                                );
                            }
                        }
                    }

                    groups.add(group);
                }
            }

        } catch (SQLException e) {

            System.out.println(
              "DB error (getComboChoiceGroupsByItem): "
                + e.getMessage()
            );
        }

        return groups;
    }

    // =========================
    // YOUR ADMIN USER STORIES
    // =========================

    public static void updateItemAvailability(
      int menuItemId,
      boolean available
    ) {

        String sql = """
            UPDATE MenuItem
            SET available = ?
            WHERE menu_item_id = ?
        """;

        try (Connection conn =
               DatabaseConnection.getConnection();

             PreparedStatement pstmt =
               conn.prepareStatement(sql)) {

            pstmt.setInt(
              1,
              available ? 1 : 0
            );

            pstmt.setInt(2, menuItemId);

            pstmt.executeUpdate();

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    public static List<MenuItem>
    getAllMenuItems() {

        List<MenuItem> items =
          new ArrayList<>();

        String sql = """
            SELECT menu_item_id,
                   name,
                   description,
                   price
            FROM MenuItem
            ORDER BY name
        """;

        try (Connection conn =
               DatabaseConnection.getConnection();

             PreparedStatement pstmt =
               conn.prepareStatement(sql);

             ResultSet rs =
               pstmt.executeQuery()) {

            while (rs.next()) {

                MenuItem item =
                  new MenuItem(
                    rs.getInt("menu_item_id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    ""
                  );

                items.add(item);
            }

        } catch (SQLException e) {

            System.out.println(
              "DB error: "
                + e.getMessage()
            );
        }

        return items;
    }

    public static boolean isItemAvailable(
      int itemId
    ) {

        String sql = """
            SELECT available
            FROM MenuItem
            WHERE menu_item_id = ?
        """;

        try (Connection conn =
               DatabaseConnection.getConnection();

             PreparedStatement pstmt =
               conn.prepareStatement(sql)) {

            pstmt.setInt(1, itemId);

            try (ResultSet rs =
                   pstmt.executeQuery()) {

                if (rs.next()) {

                    return rs.getInt("available") == 1;
                }
            }

        } catch (SQLException e) {

            System.out.println(
              "DB error: "
                + e.getMessage()
            );
        }

        return false;
    }

    public static void updateItemPrice(
      int itemId,
      double newPrice
    ) {

        String sql = """
            UPDATE MenuItem
            SET price = ?
            WHERE menu_item_id = ?
        """;

        try (Connection conn =
               DatabaseConnection.getConnection();

             PreparedStatement pstmt =
               conn.prepareStatement(sql)) {

            pstmt.setDouble(1, newPrice);

            pstmt.setInt(2, itemId);

            pstmt.executeUpdate();

        } catch (SQLException e) {

            System.out.println(
              "DB error: "
                + e.getMessage()
            );
        }
    }

    public static void updateItemName(
      int itemId,
      String newName
    ) {

        String sql = """
            UPDATE MenuItem
            SET name = ?
            WHERE menu_item_id = ?
        """;

        try (Connection conn =
               DatabaseConnection.getConnection();

             PreparedStatement pstmt =
               conn.prepareStatement(sql)) {

            pstmt.setString(1, newName);

            pstmt.setInt(2, itemId);

            pstmt.executeUpdate();

        } catch (SQLException e) {

            System.out.println(
              "DB error: "
                + e.getMessage()
            );
        }
    }

    public static void updateItemDescription(
      int itemId,
      String newDescription
    ) {

        String sql = """
            UPDATE MenuItem
            SET description = ?
            WHERE menu_item_id = ?
        """;

        try (Connection conn =
               DatabaseConnection.getConnection();

             PreparedStatement pstmt =
               conn.prepareStatement(sql)) {

            pstmt.setString(1, newDescription);

            pstmt.setInt(2, itemId);

            pstmt.executeUpdate();

        } catch (SQLException e) {

            System.out.println(
              "DB error: "
                + e.getMessage()
            );
        }
    }
}