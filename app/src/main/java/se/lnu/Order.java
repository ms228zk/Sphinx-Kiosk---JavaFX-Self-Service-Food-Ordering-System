package se.lnu;

import java.util.List;

public class Order {

    private static int counter = 1;
    private static String lastDate = "";
    private final String orderNumber;
    private final List<Cart.CartItem> items;

    public Order(List<Cart.CartItem> items) {
        this.items = items;

        // reset counter every new day
        String today = java.time.LocalDate.now().toString();
        if (!today.equals(lastDate)) {
            counter = 1;
            lastDate = today;
        }

        // generate 4‑digit order number
        this.orderNumber = String.format("%04d", counter);

        counter++;
        if (counter > 9999) {
            counter = 1;
        }
    }

    // getter for order number
    public String getOrderNumber() {
        return orderNumber;
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

    public double getTotalPrice() {
        double total = 0;

        for (Cart.CartItem item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    public List<Cart.CartItem> getItems() {
        return items;
    }
}
