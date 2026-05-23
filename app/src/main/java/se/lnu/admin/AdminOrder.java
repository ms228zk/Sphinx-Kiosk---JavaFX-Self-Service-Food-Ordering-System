package se.lnu.admin;

public class AdminOrder {

  private final int orderNumber;
  private final String date;
  private final String items;
  private final String status;

  public AdminOrder(int orderNumber, String date, String items, String status) {
    this.orderNumber = orderNumber;
    this.date = date;
    this.items = items;
    this.status = status;
  }

  public int getOrderNumber() {
    return orderNumber;
  }

  public String getFormattedOrderNumber() {
    return String.format("%04d", orderNumber);
  }

  public String getDate() {
    return date;
  }

  public String getItems() {
    return items;
  }

  public String getStatus() {
    return status;
  }
}