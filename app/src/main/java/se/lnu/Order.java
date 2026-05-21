package se.lnu;

import java.util.List;

public class Order {

    private final String orderNumber;
    private final List<Cart.CartItem> items;
    private final String paymentMethod;

    public Order(String orderNumber, List<Cart.CartItem> items, String paymentMethod) {
        this.orderNumber = orderNumber;
        this.items = items;
        this.paymentMethod = paymentMethod;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public List<Cart.CartItem> getItems() {
        return items;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public double getTotalPrice() {
        double total = 0;
        for (Cart.CartItem item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    public String getItemList() {
        StringBuilder sb = new StringBuilder();
        for (Cart.CartItem item : items) {
            sb.append(item.getQuantity())
                    .append(" x ")
                    .append(item.getMenuItem().getName())
                    .append("\n");
        }
        return sb.toString();
    }
}
