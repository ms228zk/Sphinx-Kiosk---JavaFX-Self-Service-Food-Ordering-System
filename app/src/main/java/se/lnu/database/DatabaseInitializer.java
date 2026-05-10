package se.lnu.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

  public static void initialize() {

    try (Connection conn = DatabaseConnection.getConnection();
         Statement stmt = conn.createStatement()) {

      // Create Category table
      stmt.execute("""
              CREATE TABLE IF NOT EXISTS Category (
                  category_id INTEGER PRIMARY KEY AUTOINCREMENT,
                  name TEXT NOT NULL
              )
              """);

      // Create MenuItem table
      stmt.execute("""
              CREATE TABLE IF NOT EXISTS MenuItem (
                  menu_item_id INTEGER PRIMARY KEY AUTOINCREMENT,
                  category_id INTEGER NOT NULL,
                  name TEXT NOT NULL,
                  description TEXT,
                  price REAL NOT NULL,
                  FOREIGN KEY (category_id) REFERENCES Category(category_id)
              )
              """);

      // Create Ingredient table
      stmt.execute("""
              CREATE TABLE IF NOT EXISTS RemovableIngredient (
                  ingredient_id INTEGER PRIMARY KEY AUTOINCREMENT,
                  name TEXT NOT NULL UNIQUE
              )
              """);

      // Create junction table
      stmt.execute("""
              CREATE TABLE IF NOT EXISTS MenuItemRemovableIngredient (
                  menu_item_id INTEGER NOT NULL,
                  ingredient_id INTEGER NOT NULL,
                  PRIMARY KEY (menu_item_id, ingredient_id),
                  FOREIGN KEY (menu_item_id) REFERENCES MenuItem(menu_item_id),
                  FOREIGN KEY (ingredient_id) REFERENCES RemovableIngredient(ingredient_id)
              )
              """);

      // Reset database
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
              INSERT INTO MenuItem (category_id, name, description, price) VALUES

              -- Burgers
              (1, 'BBQ Smash Burger', 'Double beef patty with BBQ sauce', 49.00),
              (1, 'Crispy Chicken Burger', 'Crispy chicken with garlic mayo', 45.00),
              (1, 'Halloumi Burger', 'Grilled halloumi with salad', 42.00),

              -- Drinks
              (2, 'Iced Coffee', 'Cold coffee with milk', 35.00),
              (2, 'Mango Smoothie', 'Fresh mango smoothie', 38.00),
              (2, 'Lemon Mint Cooler', 'Lemon drink with fresh mint', 30.00),

              -- Sides
              (3, 'Loaded Fries', 'Fries with cheese and sauce', 39.00),
              (3, 'Mozzarella Sticks', 'Crispy cheese sticks with dip', 36.00),
              (3, 'Spicy Chicken Bites', 'Small crispy spicy chicken pieces', 42.00),

              -- Desserts
              (4, 'Chocolate Brownie', 'Warm brownie with chocolate sauce', 32.00),
              (4, 'Mini Donuts', 'Three glazed mini donuts', 29.00),
              (4, 'Ice Cream Sundae', 'Ice cream with toppings and syrup', 35.00),

              -- Combos
              (5, 'Family Feast', 'Great for sharing', 179.00),

              (5, 'Kids Combo', 'Simple meal for kids', 69.00),

              (5, 'Burger Combo', 'Burger, side, and drink combo', 99.00),

              (5, 'Chicken Combo', 'Chicken, side, and drink combo', 109.00),

              (5, 'Snack Box', 'Sides and drink combo', 89.00)
              """);

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

      stmt.execute("""
              INSERT INTO MenuItemRemovableIngredient (menu_item_id, ingredient_id)
              SELECT m.menu_item_id, i.ingredient_id
              FROM MenuItem m, RemovableIngredient i
              WHERE m.name = 'BBQ Smash Burger'
              AND i.name IN ('Pickles', 'Caramelized onions', 'BBQ sauce', 'Mayo', 'Cheese', 'Bun (replace with gluten-free)')
              """);

      stmt.execute("""
              INSERT INTO MenuItemRemovableIngredient (menu_item_id, ingredient_id)
              SELECT m.menu_item_id, i.ingredient_id
              FROM MenuItem m, RemovableIngredient i
              WHERE m.name = 'Crispy Chicken Burger'
              AND i.name IN ('Mayo', 'Lettuce', 'Pickles', 'Cheese', 'Bun (replace with gluten-free)')
              """);

      stmt.execute("""
              INSERT INTO MenuItemRemovableIngredient (menu_item_id, ingredient_id)
              SELECT m.menu_item_id, i.ingredient_id
              FROM MenuItem m, RemovableIngredient i
              WHERE m.name = 'Halloumi Burger'
              AND i.name IN ('Tomato', 'Lettuce', 'Mayo', 'Bun (replace with gluten-free)')
              """);

      stmt.execute("""
              INSERT INTO MenuItemRemovableIngredient (menu_item_id, ingredient_id)
              SELECT m.menu_item_id, i.ingredient_id
              FROM MenuItem m, RemovableIngredient i
              WHERE m.name = 'Iced Coffee'
              AND i.name IN ('Sugar syrup', 'Milk', 'Whipped cream', 'Ice')
              """);

      stmt.execute("""
              INSERT INTO MenuItemRemovableIngredient (menu_item_id, ingredient_id)
              SELECT m.menu_item_id, i.ingredient_id
              FROM MenuItem m, RemovableIngredient i
              WHERE m.name = 'Mango Smoothie'
              AND i.name IN ('Sugar syrup', 'Milk', 'Ice')
              """);

      stmt.execute("""
              INSERT INTO MenuItemRemovableIngredient (menu_item_id, ingredient_id)
              SELECT m.menu_item_id, i.ingredient_id
              FROM MenuItem m, RemovableIngredient i
              WHERE m.name = 'Lemon Mint Cooler'
              AND i.name IN ('Sugar syrup', 'Mint', 'Ice', 'Soda (still water instead)')
              """);

      stmt.execute("""
              INSERT INTO MenuItemRemovableIngredient (menu_item_id, ingredient_id)
              SELECT m.menu_item_id, i.ingredient_id
              FROM MenuItem m, RemovableIngredient i
              WHERE m.name = 'Loaded Fries'
              AND i.name IN ('Jalapeños', 'Cheese sauce', 'Mayo', 'Ketchup', 'Meat topping')
              """);

      stmt.execute("""
              INSERT INTO MenuItemRemovableIngredient (menu_item_id, ingredient_id)
              SELECT m.menu_item_id, i.ingredient_id
              FROM MenuItem m, RemovableIngredient i
              WHERE m.name = 'Mozzarella Sticks'
              AND i.name IN ('Marinara dip', 'Breadcrumbs (replace with gluten-free)')
              """);

      stmt.execute("""
              INSERT INTO MenuItemRemovableIngredient (menu_item_id, ingredient_id)
              SELECT m.menu_item_id, i.ingredient_id
              FROM MenuItem m, RemovableIngredient i
              WHERE m.name = 'Spicy Chicken Bites'
              AND i.name IN ('Spicy sauce', 'Mayo dip', 'Breading')
              """);

      stmt.execute("""
              INSERT INTO MenuItemRemovableIngredient (menu_item_id, ingredient_id)
              SELECT m.menu_item_id, i.ingredient_id
              FROM MenuItem m, RemovableIngredient i
              WHERE m.name = 'Chocolate Brownie'
              AND i.name IN ('Nuts', 'Chocolate syrup', 'Ice cream')
              """);

      stmt.execute("""
              INSERT INTO MenuItemRemovableIngredient (menu_item_id, ingredient_id)
              SELECT m.menu_item_id, i.ingredient_id
              FROM MenuItem m, RemovableIngredient i
              WHERE m.name = 'Mini Donuts'
              AND i.name IN ('Sugar coating', 'Chocolate sauce', 'Caramel sauce')
              """);

      stmt.execute("""
              INSERT INTO MenuItemRemovableIngredient (menu_item_id, ingredient_id)
              SELECT m.menu_item_id, i.ingredient_id
              FROM MenuItem m, RemovableIngredient i
              WHERE m.name = 'Ice Cream Sundae'
              AND i.name IN ('Nuts', 'Whipped cream', 'Chocolate syrup', 'Cherry')
              """);

      System.out.println("Database initialized successfully.");

    } catch (SQLException e) {
      System.out.println("Database initialization error: " + e.getMessage());
    }
  }
}