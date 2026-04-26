package se.lnu;

public class MenuItem {
  private final int id;
  private final int categoryId;
  private final String name;
  private final String description;
  private final double price;

  public MenuItem(int id, int categoryId, String name, String description, double price) {
    this.id = id;
    this.categoryId = categoryId;
    this.name = name;
    this.description = description;
    this.price = price;
  }

  public int getId() {
    return id;
  }

  public int getCategoryId() {
    return categoryId;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public double getPrice() {
    return price;
  }
}
