package se.lnu;

import java.util.ArrayList;
import java.util.List;

public class MenuItem {

  private final int id;
  private final String name;
  private final String description;
  private final double price;
  private final String imageFileName;

  private List<RemovableIngredient> removableIngredients = new ArrayList<>();

  public MenuItem(int id, String name, String description, double price) {
    this(id, name, description, price, null);
  }

  public MenuItem(int id, String name, String description, double price, String imageFileName) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.price = price;
    this.imageFileName = imageFileName;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDescription() { return description; }

  public double getPrice() { return price; }

  public String getImageFileName() { return imageFileName; }

  public void addRemovableIngredient(RemovableIngredient ingredient) {
    removableIngredients.add(ingredient);
  }

  public void setRemovableIngredients(List<RemovableIngredient> ingredients) {
    this.removableIngredients = ingredients;
  }

  public List<RemovableIngredient> getRemovableIngredients() {
    return removableIngredients;
  }
}