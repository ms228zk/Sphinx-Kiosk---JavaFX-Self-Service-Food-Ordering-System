package se.lnu.database;

import java.sql.Connection;
import java.sql.ResultSet;
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
                  available INTEGER NOT NULL DEFAULT 1,
                  FOREIGN KEY (category_id) REFERENCES Category(category_id)
              )
              """);

      /*
       * If the database was created before the available column existed,
       * this safely adds it. If it already exists, SQLite throws an error,
       * which we ignore because that means the column is already there.
       */
      try {
        stmt.execute("ALTER TABLE MenuItem ADD COLUMN available INTEGER NOT NULL DEFAULT 1");
      } catch (SQLException e) {
        // Column already exists, so nothing needs to be done.
      }

      stmt.execute("""
              CREATE TABLE IF NOT EXISTS RemovableIngredient (
                  ingredient_id INTEGER PRIMARY KEY AUTOINCREMENT,
                  name TEXT NOT NULL UNIQUE
              )
              """);

      stmt.execute("""
              CREATE TABLE IF NOT EXISTS MenuItemRemovableIngredient (
                  menu_item_id INTEGER NOT NULL,
                  ingredient_id INTEGER NOT NULL,
                  PRIMARY KEY (menu_item_id, ingredient_id),
                  FOREIGN KEY (menu_item_id) REFERENCES MenuItem(menu_item_id),
                  FOREIGN KEY (ingredient_id) REFERENCES RemovableIngredient(ingredient_id)
              )
              """);

      stmt.execute("""
              CREATE TABLE IF NOT EXISTS ExtraOption (
                  extra_id INTEGER PRIMARY KEY AUTOINCREMENT,
                  name TEXT NOT NULL,
                  price REAL NOT NULL
              )
              """);

      stmt.execute("""
              CREATE TABLE IF NOT EXISTS MenuItemExtraOption (
                  menu_item_id INTEGER NOT NULL,
                  extra_id INTEGER NOT NULL,
                  PRIMARY KEY (menu_item_id, extra_id),
                  FOREIGN KEY (menu_item_id) REFERENCES MenuItem(menu_item_id),
                  FOREIGN KEY (extra_id) REFERENCES ExtraOption(extra_id)
              )
              """);

      stmt.execute("""
              CREATE TABLE IF NOT EXISTS ComboChoiceGroup (
                  group_id INTEGER PRIMARY KEY AUTOINCREMENT,
                  combo_item_id INTEGER NOT NULL,
                  group_name TEXT NOT NULL,
                  display_order INTEGER NOT NULL,
                  FOREIGN KEY (combo_item_id) REFERENCES MenuItem(menu_item_id)
              )
              """);

      stmt.execute("""
              CREATE TABLE IF NOT EXISTS ComboChoiceOption (
                  option_id INTEGER PRIMARY KEY AUTOINCREMENT,
                  group_id INTEGER NOT NULL,
                  option_name TEXT NOT NULL,
                  display_order INTEGER NOT NULL,
                  FOREIGN KEY (group_id) REFERENCES ComboChoiceGroup(group_id)
              )
              """);

      stmt.execute("""
              CREATE TABLE IF NOT EXISTS Orders (
                  id INTEGER PRIMARY KEY AUTOINCREMENT,
                  order_number INTEGER NOT NULL,
                  item_name TEXT NOT NULL,
                  quantity INTEGER NOT NULL,
                  date TEXT NOT NULL
              )
          """);


      /*
       * Important:
       * Only skip default seeding if MenuItem already has data.
       *
       * This prevents two problems:
       * 1. Admin-added items disappearing every time the app restarts.
       * 2. Categories existing but MenuItem being empty, which causes
       *    the customer side to show "No items available".
       */
      if (hasData(conn, "MenuItem")) {
        System.out.println("Database already contains menu items. Skipping default seed.");
        return;
      }

      /*
       * If MenuItem is empty, reset the seed-related tables and insert
       * the default menu data again.
       */
      stmt.execute("DELETE FROM ComboChoiceOption");
      stmt.execute("DELETE FROM ComboChoiceGroup");
      stmt.execute("DELETE FROM MenuItemExtraOption");
      stmt.execute("DELETE FROM ExtraOption");
      stmt.execute("DELETE FROM MenuItemRemovableIngredient");
      stmt.execute("DELETE FROM RemovableIngredient");
      stmt.execute("DELETE FROM MenuItem");
      stmt.execute("DELETE FROM Category");

      // Insert Categories
      stmt.execute("""
              INSERT INTO Category (category_id, name) VALUES
              (1, 'Burgers'),
              (2, 'Drinks'),
              (3, 'Sides'),
              (4, 'Desserts'),
              (5, 'Combos')
              """);

      // Insert Menu Items
      stmt.execute("""
              INSERT INTO MenuItem (category_id, name, description, price, available) VALUES
              (1, 'BBQ Smash Burger', 'Double beef patty with BBQ sauce', 49.00, 1),
              (1, 'Crispy Chicken Burger', 'Crispy chicken with garlic mayo', 45.00, 1),
              (1, 'Halloumi Burger', 'Grilled halloumi with salad', 42.00, 1),
              (2, 'Iced Coffee', 'Cold coffee with milk', 35.00, 1),
              (2, 'Mango Smoothie', 'Fresh mango smoothie', 38.00, 1),
              (2, 'Lemon Mint Cooler', 'Lemon drink with fresh mint', 30.00, 1),
              (3, 'Loaded Fries', 'Fries with cheese and sauce', 39.00, 1),
              (3, 'Mozzarella Sticks', 'Crispy cheese sticks with dip', 36.00, 1),
              (3, 'Spicy Chicken Bites', 'Small crispy spicy chicken pieces', 42.00, 1),
              (4, 'Chocolate Brownie', 'Warm brownie with chocolate sauce', 32.00, 1),
              (4, 'Mini Donuts', 'Three glazed mini donuts', 29.00, 1),
              (4, 'Ice Cream Sundae', 'Ice cream with toppings and syrup', 35.00, 1),
              (5, 'Family Feast', 'Great for sharing', 179.00, 1),
              (5, 'Kids Combo', 'Simple meal for kids', 69.00, 1),
              (5, 'Burger Combo', 'Burger, side, and drink combo', 99.00, 1),
              (5, 'Chicken Combo', 'Chicken, side, and drink combo', 109.00, 1),
              (5, 'Snack Box', 'Sides and drink combo', 89.00, 1)
              """);

      // Insert Extras
      stmt.execute("""
              INSERT INTO ExtraOption (name, price) VALUES
              ('Extra Cheese', 10.00),
              ('Extra Beef Patty', 20.00),
              ('Extra BBQ Sauce', 7.00),
              ('Extra Onion', 5.00),
              ('Extra Pickles', 5.00),
              ('Extra Chicken Patty', 20.00),
              ('Extra Garlic Mayo', 7.00),
              ('Extra Lettuce', 5.00),
              ('Extra Jalapenos', 8.00),
              ('Extra Halloumi', 18.00),
              ('Extra Salad', 5.00),
              ('Extra Garlic Sauce', 7.00),
              ('Extra Tomato', 5.00),
              ('Extra Shot', 10.00),
              ('Oat Milk', 6.00),
              ('Vanilla Syrup', 7.00),
              ('Caramel Syrup', 7.00),
              ('Whipped Cream', 8.00),
              ('Extra Mango', 8.00),
              ('Protein Boost', 12.00),
              ('Coconut Milk', 6.00),
              ('Chia Seeds', 6.00),
              ('Extra Mint', 5.00),
              ('Extra Lemon', 5.00),
              ('Ice Cubes', 3.00),
              ('Sugar Syrup', 5.00),
              ('Sparkling Water', 6.00),
              ('Extra Cheese Sauce', 10.00),
              ('Extra Chicken Bites', 15.00),
              ('Extra Marinara Dip', 7.00),
              ('Extra Garlic Dip', 7.00),
              ('Extra Cheese Dust', 6.00),
              ('Extra Spicy Dip', 7.00),
              ('Extra Spicy Sauce', 7.00),
              ('Extra Chocolate Sauce', 7.00),
              ('Vanilla Ice Cream', 12.00),
              ('Sprinkles', 5.00),
              ('Extra Glaze', 6.00),
              ('Chocolate Dip', 7.00),
              ('Caramel Dip', 7.00),
              ('Extra Caramel Sauce', 7.00),
              ('Extra Toppings', 8.00),
              ('Extra Large Fries', 25.00),
              ('Extra Drink', 20.00),
              ('Extra Sauce Pack', 15.00),
              ('Extra Juice', 15.00),
              ('Extra Small Fries', 15.00),
              ('Extra Dip', 5.00),
              ('Extra Patty', 20.00),
              ('Extra Fries', 20.00),
              ('Extra Sauce', 7.00),
              ('Extra Chicken', 20.00),
              ('Extra Nuggets', 20.00),
              ('Extra Mozzarella Sticks', 18.00)
              """);

      linkExtras(stmt, "BBQ Smash Burger", "'Extra Cheese','Extra Beef Patty','Extra BBQ Sauce','Extra Onion','Extra Pickles'");
      linkExtras(stmt, "Crispy Chicken Burger", "'Extra Cheese','Extra Chicken Patty','Extra Garlic Mayo','Extra Lettuce','Extra Jalapenos'");
      linkExtras(stmt, "Halloumi Burger", "'Extra Halloumi','Extra Salad','Extra Garlic Sauce','Extra Tomato','Extra Onion'");
      linkExtras(stmt, "Iced Coffee", "'Extra Shot','Oat Milk','Vanilla Syrup','Caramel Syrup','Whipped Cream'");
      linkExtras(stmt, "Mango Smoothie", "'Extra Mango','Protein Boost','Coconut Milk','Chia Seeds','Whipped Cream'");
      linkExtras(stmt, "Lemon Mint Cooler", "'Extra Mint','Extra Lemon','Ice Cubes','Sugar Syrup','Sparkling Water'");
      linkExtras(stmt, "Loaded Fries", "'Extra Cheese Sauce','Extra BBQ Sauce','Extra Jalapenos','Extra Chicken Bites','Extra Onion'");
      linkExtras(stmt, "Mozzarella Sticks", "'Extra Marinara Dip','Extra Garlic Dip','Extra Cheese Dust','Extra Spicy Dip'");
      linkExtras(stmt, "Spicy Chicken Bites", "'Extra Spicy Sauce','Extra Garlic Mayo','Extra BBQ Sauce','Extra Jalapenos'");
      linkExtras(stmt, "Chocolate Brownie", "'Extra Chocolate Sauce','Vanilla Ice Cream','Whipped Cream','Sprinkles'");
      linkExtras(stmt, "Mini Donuts", "'Extra Glaze','Chocolate Dip','Caramel Dip','Sprinkles'");
      linkExtras(stmt, "Ice Cream Sundae", "'Extra Chocolate Sauce','Extra Caramel Sauce','Extra Toppings','Whipped Cream'");
      linkExtras(stmt, "Family Feast", "'Extra Large Fries','Extra Drink','Extra Sauce Pack','Extra Chicken Bites'");
      linkExtras(stmt, "Kids Combo", "'Extra Juice','Extra Small Fries','Extra Dip'");
      linkExtras(stmt, "Burger Combo", "'Extra Cheese','Extra Patty','Extra Fries','Extra Sauce'");
      linkExtras(stmt, "Chicken Combo", "'Extra Chicken','Extra Garlic Mayo','Extra Fries','Extra Spicy Sauce'");
      linkExtras(stmt, "Snack Box", "'Extra Nuggets','Extra Mozzarella Sticks','Extra Dip','Extra Drink'");

      // Insert Combo Choices
      insertComboGroup(stmt, "Family Feast", "Choose Family Main", 1);
      insertComboGroup(stmt, "Family Feast", "Choose Sharing Side", 2);
      insertComboGroup(stmt, "Family Feast", "Choose Drink", 3);

      insertComboOption(stmt, "Family Feast", "Choose Family Main", "2 BBQ Smash Burgers + 2 Crispy Chicken Burgers", 1);
      insertComboOption(stmt, "Family Feast", "Choose Family Main", "2 Halloumi Burgers + 2 Crispy Chicken Burgers", 2);
      insertComboOption(stmt, "Family Feast", "Choose Family Main", "Mixed Burger Selection", 3);
      insertComboOption(stmt, "Family Feast", "Choose Sharing Side", "Loaded Fries", 1);
      insertComboOption(stmt, "Family Feast", "Choose Sharing Side", "Mozzarella Sticks", 2);
      insertComboOption(stmt, "Family Feast", "Choose Sharing Side", "Spicy Chicken Bites", 3);
      insertComboOption(stmt, "Family Feast", "Choose Sharing Side", "No Side", 4);

      insertComboGroup(stmt, "Kids Combo", "Choose Kids Main", 1);
      insertComboGroup(stmt, "Kids Combo", "Choose Kids Side", 2);
      insertComboGroup(stmt, "Kids Combo", "Choose Drink", 3);

      insertComboOption(stmt, "Kids Combo", "Choose Kids Main", "Crispy Chicken Burger", 1);
      insertComboOption(stmt, "Kids Combo", "Choose Kids Main", "Halloumi Burger", 2);
      insertComboOption(stmt, "Kids Combo", "Choose Kids Main", "Spicy Chicken Bites", 3);
      insertComboOption(stmt, "Kids Combo", "Choose Kids Side", "Mini Donuts", 1);
      insertComboOption(stmt, "Kids Combo", "Choose Kids Side", "Loaded Fries", 2);
      insertComboOption(stmt, "Kids Combo", "Choose Kids Side", "Mozzarella Sticks", 3);
      insertComboOption(stmt, "Kids Combo", "Choose Kids Side", "No Side", 4);

      insertComboGroup(stmt, "Burger Combo", "Choose Burger", 1);
      insertComboGroup(stmt, "Burger Combo", "Choose Side", 2);
      insertComboGroup(stmt, "Burger Combo", "Choose Drink", 3);

      insertComboOption(stmt, "Burger Combo", "Choose Burger", "BBQ Smash Burger", 1);
      insertComboOption(stmt, "Burger Combo", "Choose Burger", "Crispy Chicken Burger", 2);
      insertComboOption(stmt, "Burger Combo", "Choose Burger", "Halloumi Burger", 3);
      insertComboOption(stmt, "Burger Combo", "Choose Side", "Loaded Fries", 1);
      insertComboOption(stmt, "Burger Combo", "Choose Side", "Mozzarella Sticks", 2);
      insertComboOption(stmt, "Burger Combo", "Choose Side", "Spicy Chicken Bites", 3);
      insertComboOption(stmt, "Burger Combo", "Choose Side", "No Side", 4);

      insertComboGroup(stmt, "Chicken Combo", "Choose Chicken Main", 1);
      insertComboGroup(stmt, "Chicken Combo", "Choose Side", 2);
      insertComboGroup(stmt, "Chicken Combo", "Choose Drink", 3);

      insertComboOption(stmt, "Chicken Combo", "Choose Chicken Main", "Crispy Chicken Burger", 1);
      insertComboOption(stmt, "Chicken Combo", "Choose Chicken Main", "Spicy Chicken Bites", 2);
      insertComboOption(stmt, "Chicken Combo", "Choose Chicken Main", "Chicken Bites + Mozzarella Sticks", 3);
      insertComboOption(stmt, "Chicken Combo", "Choose Side", "Loaded Fries", 1);
      insertComboOption(stmt, "Chicken Combo", "Choose Side", "Mozzarella Sticks", 2);
      insertComboOption(stmt, "Chicken Combo", "Choose Side", "Spicy Chicken Bites", 3);
      insertComboOption(stmt, "Chicken Combo", "Choose Side", "No Side", 4);

      insertComboGroup(stmt, "Snack Box", "Choose Snack Main", 1);
      insertComboGroup(stmt, "Snack Box", "Choose Extra Snack", 2);
      insertComboGroup(stmt, "Snack Box", "Choose Drink", 3);

      insertComboOption(stmt, "Snack Box", "Choose Snack Main", "Loaded Fries", 1);
      insertComboOption(stmt, "Snack Box", "Choose Snack Main", "Mozzarella Sticks", 2);
      insertComboOption(stmt, "Snack Box", "Choose Snack Main", "Spicy Chicken Bites", 3);
      insertComboOption(stmt, "Snack Box", "Choose Extra Snack", "Mozzarella Sticks", 1);
      insertComboOption(stmt, "Snack Box", "Choose Extra Snack", "Mini Donuts", 2);
      insertComboOption(stmt, "Snack Box", "Choose Extra Snack", "Loaded Fries", 3);
      insertComboOption(stmt, "Snack Box", "Choose Extra Snack", "No Side", 4);

      insertComboDrinkOptions(stmt, "Family Feast");
      insertComboDrinkOptions(stmt, "Kids Combo");
      insertComboDrinkOptions(stmt, "Burger Combo");
      insertComboDrinkOptions(stmt, "Chicken Combo");
      insertComboDrinkOptions(stmt, "Snack Box");

      // Insert Removable Ingredients
      stmt.execute("""
              INSERT INTO RemovableIngredient (name) VALUES
              ('Pickles'),
              ('Caramelized onions'),
              ('BBQ sauce'),
              ('Mayo'),
              ('Cheese'),
              ('Bun (replace with gluten-free)'),
              ('Lettuce'),
              ('Tomato'),
              ('Sugar syrup'),
              ('Milk'),
              ('Whipped cream'),
              ('Ice'),
              ('Mint'),
              ('Soda (still water instead)'),
              ('Jalapeños'),
              ('Cheese sauce'),
              ('Meat topping'),
              ('Ketchup'),
              ('Marinara dip'),
              ('Breadcrumbs (replace with gluten-free)'),
              ('Spicy sauce'),
              ('Mayo dip'),
              ('Breading'),
              ('Nuts'),
              ('Chocolate syrup'),
              ('Ice cream'),
              ('Sugar coating'),
              ('Chocolate sauce'),
              ('Caramel sauce'),
              ('Cherry')
              """);

      linkRemovables(stmt, "BBQ Smash Burger", "'Pickles','Caramelized onions','BBQ sauce','Mayo','Cheese','Bun (replace with gluten-free)'");
      linkRemovables(stmt, "Crispy Chicken Burger", "'Mayo','Lettuce','Pickles','Cheese','Bun (replace with gluten-free)'");
      linkRemovables(stmt, "Halloumi Burger", "'Tomato','Lettuce','Mayo','Bun (replace with gluten-free)'");
      linkRemovables(stmt, "Iced Coffee", "'Sugar syrup','Milk','Whipped cream','Ice'");
      linkRemovables(stmt, "Mango Smoothie", "'Sugar syrup','Milk','Ice'");
      linkRemovables(stmt, "Lemon Mint Cooler", "'Sugar syrup','Mint','Ice','Soda (still water instead)'");
      linkRemovables(stmt, "Loaded Fries", "'Jalapeños','Cheese sauce','Mayo','Ketchup','Meat topping'");
      linkRemovables(stmt, "Mozzarella Sticks", "'Marinara dip','Breadcrumbs (replace with gluten-free)'");
      linkRemovables(stmt, "Spicy Chicken Bites", "'Spicy sauce','Mayo dip','Breading'");
      linkRemovables(stmt, "Chocolate Brownie", "'Nuts','Chocolate syrup','Ice cream'");
      linkRemovables(stmt, "Mini Donuts", "'Sugar coating','Chocolate sauce','Caramel sauce'");
      linkRemovables(stmt, "Ice Cream Sundae", "'Nuts','Whipped cream','Chocolate syrup','Cherry'");




      System.out.println("Database initialized successfully.");

    } catch (SQLException e) {
      System.out.println("Database initialization error: " + e.getMessage());
    }
  }

  private static boolean hasData(Connection conn, String tableName) throws SQLException {
    try (Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + tableName)) {

      return rs.next() && rs.getInt(1) > 0;
    }
  }

  private static void linkExtras(Statement stmt, String itemName, String extraNames) throws SQLException {
    stmt.execute("""
            INSERT INTO MenuItemExtraOption (menu_item_id, extra_id)
            SELECT m.menu_item_id, e.extra_id
            FROM MenuItem m, ExtraOption e
            WHERE m.name = '%s'
            AND e.name IN (%s)
            """.formatted(itemName, extraNames));
  }

  private static void linkRemovables(Statement stmt, String itemName, String ingredientNames) throws SQLException {
    stmt.execute("""
            INSERT INTO MenuItemRemovableIngredient (menu_item_id, ingredient_id)
            SELECT m.menu_item_id, i.ingredient_id
            FROM MenuItem m, RemovableIngredient i
            WHERE m.name = '%s'
            AND i.name IN (%s)
            """.formatted(itemName, ingredientNames));
  }

  private static void insertComboGroup(
          Statement stmt,
          String comboName,
          String groupName,
          int displayOrder
  ) throws SQLException {
    stmt.execute("""
            INSERT INTO ComboChoiceGroup (combo_item_id, group_name, display_order)
            SELECT menu_item_id, '%s', %d
            FROM MenuItem
            WHERE name = '%s'
            """.formatted(groupName, displayOrder, comboName));
  }

  private static void insertComboOption(
          Statement stmt,
          String comboName,
          String groupName,
          String optionName,
          int displayOrder
  ) throws SQLException {
    stmt.execute("""
            INSERT INTO ComboChoiceOption (group_id, option_name, display_order)
            SELECT g.group_id, '%s', %d
            FROM ComboChoiceGroup g
            JOIN MenuItem m ON g.combo_item_id = m.menu_item_id
            WHERE m.name = '%s'
            AND g.group_name = '%s'
            """.formatted(optionName, displayOrder, comboName, groupName));
  }

  private static void insertComboDrinkOptions(Statement stmt, String comboName) throws SQLException {
    insertComboOption(stmt, comboName, "Choose Drink", "Iced Coffee", 1);
    insertComboOption(stmt, comboName, "Choose Drink", "Mango Smoothie", 2);
    insertComboOption(stmt, comboName, "Choose Drink", "Lemon Mint Cooler", 3);
  }
}