package se.lnu;

import java.util.List;

public class Order {

    private final List<Cart.CartItem> items;

    public Order(List<Cart.CartItem> items) {
        this.items = items;
    }

    public String getItemList() {
        StringBuilder sb = new StringBuilder();
        for (Cart.CartItem item : items) {
            sb.append(item.quantity)
                    .append(" x ")
                    .append(item.menuItem.getName())
                    .append("\n");
        }
        return sb.toString();
    }

    public double getTotalPrice() {
        double total = 0;
        for (Cart.CartItem item : items) {
            total += item.menuItem.getPrice() * item.quantity;
        }
        return total;
    }
}